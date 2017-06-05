package com.itt.model.DAO;


import com.itt.model.Activity;
import com.itt.model.ActivityGroup;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

import java.util.Date;
import java.util.List;


public interface ActivityDao extends DAO<Activity, ObjectId>{


    public Activity getOneForDateAndGroup(Date date, ActivityGroup activityGroup);

    public List<Activity> getAllWithGroup(ActivityGroup activityGroup);
}