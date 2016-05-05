package com.siti.renrenlai.bean;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/5.
 */
public class LovedUsers implements Serializable{
    private String userId;
    private String userHeadPicImagePath;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHeadPicImagePath() {
        return userHeadPicImagePath;
    }

    public void setUserHeadPicImagePath(String userHeadPicImagePath) {
        this.userHeadPicImagePath = userHeadPicImagePath;
    }
}
