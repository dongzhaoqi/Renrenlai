package com.siti.renrenlai.bean;

import com.siti.renrenlai.util.ConstantValue;

import java.io.Serializable;

/**
 * Created by Dong on 6/15/2016.
 */
public class ActivityImagePre implements Serializable{
    private int activityImagePreId;
    private String activityImagePreName;
    private String activityImagePath;
    private int activityTypeId;
    public int getActivityImagePreId() {
        return activityImagePreId;
    }
    public void setActivityImagePreId(int activityImagePreId) {
        this.activityImagePreId = activityImagePreId;
    }
    public String getActivityImagePreName() {
        return activityImagePreName;
    }
    public void setActivityImagePreName(String activityImagePreName) {
        this.activityImagePreName = activityImagePreName;
    }
    public String getActivityImagePath() {
        if(activityImagePath.contains("http")){
            return activityImagePath;
        }
        return ConstantValue.urlRoot + activityImagePath;
    }
    public void setActivityImagePath(String activityImagePath) {
        this.activityImagePath = activityImagePath;
    }
    public int getActivityTypeId() {
        return activityTypeId;
    }
    public void setActivityTypeId(int activityTypeId) {
        this.activityTypeId = activityTypeId;
    }
}
