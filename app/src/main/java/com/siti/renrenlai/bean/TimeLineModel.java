package com.siti.renrenlai.bean;

import java.io.Serializable;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineModel implements Serializable{
    private String time;
    private String img;
    private String title;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
