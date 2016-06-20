package com.siti.renrenlai.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dong on 6/20/2016.
 */
@Table(name = "message")
public class Message {

    @Column(name = "msgId", isId = true, autoGen = false)
    private int msgId;

    @Column(name = "msgTitle")
    private String msgTitle;

    @Column(name = "msgContent")
    private String msgContent;

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
}
