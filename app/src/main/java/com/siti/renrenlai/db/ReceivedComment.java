package com.siti.renrenlai.db;

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
        return userHeadImagePath;
    }

    public void setUserHeadImagePath(String userHeadImagePath) {
        this.userHeadImagePath = userHeadImagePath;
    }
}
