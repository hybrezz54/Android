package org.technowolves.otpradar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Robot2016 {

    @SerializedName("team_number")
    @Expose
    String number;

    @SerializedName("robot_name")
    @Expose
    String name;

    @SerializedName("weight")
    @Expose
    String weight;

    @SerializedName("height")
    @Expose
    String height;

    @SerializedName("style")
    @Expose
    int style;

    @SerializedName("drive_train")
    @Expose
    int driveTrain;

    @SerializedName("wheel_type")
    @Expose
    int wheels;

    @SerializedName("shoot_ball")
    @Expose
    boolean shootBall;

    @SerializedName("track_target")
    @Expose
    boolean visionTrack;

    @SerializedName("portcullis_auto")
    @Expose
    boolean portcullisAuto;

    @SerializedName("drawbridge_auto")
    @Expose
    boolean drawbridgeAuto;

    @SerializedName("cheval_de_frise_auto")
    @Expose
    boolean chevalDeFriseAuto;

    @SerializedName("moat_auto")
    @Expose
    boolean moatAuto;

    @SerializedName("ramparts_auto")
    @Expose
    boolean rampartsAuto;

    @SerializedName("sallyport_auto")
    @Expose
    boolean sallyportAuto;

    @SerializedName("rock_wall_auto")
    @Expose
    boolean rockWallAuto;

    @SerializedName("rough_terrain_auto")
    @Expose
    boolean roughTerrainAuto;

    @SerializedName("low_bar_auto")
    @Expose
    boolean lowBarAuto;

    @SerializedName("portcullis")
    @Expose
    boolean portcullis;

    @SerializedName("drawbridge")
    @Expose
    boolean drawbridge;

    @SerializedName("cheval_de_frise")
    @Expose
    boolean chevalDeFrise;

    @SerializedName("moat")
    @Expose
    boolean moat;

    @SerializedName("ramparts")
    @Expose
    boolean ramparts;

    @SerializedName("sallyport")
    @Expose
    boolean sallyport;

    @SerializedName("rock_wall")
    @Expose
    boolean rockWall;

    @SerializedName("rough_terrain")
    @Expose
    boolean roughTerrain;

    @SerializedName("low_bar")
    @Expose
    boolean lowBar;

    public Robot2016(String number) {
        this.number = number;

        name = "";
        weight = "";
        height = "";
        style = 0;
        driveTrain = 0;
        wheels = 0;
        shootBall = false;
        visionTrack = false;
        portcullisAuto = false;
        drawbridgeAuto = false;
        chevalDeFriseAuto = false;
        moatAuto = false;
        rampartsAuto = false;
        sallyportAuto = false;
        rockWallAuto = false;
        roughTerrainAuto = false;
        lowBarAuto = false;
        portcullis = false;
        drawbridge = false;
        chevalDeFrise = false;
        moat = false;
        ramparts = false;
        sallyport = false;
        rockWall = false;
        roughTerrain = false;
        lowBar = false;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getDriveTrain() {
        return driveTrain;
    }

    public void setDriveTrain(int driveTrain) {
        this.driveTrain = driveTrain;
    }

    public int getWheels() {
        return wheels;
    }

    public void setWheels(int wheels) {
        this.wheels = wheels;
    }

    public boolean isShootBall() {
        return shootBall;
    }

    public void setShootBall(boolean shootBall) {
        this.shootBall = shootBall;
    }

    public boolean isVisionTrack() {
        return visionTrack;
    }

    public void setVisionTrack(boolean visionTrack) {
        this.visionTrack = visionTrack;
    }

    public boolean isPortcullisAuto() {
        return portcullisAuto;
    }

    public void setPortcullisAuto(boolean portcullisAuto) {
        this.portcullisAuto = portcullisAuto;
    }

    public boolean isDrawbridgeAuto() {
        return drawbridgeAuto;
    }

    public void setDrawbridgeAuto(boolean drawbridgeAuto) {
        this.drawbridgeAuto = drawbridgeAuto;
    }

    public boolean isChevalDeFriseAuto() {
        return chevalDeFriseAuto;
    }

    public void setChevalDeFriseAuto(boolean chevalDeFriseAuto) {
        this.chevalDeFriseAuto = chevalDeFriseAuto;
    }

    public boolean isMoatAuto() {
        return moatAuto;
    }

    public void setMoatAuto(boolean moatAuto) {
        this.moatAuto = moatAuto;
    }

    public boolean isRampartsAuto() {
        return rampartsAuto;
    }

    public void setRampartsAuto(boolean rampartsAuto) {
        this.rampartsAuto = rampartsAuto;
    }

    public boolean isSallyportAuto() {
        return sallyportAuto;
    }

    public void setSallyportAuto(boolean sallyportAuto) {
        this.sallyportAuto = sallyportAuto;
    }

    public boolean isRockWallAuto() {
        return rockWallAuto;
    }

    public void setRockWallAuto(boolean rockWallAuto) {
        this.rockWallAuto = rockWallAuto;
    }

    public boolean isRoughTerrainAuto() {
        return roughTerrainAuto;
    }

    public void setRoughTerrainAuto(boolean roughTerrainAuto) {
        this.roughTerrainAuto = roughTerrainAuto;
    }

    public boolean isLowBarAuto() {
        return lowBarAuto;
    }

    public void setLowBarAuto(boolean lowBarAuto) {
        this.lowBarAuto = lowBarAuto;
    }

    public boolean isPortcullis() {
        return portcullis;
    }

    public void setPortcullis(boolean portcullis) {
        this.portcullis = portcullis;
    }

    public boolean isDrawbridge() {
        return drawbridge;
    }

    public void setDrawbridge(boolean drawbridge) {
        this.drawbridge = drawbridge;
    }

    public boolean isChevalDeFrise() {
        return chevalDeFrise;
    }

    public void setChevalDeFrise(boolean chevalDeFrise) {
        this.chevalDeFrise = chevalDeFrise;
    }

    public boolean isMoat() {
        return moat;
    }

    public void setMoat(boolean moat) {
        this.moat = moat;
    }

    public boolean isRamparts() {
        return ramparts;
    }

    public void setRamparts(boolean ramparts) {
        this.ramparts = ramparts;
    }

    public boolean isSallyport() {
        return sallyport;
    }

    public void setSallyport(boolean sallyport) {
        this.sallyport = sallyport;
    }

    public boolean isRockWall() {
        return rockWall;
    }

    public void setRockWall(boolean rockWall) {
        this.rockWall = rockWall;
    }

    public boolean isRoughTerrain() {
        return roughTerrain;
    }

    public void setRoughTerrain(boolean roughTerrain) {
        this.roughTerrain = roughTerrain;
    }

    public boolean isLowBar() {
        return lowBar;
    }

    public void setLowBar(boolean lowBar) {
        this.lowBar = lowBar;
    }

}
