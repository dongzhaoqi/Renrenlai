package com.siti.renrenlai.db;

import com.siti.renrenlai.util.ConstantValue;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dong on 2016/6/24.
 */

@Table(name = "received_comment")
public class DbReceivedComment {

    @Column(name = "adviceId", isId = true, autoGen = false)
    private int adviceId;

    @Column(name = "content")
    private String content;

    @Column(name = "userName")
    private String userName;

    @Column(name = "userHeadImagePath")
    private String userHeadImagePath;

    @Column(name = "projectId")
    private int projectId;

    @Column(name = "activityId")
    private int activityId;

    @Column(name = "time")
    private String time;

    @Column(name = "type")
    private int type;

    @Column(name = "handleOrNot")
    private int handleOrNot;

    @Column(name = "activOrProId")
    private int activOrProId;

    public int getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(int adviceId) {
        this.adviceId = adviceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadImagePath() {
        if(userHeadImagePath != null && userHeadImagePath.contains("http")){
            return userHeadImagePath;
        }
        return ConstantValue.urlRoot + userHeadImagePath;
    }

    public void setUserHeadImagePath(String userHeadImagePath) {
        this.userHeadImagePath = userHeadImagePath;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHandleOrNot() {
        return handleOrNot;
    }

    public void setHandleOrNot(int handleOrNot) {
        this.handleOrNot = handleOrNot;
    }

    public int getActivOrProId() {
        return activOrProId;
    }

    public void setActivOrProId(int activOrProId) {
        this.activOrProId = activOrProId;
    }
}
