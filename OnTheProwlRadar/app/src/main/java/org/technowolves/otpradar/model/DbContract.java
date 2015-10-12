package org.technowolves.otpradar.model;

public class DbContract {

    public static final String DATABASE_NAME = "otpradar";
    public static final int DATABASE_VERSION = 2;

    public static final String COLUMN_ID = "_id";

    public static final String TEAM_LIST_TABLE = "team_list";
    public static final String COLUMN_NUMBER = "team_number";
    public static final String COLUMN_NAME = "team_name";
    public static final String COLUMN_SITE = "team_site";

    public static final String TEAM_INFO_TABLE = "team_info";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TOTALYRS = "total_yrs";
    public static final String COLUMN_PARTICIPATE = "participate";
    public static final String COLUMN_AWARD1 = "award_one";
    public static final String COLUMN_YEAR1 = "year_one";
    public static final String COLUMN_AWARD2 = "award_two";
    public static final String COLUMN_YEAR2 = "year_two";
    public static final String COLUMN_AWARD3 = "award_three";
    public static final String COLUMN_YEAR3 = "year_three";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_COACHMENTOR = "coach_mentor";
    public static final String COLUMN_DRIVER = "driver_rate";
    public static final String COLUMN_HP = "hp_rate";

    public static final String ROBOT_TABLE = "robot_info";
    public static final String COLUMN_SEASON = "frc_season";
    public static final String COLUMN_ROBOT = "robot_name";
    public static final String COLUMN_STYLE = "robot_style";
    public static final String COLUMN_HEIGHT = "robot_ht";
    public static final String COLUMN_WEIGHT = "robot_wt";
    public static final String COLUMN_DRIVE = "drive_train";
    public static final String COLUMN_WHEEL = "wheel_type";
    public static final String COLUMN_STRAFE = "robot_strafe";
    public static final String COLUMN_MOVE1 = "move_tote";
    public static final String COLUMN_MOVE2 = "move_cont";
    public static final String COLUMN_ACQ1 = "acq_tote";
    public static final String COLUMN_ACQ2 = "acq_cont";
    public static final String COLUMN_POS = "start_pos";
    public static final String COLUMN_TOTE = "max_tote";

}
