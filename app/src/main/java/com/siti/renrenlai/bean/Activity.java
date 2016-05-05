package com.siti.renrenlai.bean;

import java.io.Serializable;
import java.util.List;

public class Activity implements Serializable {

    private String activityImg;
    private String activityName;
    private String activityAddress;
    private String activityDescrip;
    private String activityStartTime;
    private String activityEndTime;
    private String contactTel;
    private List<LovedUsers> lovedUsers;
    private List<CommentContents> comments;

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

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
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

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
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