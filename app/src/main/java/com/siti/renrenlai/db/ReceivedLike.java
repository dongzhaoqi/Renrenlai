package com.siti.renrenlai.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dong on 2016/6/24.
 */
@Table(name = "received_like")
public class ReceivedLike {

    @Column(name = "likeId", isId = true, autoGen = true)
    private int likeId;

    @Column(name = "userHeadImagePath")
    private String userHeadImagePath;


    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public String getUserHeadImagePath() {
        return userHeadImagePath;
    }

    public void setUserHeadImagePath(String userHeadImagePath) {
        this.userHeadImagePath = userHeadImagePath;
    }
}
