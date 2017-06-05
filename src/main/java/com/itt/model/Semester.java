package com.itt.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

@Entity("semester")
public class Semester {

    @Id
    private ObjectId objectId;

    @Reference
    private AcademicYear academicYear;

    private Integer number;

    private String key;

    public ObjectId getObjectId() {
        return objectId;
    }

    public Semester setObjectId(ObjectId objectId) {
        this.objectId = objectId;
        return this;
    }

    public AcademicYear getAcademicYear() {
        return academicYear;
    }

    public Semester setAcademicYear(AcademicYear academicYear) {
        this.academicYear = academicYear;
        return this;
    }

    public Integer getNumber() {
        return number;
    }

    public Semester setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Semester setKey(String key) {
        this.key = key;
        return this;
    }
}
