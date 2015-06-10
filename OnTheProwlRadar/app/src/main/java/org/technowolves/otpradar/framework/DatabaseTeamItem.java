package org.technowolves.otpradar.framework;

public class DatabaseTeamItem {

    private long id;
    public String number;
    public String name;
    public String website;

    public DatabaseTeamItem(String number, String name, String website) {
        this.number = number;
        this.name = name;
        this.website = website;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String toString() {
        return number;
    }

}
