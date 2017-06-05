package com.itt.model.DAO;


import com.itt.model.AcademicYear;
import com.itt.model.Activity;
import com.itt.model.ActivityGroup;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.Date;
import java.util.List;


public class ActivityDaoImplementation extends BasicDAO<Activity, ObjectId> implements ActivityDao{

    public ActivityDaoImplementation(Class<Activity> entityClass, Datastore ds) {
        super(entityClass, ds);
    }


    public Activity getOneForDateAndGroup(Date date, ActivityGroup activityGroup) {
        Query<Activity> query = createQuery();

        query.field("period.startDate").lessThan(date);
        query.field("period.startDate").lessThan(date);
        query.field("activityGroup").equal(activityGroup);

        return query.get();
    }

    public List<Activity> getAllWithGroup(ActivityGroup activityGroup) {
        Query<Activity> query = createQuery();

        query.field("activityGroup").equal(activityGroup);

        return query.order("period.startDate").asList();
    }
}