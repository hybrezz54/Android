package technowolves.org.otpradar;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable {

    public String number;
    public String team;

    public Team(String number, String team) {
        this.number = number;
        this.team = team;
    }

    public Team(Parcel in) {
        number = in.readString();
        team = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(team);
    }

    public static final Creator<Team> CREATOR = new Parcelable.Creator<Team>() {

        @Override
        public Team createFromParcel(Parcel source) {
            return new Team(source);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

}
