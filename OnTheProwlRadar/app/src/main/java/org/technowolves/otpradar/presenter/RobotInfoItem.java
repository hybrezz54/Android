package org.technowolves.otpradar.presenter;

public class RobotInfoItem {

    private long id;
    private int season;
    private int style;
    private int driveTrain;
    private int wheelType;
    private int strafe;
    private int moveTote;
    private int moveCont;
    private int acqTote;
    private int acqCont;
    private int startPos;
    private String name;
    private String height;
    private String weight;
    private String maxTote;


    public RobotInfoItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getDriveTrain() {
        return driveTrain;
    }

    public void setDriveTrain(int driveTrain) {
        this.driveTrain = driveTrain;
    }

    public int getWheelType() {
        return wheelType;
    }

    public void setWheelType(int wheelType) {
        this.wheelType = wheelType;
    }

    public int getStrafe() {
        return strafe;
    }

    public void setStrafe(int strafe) {
        this.strafe = strafe;
    }

    public int getMoveTote() {
        return moveTote;
    }

    public void setMoveTote(int moveTote) {
        this.moveTote = moveTote;
    }

    public int getMoveCont() {
        return moveCont;
    }

    public void setMoveCont(int moveCont) {
        this.moveCont = moveCont;
    }


    public int getAcqTote() {
        return acqTote;
    }

    public void setAcqTote(int acqTote) {
        this.acqTote = acqTote;
    }


    public int getAcqCont() {
        return acqCont;
    }

    public void setAcqCont(int acqCont) {
        this.acqCont = acqCont;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public String getMaxTote() {
        return maxTote;
    }

    public void setMaxTote(String maxTote) {
        this.maxTote = maxTote;
    }

}
