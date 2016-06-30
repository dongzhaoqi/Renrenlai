package com.siti.renrenlai.bean;

import java.io.Serializable;
import java.util.List;

public class Activity implements Serializable {

    private int activityId;
    private String activityName;
    private int activityType;                    //活动类型
    private String activityAddress;                 //活动地点
    private String participateNum;                     //活动人数
    private String activityDetailDescrip;                 //活动详情
    private String activityStartTime;               //开始时间
    private String activityEndTime;                 //结束时间
    private String deadline;                        //报名截止时间
    private String activityReleaserTel;
    private String activityReleaseTime;
    private String projectName;
    private String activityStatus;
    private boolean lovedIs;                        //用户是否已喜欢
    private boolean signUpIs;                       //用户是否已报名
    private List<ActivityImage> activityImages;
    private List<LovedUsers> lovedUsers;
    private List<CommentContents> commentContents;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
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

    public String getActivityDetailDescrip() {
        return activityDetailDescrip;
    }

    public void setActivityDetailDescrip(String activityDetailDescrip) {
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

    public String getActivityReleaserTel() {
        return activityReleaserTel;
    }

    public void setActivityReleaserTel(String activityReleaserTel) {
        this.activityReleaserTel = activityReleaserTel;
    }

    public String getActivityReleaseTime() {
        return activityReleaseTime;
    }

    public void setActivityReleaseTime(String activityReleaseTime) {
        this.activityReleaseTime = activityReleaseTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public boolean isLovedIs() {
        return lovedIs;
    }

    public void setLovedIs(boolean lovedIs) {
        this.lovedIs = lovedIs;
    }

    public boolean isSignUpIs() {
        return signUpIs;
    }

    public void setSignUpIs(boolean signUpIs) {
        this.signUpIs = signUpIs;
    }

    public List<ActivityImage> getActivityImages() {
        return activityImages;
    }

    public void setActivityImages(List<ActivityImage> activityImages) {
        this.activityImages = activityImages;
    }

    public List<LovedUsers> getLovedUsers() {
        return lovedUsers;
    }

    public void setLovedUsers(List<LovedUsers> lovedUsers) {
        this.lovedUsers = lovedUsers;
    }

    public List<CommentContents> getCommentContents() {
        return commentContents;
    }

    public void setCommentContents(List<CommentContents> commentContents) {
        this.commentContents = commentContents;
    }
}