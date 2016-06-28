package com.siti.renrenlai.db;

import com.siti.renrenlai.util.ConstantValue;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/30.
 */

@Table(name = "project_image")
public class DbProjectImage implements Serializable{

    @Column(name = "projectImageId", isId = true, autoGen = true)
    private int projectImageId;

    @Column(name = "projectImagePath")
    private String projectImagePath;

    @Column(name = "projectImageName")
    private String projectImageName;

    @Column(name = "projectId")
    private int projectId;

    public int getProjectImageId() {
        return projectImageId;
    }

    public void setProjectImageId(int projectImageId) {
        this.projectImageId = projectImageId;
    }

    public String getProjectImagePath() {
        if(projectImagePath != null && projectImagePath.contains("http")){
            return projectImagePath;
        }
        return ConstantValue.urlRoot + projectImagePath;
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
