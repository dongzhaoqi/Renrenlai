package com.siti.renrenlai.bean;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/5.
 */
public class CommentContents implements Serializable{
    private String userName;
    private String commentContent;

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
