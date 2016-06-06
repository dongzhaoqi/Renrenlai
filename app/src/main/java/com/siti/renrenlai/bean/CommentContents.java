package com.siti.renrenlai.bean;

import com.siti.renrenlai.util.ConstantValue;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/5.
 */
public class CommentContents implements Serializable{
    private int commentId;
    private String userName;
    private String commentContent;
    private String commentTime;
    private String userHeadPicImagePath;

    public CommentContents() {
    }

    public CommentContents(String userName, String commentContent) {
        this.userName = userName;
        this.commentContent = commentContent;
    }
    public CommentContents(String userName, String commentContent, String userHeadImagePath, String commentTime) {
        this.userName = userName;
        this.commentContent = commentContent;
        this.userHeadPicImagePath = userHeadImagePath;
        this.commentTime = commentTime;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadPicImagePath() {
        return ConstantValue.urlRoot + userHeadPicImagePath;
    }

    public void setUserHeadPicImagePath(String userHeadPicImagePath) {
        this.userHeadPicImagePath = userHeadPicImagePath;
    }
}
