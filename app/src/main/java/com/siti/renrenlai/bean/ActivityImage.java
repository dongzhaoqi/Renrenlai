package com.siti.renrenlai.bean;

import com.siti.renrenlai.util.ConstantValue;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/27.
 */
public class ActivityImage implements Serializable {

    private int activityImageId;
    private String activityImageName;
    private String activityImagePath;

    public int getActivityImageId() {
        return activityImageId;
    }

    public void setActivityImageId(int activityImageId) {
        this.activityImageId = activityImageId;
    }

    public String getActivityImageName() {
        return activityImageName;
    }

    public void setActivityImageName(String activityImageName) {
        this.activityImageName = activityImageName;
    }

    public String getActivityImagePath() {
        return ConstantValue.urlRoot +  activityImagePath;
    }

    public void setActivityImagePath(String activityImagePath) {
        this.activityImagePath = activityImagePath;
    }
}
