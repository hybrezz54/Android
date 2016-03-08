package org.technowolves.otpradar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("key")
    @Expose
    String key;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("location")
    @Expose
    String location;

    public Event(String key, String name, String location) {
        this.key = key;
        this.name = name;
        this.location = location;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
