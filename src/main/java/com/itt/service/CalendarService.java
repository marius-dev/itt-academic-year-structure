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

        AcademicYear academicYear= new AcademicYear();
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


    public Integer getWeekNumber(String dateAsString, String academicYearAsString, Integer semesterNumber,  Integer yearOfStudy, String specialization) throws ParseException, NotFoundException, HttpRetryException {
        Date date = this.getDateFromString(dateAsString);

        AcademicYear academicYear = academicYearDao.getOneByName(academicYearAsString);

        if(academicYear == null){
            throw new NotFoundException("Academic year with name:" + academicYearAsString + " was not found");
        }

        Semester semester = this.semesterDao.findOneByNumber(semesterNumber, academicYear);

        ActivityGroup activityGroup = this.activityGroupDao.findBySemesterYearOfStudyAndSpecialization(
                semester,
                yearOfStudy,
                specialization
        );

        Activity activity = this.activityDao.getOneForDateAndGroup(date, activityGroup);

        if(activity == null){
            throw new NotFoundException("No activity was founded on this date" + date + " was not found");
        }

        List<Activity> activities = this.activityDao.getAllWithGroup(activityGroup);

        //do the magic

        Integer weekNumber = 0;

        for (Activity tmpActivity: activities) {
            Integer startDateComparision = tmpActivity.getPeriod().getStartDate().compareTo(date);
            Integer endDatecomparision = tmpActivity.getPeriod().getEndDate().compareTo(date);

            if(endDatecomparision < 0){

                if(tmpActivity.getActivityType().equals("PREDARE")) {
                    weekNumber += tmpActivity.getPeriod().getNumberOfWeeks();
                }

            } else {

                if(!tmpActivity.getActivityType().equals("PREDARE")) {
                    break;
                }

                weekNumber += dateUtilService.getWeeksBetween(tmpActivity.getPeriod().getStartDate(), date);

                if((date.getDay() - tmpActivity.getPeriod().getStartDate().getDay()) % 7 == 0){
                    weekNumber +=1;
                }

                break;
            }
        }

        return weekNumber;
    }

    private void setAndSaveSubFieldsForSemester(DBObject calendar, Semester semester) throws ParseException {
        for (Object object: ((BasicDBList)((BasicDBObject) calendar.get(semester.getKey())).get("grupuri"))) {
            ActivityGroup activityGroup = new ActivityGroup();
            activityGroup.setSemester(semester);

            BasicDBObject as = (BasicDBObject)((BasicDBObject )object).get("anul_studii");

            YearOfStudy yearOfStudy = new YearOfStudy();

            for (Object licenseYear: (BasicDBList)as.get("licenta")) {
                yearOfStudy.getLicense().add((Integer) licenseYear);
            }

            for (Object masterYear: (BasicDBList)as.get("master")) {
                yearOfStudy.getMaster().add((Integer) masterYear);
            }

            activityGroup.setYearOfStudy(yearOfStudy);

            activityGroup.setObjectId(
                    (ObjectId) activityGroupDao.save(activityGroup).getId()
            );

            for (Object data: (BasicDBList) ((BasicDBObject )object).get("lista_activitati")) {

                String startDateAsString = (String) ((BasicDBObject) ((BasicDBObject) data).get("perioada")).get("inceput");
                String endDateAsString  = (String) ((BasicDBObject) ((BasicDBObject) data).get("perioada")).get("sfarsit");
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
         return  new SimpleDateFormat("dd-MM-yyyy").parse(date);
    }
}
