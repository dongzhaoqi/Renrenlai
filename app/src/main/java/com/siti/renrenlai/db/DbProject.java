package com.siti.renrenlai.db;

import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.bean.ProjectImage;
import com.siti.renrenlai.util.ConstantValue;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dong on 2016/5/26.
 */

@Table(name = "project")
public class DbProject implements Serializable{

    @Column(name = "autoProjectId", isId = true, autoGen = true)
    private int autoProjectId;

    @Column(name = "projectId")
    private int projectId;

    @Column(name = "projectName")
    private String projectName;

    @Column(name = "projectImagePath")
    private String projectImagePath;

    @Column(name = "projectDescrip")
    private String projectDescrip;

    @Column(name = "beginTimeOfProject")
    private String beginTimeOfProject;

    @Column(name = "endTimeOfProject")
    private String endTimeOfProject;

    @Column(name = "projectReleaseTime")
    private String projectReleaseTime;

    @Column(name = "projectAddress")
    private String projectAddress;

    @Column(name = "telephone")
    private String telephone;
    private List<LovedUsers> lovedImages;
    private List<CommentContents> commentList;
    private List<ProjectImage> projectImageList;
    private String userName1;
    private String comment1;
    private String userName2;
    private String comment2;
    private String commentCount;
    private String lovedCount;

    @Column(name = "lovedIs")
    private boolean lovedOrNot;

    public List<DbProjectImage> getProjectImages(DbManager db) throws DbException {
        return db.selector(DbProjectImage.class).where("projectId", "=", this.projectId).findAll();
    }

    public int getAutoProjectId() {
        return autoProjectId;
    }

    public void setAutoProjectId(int autoProjectId) {
        this.autoProjectId = autoProjectId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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



    public String getProjectDescrip() {
        return projectDescrip;
    }

    public void setProjectDescrip(String projectDescrip) {
        this.projectDescrip = projectDescrip;
    }

    public String getBeginTimeOfProject() {
        return beginTimeOfProject;
    }

    public void setBeginTimeOfProject(String beginTimeOfProject) {
        this.beginTimeOfProject = beginTimeOfProject;
    }

    public String getEndTimeOfProject() {
        return endTimeOfProject;
    }

    public void setEndTimeOfProject(String endTimeOfProject) {
        this.endTimeOfProject = endTimeOfProject;
    }

    public String getProjectReleaseTime() {
        return projectReleaseTime;
    }

    public void setProjectReleaseTime(String projectReleaseTime) {
        this.projectReleaseTime = projectReleaseTime;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<LovedUsers> getLovedImages() {
        return lovedImages;
    }

    public void setLovedImages(List<LovedUsers> lovedImages) {
        this.lovedImages = lovedImages;
    }

    public List<ProjectImage> getProjectImageList() {
        return projectImageList;
    }

    public void setProjectImageList(List<ProjectImage> projectImageList) {
        this.projectImageList = projectImageList;
    }

    public List<CommentContents> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentContents> commentList) {
        this.commentList = commentList;
    }

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getLovedCount() {
        return lovedCount;
    }

    public void setLovedCount(String lovedCount) {
        this.lovedCount = lovedCount;
    }

    public boolean isLovedOrNot() {
        return lovedOrNot;
    }

    public void setLovedOrNot(boolean lovedOrNot) {
        this.lovedOrNot = lovedOrNot;
    }
}
