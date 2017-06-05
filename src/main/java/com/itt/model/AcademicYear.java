package com.itt.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("academic_year")
public class AcademicYear {

    @Id
    private ObjectId objectId;

    /** 2016-2017*/
    private String years;

    public String getYears() {
        return years;
    }

    public AcademicYear setYears(String years) {
        this.years = years;
        return this;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public AcademicYear setObjectId(ObjectId objectId) {
        this.objectId = objectId;
        return this;
    }
}
