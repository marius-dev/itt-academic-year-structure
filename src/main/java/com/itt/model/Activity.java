package com.itt.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("activity")
public class Activity {
    @Id
    private ObjectId id;

    private String activityName;

    @Reference
    private ActivityGroup activityGroup;

    private String activityType;

    private Integer numberOfWeeks;

    @Embedded
    private Period period;

    public Activity(String activityName, String activityType, Period period) {
        this.activityName = activityName;
        this.activityType = activityType;
        this.period = period;
    }

    public Activity() {

    }

    public ObjectId getId() {
        return id;
    }

    public Activity setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public String getActivityName() {
        return activityName;
    }

    public Activity setActivityName(String activityName) {
        this.activityName = activityName;
        return this;
    }

    public ActivityGroup getActivityGroup() {
        return activityGroup;
    }

    public Activity setActivityGroup(ActivityGroup activityGroup) {
        this.activityGroup = activityGroup;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public Activity setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public Period getPeriod() {
        return period;
    }

    public Activity setPeriod(Period period) {
        this.period = period;
        return this;
    }
}