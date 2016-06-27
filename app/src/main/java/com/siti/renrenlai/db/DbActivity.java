package com.siti.renrenlai.db;

import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dong on 2016/6/23.
 */

@Table(name = "activity")
public class DbActivity implements Serializable{
    @Column(name = "activityId", isId = true, autoGen = false)
    private int activityId;

    @Column(name = "activityName")
    private String activityName;

    @Column(name = "activityType")
    private String activityType;                    //活动类型

    @Column(name = "activityAddress")
    private String activityAddress;                 //活动地点

    @Column(name = "participateNum")
    private String participateNum;                     //活动人数

    @Column(name = "activityDetailDescrip")
    private String activityDetailDescrip;                 //活动详情

    @Column(name = "activityStartTime")
    private String activityStartTime;               //开始时间

    @Column(name = "activityEndTime")
    private String activityEndTime;                 //结束时间

    @Column(name = "deadline")
    private String deadline;                        //报名截止时间

    @Column(name = "activityReleaserTel")
    private String activityReleaserTel;

    @Column(name = "activityReleaseTime")
    private String activityReleaseTime;

    @Column(name = "activityStatus")
    private String activityStatus;

    @Column(name = "lovedIs")
    private boolean lovedIs;                        //用户是否已喜欢

    @Column(name = "signUpIs")
    private boolean signUpIs;                       //用户是否已报名


    public List<DbActivityImage> getActivityImages(DbManager db) throws DbException{
        return db.selector(DbActivityImage.class).where("activityId", "=", this.activityId).findAll();
    }


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


}
