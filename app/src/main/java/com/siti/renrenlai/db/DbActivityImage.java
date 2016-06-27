package com.siti.renrenlai.db;

import com.siti.renrenlai.util.ConstantValue;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/27.
 */
@Table(name = "activity_image")
public class DbActivityImage implements Serializable {

    @Column(name = "activityImageId", isId = true, autoGen = true)
    private int activityImageId;

    @Column(name = "activityImageName")
    private String activityImageName;

    @Column(name = "activityImagePath")
    private String activityImagePath;

    @Column(name = "activityId")
    private int activityId;

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
        if(activityImagePath.contains("http")){
            return activityImagePath;
        }
        return ConstantValue.urlRoot + activityImagePath;
    }

    public void setActivityImagePath(String activityImagePath) {
        this.activityImagePath = activityImagePath;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
}
