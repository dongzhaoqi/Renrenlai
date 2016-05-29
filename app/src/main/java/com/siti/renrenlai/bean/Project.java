package com.siti.renrenlai.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dong on 2016/5/26.
 */
public class Project implements Serializable{
    private int projectId;
    private String projectName;
    private String projectImagePath;
    private String projectDescrip;
    private String beginTimeOfProject;
    private String endTimeOfProject;
    private String projectReleaseTime;
    private String projectAddress;
    private String telephone;
    private List<LovedUsers> lovedImages;
    private List<CommentContents> commentList;
    private String userName1;
    private String comment1;
    private String userName2;
    private String comment2;
    private String commentCount;
    private String lovedCount;
    private boolean lovedOrNot;

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
        return projectImagePath;
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
