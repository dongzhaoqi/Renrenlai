package com.siti.renrenlai.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.siti.renrenlai.activity.MessageActivity;
import com.siti.renrenlai.db.DbReceivedComment;
import com.siti.renrenlai.db.DbReceivedLike;
import com.siti.renrenlai.db.DbSystemMessage;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Dong on 2016/5/25.
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private DbManager db;
    int type;  //消息类型
    JSONObject jsonObject;
    String userHeadImagePath, userName, commentContent, time, title, content, extras;
    int projectId, activityId;

    @Override
    public void onReceive(Context context, Intent intent) {

        db = x.getDb(CustomApplication.getInstance().getDaoConfig());

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            content = bundle.getString(JPushInterface.EXTRA_ALERT);
            extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

            try {
                jsonObject = new JSONObject(extras);
                type = jsonObject.getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "onReceive: 接收到推送下来的通知标题:" + title);
            Log.d(TAG, "onReceive: 接收到推送下来的通知内容:" + content);
            Log.d(TAG, "onReceive: 接收到推送下来的extras:" + extras);


            //收到的系统消息---用户报名了活动
            //saveSystemMessage();

            //收到的活动评论
            //saveReviewMessage();

            //收到的活动喜欢
            //saveActivityLikeMessage();

            //收到的项目评论
            //saveProjectComment();

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            Intent i = new Intent(context, MessageActivity.class);
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    public void saveSystemMessage(){
        if (type == ConstantValue.Activity_SYSTEM_MESSAGE) {
            try {
                userHeadImagePath = jsonObject.optString("userHeadImagePath");
                activityId = jsonObject.getInt("activityId");
                time = jsonObject.getString("time");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DbSystemMessage systemMessage = new DbSystemMessage();
            systemMessage.setContent(content);
            systemMessage.setActivityId(activityId);
            systemMessage.setUserHeadImagePath(userHeadImagePath);
            systemMessage.setTime(time);
            try {
                db.save(systemMessage);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveReviewMessage(){
        if (type == ConstantValue.ACTIVITY_RECEIVED_COMMENT) {
            userHeadImagePath = jsonObject.optString("userHeadImagePath");
            userName = jsonObject.optString("userName");
            commentContent = jsonObject.optString("content");
            activityId = jsonObject.optInt("activityId");
            time = jsonObject.optString("time");

            DbReceivedComment receivedComment = new DbReceivedComment();
            receivedComment.setContent(commentContent);
            receivedComment.setUserName(userName);
            receivedComment.setUserHeadImagePath(userHeadImagePath);
            receivedComment.setActivityId(activityId);
            receivedComment.setTime(time);

            try {
                db.save(receivedComment);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }


    public void saveActivityLikeMessage(){
        if (type == ConstantValue.ACTIVITY_RECEIVED_LIKE) {
            try {
                time = jsonObject.getString("time");
                activityId = jsonObject.getInt("activityId");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DbReceivedLike receivedLike = new DbReceivedLike();
            receivedLike.setActivityId(activityId);
            receivedLike.setTime(time);
            try {
                db.save(receivedLike);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }


    public void saveProjectComment(){
        if (type == ConstantValue.PROJECT_RECEIVED_COMMENT) {
            try {
                userHeadImagePath = jsonObject.getString("userHeadImagePath");
                userName = jsonObject.getString("userName");
                commentContent = jsonObject.getString("content");
                projectId = jsonObject.getInt("projectId");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DbReceivedComment receivedComment = new DbReceivedComment();
            receivedComment.setContent(commentContent);
            receivedComment.setUserName(userName);
            receivedComment.setUserHeadImagePath(userHeadImagePath);
            receivedComment.setProjectId(projectId);

            try {
                db.save(receivedComment);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


}