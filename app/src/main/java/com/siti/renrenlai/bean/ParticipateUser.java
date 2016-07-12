package com.siti.renrenlai.bean;

import com.siti.renrenlai.util.ConstantValue;

import java.io.Serializable;

/**
 * Created by Dong on 2016/7/12.
 */
public class ParticipateUser implements Serializable {
    private int userId;
    private String realName;
    private String telephone;
    private String userHeadPicImagePath;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserHeadPicImagePath() {
        if(userHeadPicImagePath.contains("http")){
            return userHeadPicImagePath;
        }
        return ConstantValue.urlRoot + userHeadPicImagePath;
    }

    public void setUserHeadPicImagePath(String userHeadPicImagePath) {
        this.userHeadPicImagePath = userHeadPicImagePath;
    }
}
