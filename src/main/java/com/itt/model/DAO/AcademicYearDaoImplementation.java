package com.itt.model.DAO;


import com.itt.model.AcademicYear;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;


public class AcademicYearDaoImplementation  extends BasicDAO<AcademicYear, ObjectId> implements AcademicYearDao{

    public AcademicYearDaoImplementation(Class<AcademicYear> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public AcademicYear getOneByName(String name) {
        Query<AcademicYear> query = createQuery().
                field("years").equal(name);

        return query.get();
    }
}