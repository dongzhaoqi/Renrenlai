package com.siti.renrenlai.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dong on 6/20/2016.
 */
@Table(name = "system_message")
public class DbSystemMessage {

    @Column(name = "msgId", isId = true, autoGen = false)
    private int msgId;

    @Column(name = "msgTitle")
    private String msgTitle;

    @Column(name = "msgContent")
    private String msgContent;

    @Column(name = "activityId")
    private int activityId;

    @Column(name = "time")
    private String time;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
