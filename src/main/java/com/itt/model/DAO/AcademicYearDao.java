package com.itt.model.DAO;


import com.itt.model.AcademicYear;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;


public interface AcademicYearDao extends DAO<AcademicYear, ObjectId>{

    /**
     */
    public AcademicYear getOneByName(String firstName);

}