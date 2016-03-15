package org.technowolves.otpradar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team {

    @SerializedName("team_number")
    @Expose
    String number;

    @SerializedName("nickname")
    @Expose
    String name;

    @SerializedName("website")
    @Expose
    String website;

    @SerializedName("location")
    @Expose
    String location;

    @SerializedName("rookie_year")
    @Expose
    String rookie;

    @SerializedName("num_competition")
    @Expose
    String competition;

    @SerializedName("award_one")
    @Expose
    int award1;

    @SerializedName("year_one")
    @Expose
    String year1;

    @SerializedName("award_two")
    @Expose
    int award2;

    @SerializedName("year_two")
    @Expose
    String year2;

    @SerializedName("notes")
    @Expose
    String notes;

    @SerializedName("driver_xp")
    @Expose
    float driverExp;

    @SerializedName("hp_xp")
    @Expose
    float humanExp;

    public Team(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRookie() {
        return rookie;
    }

    public void setRookie(String rookie) {
        this.rookie = rookie;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public int getAward1() {
        return award1;
    }

    public void setAward1(int award1) {
        this.award1 = award1;
    }

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public int getAward2() {
        return award2;
    }

    public void setAward2(int award2) {
        this.award2 = award2;
    }

    public String getYear2() {
        return year2;
    }

    public void setYear2(String year2) {
        this.year2 = year2;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public float getDriverExp() {
        return driverExp;
    }

    public void setDriverExp(float driverExp) {
        this.driverExp = driverExp;
    }

    public float getHumanExp() {
        return humanExp;
    }

    public void setHumanExp(float humanExp) {
        this.humanExp = humanExp;
    }

}
