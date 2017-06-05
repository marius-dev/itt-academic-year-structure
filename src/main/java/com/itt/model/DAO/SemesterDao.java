package com.itt.model.DAO;

import com.itt.model.AcademicYear;
import com.itt.model.Semester;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.Query;


public interface SemesterDao extends DAO<Semester, ObjectId>{

    public Semester findOneByNumber(Integer number, AcademicYear academicYear);
}