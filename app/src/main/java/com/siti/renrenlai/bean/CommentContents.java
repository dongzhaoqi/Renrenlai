package com.siti.renrenlai.bean;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/5.
 */
public class CommentContents implements Serializable{
    private String userName;
    private String commentContent;
    private String commentTime;

    public CommentContents() {
    }

    public CommentContents(String userName, String commentContent) {
        this.userName = userName;
        this.commentContent = commentContent;
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
}
