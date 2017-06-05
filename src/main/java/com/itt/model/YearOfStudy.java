package com.itt.model;


import java.util.ArrayList;
import java.util.List;

public class YearOfStudy {

    private List<Integer> license;
    private List<Integer> master;


    public YearOfStudy() {
        this.license = license = new ArrayList<>();
        this.master = master = new ArrayList<>();
    }

    public List<Integer> getLicense() {
        return license;
    }

    public YearOfStudy setLicense(List<Integer> license) {
        this.license = license;
        return this;
    }

    public List<Integer> getMaster() {
        return master;
    }

    public YearOfStudy setMaster(List<Integer> master) {
        this.master = master;
        return this;
    }
}
