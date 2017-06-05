package com.itt.model;

import java.util.Date;


public class Period {
    private Date startDate;
    private Date endDate;
    private Integer numberOfWeeks;

    public Period(Date startDate, Date endDate, Integer numberOfWeeks) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfWeeks = numberOfWeeks;
    }

    public Period() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public Period setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Period setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public Period setNumberOfWeeks(Integer numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
        return this;
    }
}
