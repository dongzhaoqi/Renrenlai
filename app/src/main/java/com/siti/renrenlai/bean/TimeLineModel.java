package com.siti.renrenlai.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineModel implements Serializable{
    private int activityId;
    private int activityStatus;
    private String activityName;
    private String dateTimeForActiv;
    private String activityStartTime;
    private String activityEndTime;
    private String lovedPersonNum;
    private List<ActivityImage>activityImageList;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public String getDateTimeForActiv() {
        return dateTimeForActiv;
    }

    public void setDateTimeForActiv(String dateTimeForActiv) {
        this.dateTimeForActiv = dateTimeForActiv;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getLovedPersonNum() {
        return lovedPersonNum;
    }

    public void setLovedPersonNum(String lovedPersonNum) {
        this.lovedPersonNum = lovedPersonNum;
    }

    public List<ActivityImage> getActivityImageList() {
        return activityImageList;
    }

    public void setActivityImageList(List<ActivityImage> activityImageList) {
        this.activityImageList = activityImageList;
    }
}
