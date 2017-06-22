package com.itt.service;

import com.itt.model.*;
import com.itt.model.DAO.*;
import com.mongodb.*;
import com.mongodb.util.JSON;
import javassist.NotFoundException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpRetryException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CalendarService {


    private AcademicYearDao academicYearDao;
    private ActivityGroupDao activityGroupDao;
    private ActivityDao activityDao;
    private SemesterDao semesterDao;
    private DateUtilService dateUtilService;

    @Autowired
    public CalendarService(DbManagerService dbManagerService, DateUtilService dateUtilService) {

        this.academicYearDao = new AcademicYearDaoImplementation(AcademicYear.class, dbManagerService.getDataStore());
        this.activityGroupDao = new ActivityGroupDaoImplementation(ActivityGroup.class, dbManagerService.getDataStore());
        this.activityDao = new ActivityDaoImplementation(Activity.class, dbManagerService.getDataStore());
        this.semesterDao = new SemesterDaoImplementation(Semester.class, dbManagerService.getDataStore());
        this.dateUtilService = dateUtilService;
    }


    public void saveAcademicYearFormCalendar(String jsonCalendar) throws ParseException {

        DBObject dbObject = (DBObject) JSON.parse(jsonCalendar);

        AcademicYear academicYear = new AcademicYear();
        academicYear.setYears((String) dbObject.get("years"));
        Key academicYearId = academicYearDao.save(academicYear);
        academicYear.setObjectId((ObjectId) academicYearId.getId());

        Semester semester1 = new Semester();
        semester1.setAcademicYear(academicYear)
                .setKey("sem-1")
                .setNumber(1);

        semester1.setObjectId(
                (ObjectId) semesterDao.save(semester1).getId()
        );

        Semester semester2 = new Semester();
        semester2.setAcademicYear(academicYear)
                .setKey("sem-2")
                .setNumber(2);

        semester2.setObjectId(
                (ObjectId) semesterDao.save(semester2).getId()
        );

        this.setAndSaveSubFieldsForSemester(dbObject, semester1);
        this.setAndSaveSubFieldsForSemester(dbObject, semester2);
    }

    public DBObject getWeekNumber(String dateAsString, Integer yearOfStudy, String specialization) throws ParseException, NotFoundException, HttpRetryException {
        Date date = this.getDateFromString(dateAsString);

        Activity activityTmp = this.activityDao.getOneForDate(date);

        if (activityTmp == null) {
            throw new NotFoundException("No activity was founded on this period");
        }

        Semester semester = activityTmp.getActivityGroup().getSemester();

        ActivityGroup activityGroup = this.activityGroupDao.findBySemesterYearOfStudyAndSpecialization(
                semester,
                yearOfStudy,
                specialization
        );

        Activity activity = this.activityDao.getOneForDateAndGroup(date, activityGroup);

        Integer weekNumber = this.calculateWeekNumberByActivity(activity, date);

        DBObject retData = new BasicDBObject();
        retData.put("weekNumber", weekNumber);
        retData.put("activity", activity);

        return retData;
    }

    /**
     * @param dateAsString
     * @return BasicDBObject
     * @throws ParseException
     * @throws NotFoundException
     * @throws HttpRetryException
     */
    public DBObject getWeekNumber(String dateAsString) throws ParseException, NotFoundException, HttpRetryException {
        Date date = this.getDateFromString(dateAsString);

        List<Activity> activityTmpList = this.activityDao.getAllForDate(date);

        if (activityTmpList.isEmpty()) {
            throw new NotFoundException("No activities were founded on this period");
        }

        List<BasicDBObject> weekNumbers = new ArrayList<>();

        for (Activity act : activityTmpList) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("weekNumber", this.calculateWeekNumberByActivity(act, date));
            obj.put("activity", act);

            weekNumbers.add(obj);
        }


        return new BasicDBObject("data", weekNumbers);
    }

    private Integer calculateWeekNumberByActivity(Activity imputActivity, Date date) throws NotFoundException {

        if (imputActivity == null) {
            throw new NotFoundException("No activity were founded on this period");
        }

        ActivityGroup activityGroup = imputActivity.getActivityGroup();
        Activity activity = this.activityDao.getOneForDateAndGroup(date, activityGroup);

        if (activity == null) {
            throw new NotFoundException("No activity was founded on this date" + date + " was not found");
        }

        List<Activity> activities = this.activityDao.getAllWithGroup(activityGroup);

        //do the magic

        Integer weekNumber = 0;

        for (Activity tmpActivity : activities) {
            Integer startDateComparision = tmpActivity.getPeriod().getStartDate().compareTo(date);
            Integer endDatecomparision = tmpActivity.getPeriod().getEndDate().compareTo(date);

            if (endDatecomparision < 0) {

                if (tmpActivity.getActivityType().equals("PREDARE")) {
                    weekNumber += tmpActivity.getPeriod().getNumberOfWeeks();
                }

            } else {

                if (!tmpActivity.getActivityType().equals("PREDARE")) {
                    break;
                }

                weekNumber += dateUtilService.getWeeksBetween(tmpActivity.getPeriod().getStartDate(), date);

                if ((date.getDay() - tmpActivity.getPeriod().getStartDate().getDay()) % 7 == 0) {
                    weekNumber += 1;
                }

                break;
            }
        }

        return weekNumber;
    }

    private void setAndSaveSubFieldsForSemester(DBObject calendar, Semester semester) throws ParseException {
        for (Object object : ((BasicDBList) ((BasicDBObject) calendar.get(semester.getKey())).get("grupuri"))) {
            ActivityGroup activityGroup = new ActivityGroup();
            activityGroup.setSemester(semester);

            BasicDBObject as = (BasicDBObject) ((BasicDBObject) object).get("anul_studii");

            YearOfStudy yearOfStudy = new YearOfStudy();

            for (Object licenseYear : (BasicDBList) as.get("licenta")) {
                yearOfStudy.getLicense().add((Integer) licenseYear);
            }

            for (Object masterYear : (BasicDBList) as.get("master")) {
                yearOfStudy.getMaster().add((Integer) masterYear);
            }

            activityGroup.setYearOfStudy(yearOfStudy);

            activityGroup.setObjectId(
                    (ObjectId) activityGroupDao.save(activityGroup).getId()
            );

            for (Object data : (BasicDBList) ((BasicDBObject) object).get("lista_activitati")) {

                String startDateAsString = (String) ((BasicDBObject) ((BasicDBObject) data).get("perioada")).get("inceput");
                String endDateAsString = (String) ((BasicDBObject) ((BasicDBObject) data).get("perioada")).get("sfarsit");
                Integer weeksNumber = (Integer) ((BasicDBObject) ((BasicDBObject) data).get("perioada")).get("nr_saptamani");

                Activity activity = new Activity(
                        (String) ((BasicDBObject) data).get("nume_activitate"),
                        (String) ((BasicDBObject) data).get("tip_activitate"),
                        new Period(
                                this.getDateFromString(startDateAsString),
                                this.getDateFromString(endDateAsString),
                                weeksNumber
                        )
                );

                activity.setActivityGroup(activityGroup);
                activityDao.save(activity);
            }
        }
    }

    private Date getDateFromString(String date) throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy").parse(date);
    }
}
