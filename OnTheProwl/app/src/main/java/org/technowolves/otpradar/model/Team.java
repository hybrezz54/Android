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

}
