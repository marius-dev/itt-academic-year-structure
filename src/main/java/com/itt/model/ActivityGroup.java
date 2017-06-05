package com.itt.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("activity_group")
public class ActivityGroup {
    @Id
    private ObjectId objectId;

    @Embedded
    private YearOfStudy yearOfStudy;

    @Reference
    private Semester semester;

    public ObjectId getObjectId() {
        return objectId;
    }

    public ActivityGroup setObjectId(ObjectId objectId) {
        this.objectId = objectId;
        return this;
    }

    public YearOfStudy getYearOfStudy() {
        return yearOfStudy;
    }

    public ActivityGroup setYearOfStudy(YearOfStudy yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
        return this;
    }

    public Semester getSemester() {
        return semester;
    }

    public ActivityGroup setSemester(Semester semester) {
        this.semester = semester;
        return this;
    }
}
