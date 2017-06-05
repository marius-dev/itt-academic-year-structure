package com.itt.model.DAO;

import com.itt.model.ActivityGroup;
import com.itt.model.Semester;
import javassist.tools.web.BadHttpRequest;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.springframework.http.HttpStatus;

import javax.xml.ws.http.HTTPException;
import java.net.HttpRetryException;


public class ActivityGroupDaoImplementation extends BasicDAO<ActivityGroup, ObjectId> implements ActivityGroupDao{

    public ActivityGroupDaoImplementation(Class<ActivityGroup> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public ActivityGroup findBySemesterYearOfStudyAndSpecialization(Semester semester, Integer yearOfStudy, String specialization) throws HttpRetryException {
        Query<ActivityGroup> query = createQuery()
                .field("semester").equal(semester)
                .field("yearOfStudy."+this.normalizeSpecialization(specialization)).equal(yearOfStudy);

        return query.get();
    }

    private String normalizeSpecialization(String specialization) throws HttpRetryException {

        switch (specialization.toLowerCase()) {
            case "licenta":
                return "license";
            case "master":
                return "mester";
            default:
                throw new HttpRetryException("There is no specialization named: "+ specialization, 400);
        }
    }
}