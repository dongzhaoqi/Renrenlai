package com.siti.renrenlai.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.activity.MessageActivity;
import com.siti.renrenlai.db.DbReceivedComment;
import com.siti.renrenlai.db.DbReceivedLike;
import com.siti.renrenlai.db.DbSystemMessage;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Dong on 2016/5/25.
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private DbManager db;
    private List<DbSystemMessage> systemMessageList;
    private List<DbReceivedComment> receivedCommentList;
    private List<DbReceivedLike> receivedLikeList;
    int type;  //消息类型
    JSONObject extrasJsonObject, jsonObject;
    String userHeadImagePath, userName, commentContent, time, title, content, extras;
    String url1, url2, url3;
    int projectId, activityId;
    int handleOrNot;        //消息是否已读
    int adviceId;           //消息Id
    int activOrProId;       //活动或项目Id

    @Override
    public void onReceive(Context context, Intent intent) {

        db = x.getDb(CustomApplication.getInstance().getDaoConfig());
        userName = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        context, "login"), "userName");
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
                extrasJsonObject = new JSONObject(extras);
                type = extrasJsonObject.getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "onReceive: 接收到推送下来的通知标题:" + title);
            Log.d(TAG, "onReceive: 接收到推送下来的通知内容:" + content);
            Log.d(TAG, "onReceive: 接收到推送下来的extras:" + extras);

            jsonObject = new JSONObject();
            try {
                jsonObject.put("userName", userName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //收到的系统消息---用户报名了活动
            saveSystemMessage();

            //收到的评论
            saveReviewMessage();

            //收到的喜欢
            saveActivityLikeMessage();

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

    public void saveSystemMessage() {
        url1 = ConstantValue.urlRoot + ConstantValue.GET_SYSTEM_MESSAGE;
        Log.d(TAG, "initMessage2: " + url1 + " userName " + userName);

        JsonObjectRequest request1 = new JsonObjectRequest(url1, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse   saveSystemMessage: " + response);
                        JSONArray result = response.optJSONArray("result");
                        if(result != null && result.length() > 0){
                            systemMessageList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbSystemMessage.class);
                            try {
                                db.delete(DbSystemMessage.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            for(DbSystemMessage systemMessage : systemMessageList){
                                try {
                                    db.save(systemMessage);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request1.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request1);      //加入请求队列

        /*if (type == ConstantValue.Activity_SYSTEM_MESSAGE) {
            adviceId = jsonObject.optInt("adviceId");
            userHeadImagePath = jsonObject.optString("userHeadImagePath");
            activityId = jsonObject.optInt("activityId");
            time = jsonObject.optString("time");
            type = jsonObject.optInt("type");
            handleOrNot = jsonObject.optInt("handleOrNot");

            DbSystemMessage systemMessage = new DbSystemMessage();
            systemMessage.setAdviceId(adviceId);
            systemMessage.setContent(content);
            systemMessage.setActivityId(activityId);
            systemMessage.setUserHeadImagePath(userHeadImagePath);
            systemMessage.setType(type);
            systemMessage.setHandleOrNot(handleOrNot);
            systemMessage.setTime(time);
            try {
                db.save(systemMessage);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void saveReviewMessage() {
        url2 = ConstantValue.urlRoot + ConstantValue.GET_COMMENT_MESSAGE;
        Log.d(TAG, "initMessage2: " + url2 + " userName " + userName);

        JsonObjectRequest request2 = new JsonObjectRequest(url2, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse   saveReviewMessage: " + response);
                        JSONArray result = response.optJSONArray("result");
                        if(result != null && result.length() > 0){
                            receivedCommentList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbReceivedComment.class);
                            try {
                                db.delete(DbReceivedComment.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            for(DbReceivedComment receivedComment : receivedCommentList){
                                try {
                                    db.save(receivedComment);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request2.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request2);      //加入请求队列

        /*if (type == ConstantValue.ACTIVITY_RECEIVED_COMMENT) {
            adviceId = jsonObject.optInt("adviceId");
            userHeadImagePath = jsonObject.optString("userHeadImagePath");
            userName = jsonObject.optString("userName");
            commentContent = jsonObject.optString("content");
            activityId = jsonObject.optInt("activityId");
            time = jsonObject.optString("time");
            type = jsonObject.optInt("type");
            handleOrNot = jsonObject.optInt("handleOrNot");

            DbReceivedComment receivedComment = new DbReceivedComment();
            receivedComment.setAdviceId(adviceId);
            receivedComment.setContent(commentContent);
            receivedComment.setUserName(userName);
            receivedComment.setUserHeadImagePath(userHeadImagePath);
            receivedComment.setActivityId(activityId);
            receivedComment.setTime(time);
            receivedComment.setType(type);
            receivedComment.setActivOrProId(activOrProId);
            receivedComment.setHandleOrNot(handleOrNot);

            try {
                db.save(receivedComment);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }*/
    }


    public void saveActivityLikeMessage() {
        url3 = ConstantValue.urlRoot + ConstantValue.GET_LIKE_MESSAGE;
        Log.d(TAG, "initMessage2: " + url3 + " userName " + userName);

        JsonObjectRequest request3 = new JsonObjectRequest(url3, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse  saveActivityLikeMessage: " + response);
                        JSONArray result = response.optJSONArray("result");
                        if(result != null && result.length() > 0){
                            receivedLikeList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbReceivedLike.class);
                            try {
                                db.delete(DbReceivedLike.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            for(DbReceivedLike receivedLike : receivedLikeList){
                                try {
                                    db.save(receivedLike);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request3.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request3);      //加入请求队列
        /*if (type == ConstantValue.ACTIVITY_RECEIVED_LIKE) {
            adviceId = jsonObject.optInt("adviceId");
            time = jsonObject.optString("time");
            activityId = jsonObject.optInt("activityId");
            type = jsonObject.optInt("type");
            handleOrNot = jsonObject.optInt("handleOrNot");

            DbReceivedLike receivedLike = new DbReceivedLike();
            receivedLike.setActivityId(activityId);
            receivedLike.setTime(time);
            try {
                db.save(receivedLike);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }*/
    }


    public void saveProjectComment() {
        if (type == ConstantValue.PROJECT_RECEIVED_COMMENT) {
            try {
                userHeadImagePath = extrasJsonObject.getString("userHeadImagePath");
                userName = extrasJsonObject.getString("userName");
                commentContent = extrasJsonObject.getString("content");
                projectId = extrasJsonObject.getInt("projectId");
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