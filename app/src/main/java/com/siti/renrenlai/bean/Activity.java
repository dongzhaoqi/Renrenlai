package com.siti.renrenlai.bean;

import java.io.Serializable;

public class Activity implements Serializable {

    private String img;
    private String tv;

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getTv() {
        return tv;
    }
    public void setTv(String tv) {
        this.tv = tv;
    }

}