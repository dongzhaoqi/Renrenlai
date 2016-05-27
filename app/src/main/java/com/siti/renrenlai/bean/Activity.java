package com.siti.renrenlai.bean;

import java.io.Serializable;
import java.util.List;

public class Activity implements Serializable {

    private int activityId;
    private String activityName;
    private String activityType;                    //活动类型
    private String activityAddress;                 //活动地点
    private String participateNum;                     //活动人数
    private String activityDetailDescrip;                 //活动详情
    private String activityStartTime;               //开始时间
    private String activityEndTime;                 //结束时间
    private String deadline;                        //报名截止时间
    private String activityReleaserTel;
    private String activityReleaseTime;
    private String activityStatus;
    private List<ActivityImage> activityImages;
    private List<LovedUsers> lovedUsers;
    private List<CommentContents> commentContents;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityDetailDescrip() {
        return activityDetailDescrip;
    }

    public void setActivityDetailDescrip(String activityDetailDescrip) {
        this.activityDetailDescrip = activityDetailDescrip;
    }

    public String getActivityReleaserTel() {
        return activityReleaserTel;
    }

    public void setActivityReleaserTel(String activityReleaserTel) {
        this.activityReleaserTel = activityReleaserTel;
    }

    public List<ActivityImage> getActivityImages() {
        return activityImages;
    }

    public void setActivityImages(List<ActivityImage> activityImages) {
        this.activityImages = activityImages;
    }

    public List<CommentContents> getCommentContents() {
        return commentContents;
    }

    public void setCommentContents(List<CommentContents> commentContents) {
        this.commentContents = commentContents;
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

    public String getactivityDetailDescrip() {
        return activityDetailDescrip;
    }

    public void setactivityDetailDescrip(String activityDetailDescrip) {
        this.activityDetailDescrip = activityDetailDescrip;
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

    public String getactivityReleaserTel() {
        return activityReleaserTel;
    }

    public void setactivityReleaserTel(String activityReleaserTel) {
        this.activityReleaserTel = activityReleaserTel;
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
        return commentContents;
    }

    public void setComments(List<CommentContents> comments) {
        this.commentContents = comments;
    }
}