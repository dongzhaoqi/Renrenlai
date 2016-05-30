package com.siti.renrenlai.bean;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/30.
 */
public class ProjectImage implements Serializable{

    private int projectImageId;
    private String projectImagePath;
    private String projectImageName;

    public int getProjectImageId() {
        return projectImageId;
    }

    public void setProjectImageId(int projectImageId) {
        this.projectImageId = projectImageId;
    }

    public String getProjectImagePath() {
        return projectImagePath;
    }

    public void setProjectImagePath(String projectImagePath) {
        this.projectImagePath = projectImagePath;
    }

    public String getProjectImageName() {
        return projectImageName;
    }

    public void setProjectImageName(String projectImageName) {
        this.projectImageName = projectImageName;
    }
}
