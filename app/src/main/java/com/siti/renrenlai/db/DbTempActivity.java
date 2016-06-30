package com.siti.renrenlai.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dong on 2016/6/29.
 */
@Table(name = "temp_activity")
public class DbTempActivity {

    @Column(name = "tempId", isId = true, autoGen = true)
    private int tempId;

    @Column(name = "activityType")
    private int activityType;

    @Column(name = "activityName")
    private String activityName;

    @Column(name = "activityStartTime")
    private String activityStartTime;

    @Column(name = "activityEndTime")
    private String activityEndTime;

    @Column(name = "activityDeadLineTime")
    private String activityDeadLineTime;

    @Column(name = "activityAddress")
    private String activityAddress;

    @Column(name = "activityPeopleNums")
    private String activityPeopleNums;

    @Column(name = "projectId")
    private int projectId;

    @Column(name = "projectName")
    private String projectName;

    @Column(name = "activityDetail")
    private String activityDetail;

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public String getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(String activityDetail) {
        this.activityDetail = activityDetail;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
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

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getActivityDeadLineTime() {
        return activityDeadLineTime;
    }

    public void setActivityDeadLineTime(String activityDeadLineTime) {
        this.activityDeadLineTime = activityDeadLineTime;
    }

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
    }

    public String getActivityPeopleNums() {
        return activityPeopleNums;
    }

    public void setActivityPeopleNums(String activityPeopleNums) {
        this.activityPeopleNums = activityPeopleNums;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
