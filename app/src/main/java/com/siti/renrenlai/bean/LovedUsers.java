package com.siti.renrenlai.bean;

import com.siti.renrenlai.util.ConstantValue;

import java.io.Serializable;

/**
 * Created by Dong on 2016/5/5.
 */
public class LovedUsers implements Serializable{
    private String userId;
    private String userName;
    private String userHeadPicImagePath;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadPicImagePath() {
        return ConstantValue.urlRoot +  userHeadPicImagePath;
    }

    public void setUserHeadPicImagePath(String userHeadPicImagePath) {
        this.userHeadPicImagePath = userHeadPicImagePath;
    }
}
