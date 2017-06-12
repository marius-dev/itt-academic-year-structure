package com.itt.model.DAO;

import com.itt.model.ActivityGroup;
import com.itt.model.Semester;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

import java.net.HttpRetryException;
import java.util.Date;


public interface ActivityGroupDao extends DAO<ActivityGroup, ObjectId>{


    public ActivityGroup findBySemesterYearOfStudyAndSpecialization(Semester semester, Integer yearOfStudy, String specialization) throws HttpRetryException;

}