package com.example.covidapp;

public class CountryModel {
    private  String flag, countries, cases, critical, recovered, todayCases, todayDeaths, deaths, active;

    public CountryModel() {
    }

    public CountryModel(String flag, String countries, String cases, String critical, String recovered, String todayCases, String todayDeaths, String deaths, String active) {
        this.flag = flag;
        this.countries = countries;
        this.cases = cases;
        this.critical = critical;
        this.recovered = recovered;
        this.todayCases = todayCases;
        this.todayDeaths = todayDeaths;
        this.deaths = deaths;
        this.active = active;


    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getCritical() {
        return critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(String todayDeaths) {
        this.todayDeaths = todayDeaths;
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
}
