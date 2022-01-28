package com.example.trackcovid19;

public class StatesModel {
    private String state, stateCode;
    private String cases, deaths, active, recovered;

    public StatesModel(String state, String active, String cases, String deaths, String recovered) {
        this.state = state;
        this.active = active;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }
    public StatesModel() {
    }

    public StatesModel(String state) {
        this.state = state;
    }
    public StatesModel(String state, String stateCode) {
        this.state = state;
        this.stateCode = stateCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String district) {
        this.stateCode = district;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
}
