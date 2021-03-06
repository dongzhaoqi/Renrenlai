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
    private String activityAddress;
    private String signPersonNum;
    private String lovedPersonNum;
    private List<ParticipateUser>participateUserList;
    private List<ActivityImage>activityImageList;
    private List<CommentContents>commentContents;

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

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
    }

    public String getLovedPersonNum() {
        return lovedPersonNum;
    }

    public void setLovedPersonNum(String lovedPersonNum) {
        this.lovedPersonNum = lovedPersonNum;
    }

    public List<ParticipateUser> getParticipateUserList() {
        return participateUserList;
    }

    public void setParticipateUserList(List<ParticipateUser> participateUserList) {
        this.participateUserList = participateUserList;
    }

    public List<ActivityImage> getActivityImageList() {
        return activityImageList;
    }

    public void setActivityImageList(List<ActivityImage> activityImageList) {
        this.activityImageList = activityImageList;
    }

    public String getSignPersonNum() {
        return signPersonNum;
    }

    public void setSignPersonNum(String signPersonNum) {
        this.signPersonNum = signPersonNum;
    }

    public List<CommentContents> getCommentContents() {
        return commentContents;
    }

    public void setCommentContents(List<CommentContents> commentContents) {
        this.commentContents = commentContents;
    }
}
