package com.itt.model.DAO;

import com.itt.model.AcademicYear;
import com.itt.model.Semester;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;


public class SemesterDaoImplementation extends BasicDAO<Semester, ObjectId> implements SemesterDao{

    public SemesterDaoImplementation(Class<Semester> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public Semester findOneByNumber(Integer number, AcademicYear academicYear) {
        Query<Semester> query = createQuery().
                field("number").equal(number)
                .field("academicYear").equal(academicYear);

        return query.get();
    }
}