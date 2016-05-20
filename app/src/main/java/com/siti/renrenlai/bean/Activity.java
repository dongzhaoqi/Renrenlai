package com.siti.renrenlai.bean;

import java.io.Serializable;
import java.util.List;

public class Activity implements Serializable {

    private int activityId;
    private String activityImg;
    private String activityName;
    private String activityType;                    //活动类型
    private String activityAddress;                 //活动地点
    private String participateNum;                     //活动人数
    private String activityDescrip;                 //活动详情
    private String activityStartTime;               //开始时间
    private String activityEndTime;                 //结束时间
    private String deadline;                        //报名截止时间
    private String contactTel;
    private String activityReleaseTime;
    private String activityStatus;
    private List<LovedUsers> lovedUsers;
    private List<CommentContents> comments;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
    }

    public String getParticipateNum() {
        return participateNum;
    }

    public void setParticipateNum(String participateNum) {
        this.participateNum = participateNum;
    }

    public String getActivityDescrip() {
        return activityDescrip;
    }

    public void setActivityDescrip(String activityDescrip) {
        this.activityDescrip = activityDescrip;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getActivityReleaseTime() {
        return activityReleaseTime;
    }

    public void setActivityReleaseTime(String activityReleaseTime) {
        this.activityReleaseTime = activityReleaseTime;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public List<LovedUsers> getLovedUsers() {
        return lovedUsers;
    }

    public void setLovedUsers(List<LovedUsers> lovedUsers) {
        this.lovedUsers = lovedUsers;
    }

    public List<CommentContents> getComments() {
        return comments;
    }

    public void setComments(List<CommentContents> comments) {
        this.comments = comments;
    }
}