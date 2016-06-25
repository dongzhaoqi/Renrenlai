package com.siti.renrenlai.db;

import com.siti.renrenlai.util.ConstantValue;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dong on 2016/6/24.
 */

@Table(name = "received_comment")
public class ReceivedComment {

    @Column(name = "commentId", isId = true, autoGen = true)
    private int commentId;

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

    @Column(name = "commentTime")
    private String commentTime;

    @Column(name = "isRead")
    private boolean isRead;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserHeadImagePath() {
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

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
}
