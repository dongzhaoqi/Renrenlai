package com.siti.renrenlai.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dong on 2016/6/30.
 */

@Table(name = "project_intention")
public class DbProjectIntention {

    @Column(name = "tempId", isId = true, autoGen = true)
    private int tempId;

    @Column(name = "userName")
    private String userName;

    @Column(name = "projectIntentionName")
    private String projectIntentionName;                //怎么称呼

    @Column(name = "projectIntentionDescrip")           //描述点子
    private String projectIntentionDescrip;

    @Column(name = "projectIntentionPurpose")           //目标
    private String projectIntentionPurpose;

    @Column(name = "projectIntentionBenefitForWho")     //为谁带来收益
    private String projectIntentionBenefitForWho;

    @Column(name = "projectIntentionExecuteTime")       //什么时候做
    private String projectIntentionExecuteTime;

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectIntentionName() {
        return projectIntentionName;
    }

    public void setProjectIntentionName(String projectIntentionName) {
        this.projectIntentionName = projectIntentionName;
    }

    public String getProjectIntentionDescrip() {
        return projectIntentionDescrip;
    }

    public void setProjectIntentionDescrip(String projectIntentionDescrip) {
        this.projectIntentionDescrip = projectIntentionDescrip;
    }

    public String getProjectIntentionPurpose() {
        return projectIntentionPurpose;
    }

    public void setProjectIntentionPurpose(String projectIntentionPurpose) {
        this.projectIntentionPurpose = projectIntentionPurpose;
    }

    public String getProjectIntentionBenefitForWho() {
        return projectIntentionBenefitForWho;
    }

    public void setProjectIntentionBenefitForWho(String projectIntentionBenefitForWho) {
        this.projectIntentionBenefitForWho = projectIntentionBenefitForWho;
    }

    public String getProjectIntentionExecuteTime() {
        return projectIntentionExecuteTime;
    }

    public void setProjectIntentionExecuteTime(String projectIntentionExecuteTime) {
        this.projectIntentionExecuteTime = projectIntentionExecuteTime;
    }
}
