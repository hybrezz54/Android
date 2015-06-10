package org.technowolves.otpradar.framework;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamListItem implements Parcelable {

    public String number;
    public String name;
    public String website;

    public TeamListItem(String number, String name, String website) {
        this.number = number;
        this.name = name;
        this.website = website;
    }

    public TeamListItem(Parcel in) {
        number = in.readString();
        name = in.readString();
        website = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(name);
        dest.writeString(website);
    }

    public static final Creator<TeamListItem> CREATOR = new Parcelable.Creator<TeamListItem>() {

        @Override
        public TeamListItem createFromParcel(Parcel source) {
            return new TeamListItem(source);
        }

        @Override
        public TeamListItem[] newArray(int size) {
            return new TeamListItem[size];
        }
    };

}
