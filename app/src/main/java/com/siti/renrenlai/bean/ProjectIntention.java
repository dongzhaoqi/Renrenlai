package com.siti.renrenlai.bean;

import java.io.Serializable;

/**
 * Created by Dong on 2016/6/13.
 */
public class ProjectIntention implements Serializable{
    private int projectIntentionId;
    private String projectIntentionName;
    private String projectIntentionDescrip;
    private String projectIntentionPurpose;
    private String projectIntentionBenefitForWho;
    private String projectIntentionExecuteTime;

    public int getProjectIntentionId() {
        return projectIntentionId;
    }

    public void setProjectIntentionId(int projectIntentionId) {
        this.projectIntentionId = projectIntentionId;
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
