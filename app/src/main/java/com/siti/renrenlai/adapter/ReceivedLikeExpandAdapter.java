package com.siti.renrenlai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.ActivityInfo;
import com.siti.renrenlai.activity.ProjectInfo;
import com.siti.renrenlai.bean.ActivityImage;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.db.DbActivity;
import com.siti.renrenlai.db.DbActivityImage;
import com.siti.renrenlai.db.DbReceivedComment;
import com.siti.renrenlai.db.DbReceivedLike;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/5/20.
 */
public class ReceivedLikeExpandAdapter extends AnimatedExpandableListAdapter implements View.OnClickListener {

    private LayoutInflater inflater;
    private List<ReceivedLikeGroup> receivedLikeGroupList;
    private List<DbReceivedLike> receivedLikeList;
    private OnChildItemClickListener mOnChildItemClickListener;
    private Context mContext;
    private DbManager db;
    String userName, url;
    int count;
    private List<LovedUsers> lovedUsersList = new ArrayList<>();
    private List<CommentContents> commentsList = new ArrayList<>();
    private static final String TAG = "ReceivedLikeAdapter";

    public ReceivedLikeExpandAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(mContext, "login"), "userName");
    }

    public void setData(List<ReceivedLikeGroup> receivedLikeGroupList, List<DbReceivedLike> receivedLikeList) {
        this.receivedLikeGroupList = receivedLikeGroupList;
        this.receivedLikeList = receivedLikeList;
    }

    @Override
    public int getGroupCount() {
        return receivedLikeGroupList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return receivedLikeGroupList.get(groupPosition);
    }

    @Override
    public ReceivedLikeChild getChild(int groupPosition, int childPosition) {
        return receivedLikeGroupList.get(groupPosition).receivedLikeChildList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if(convertView == null){
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.group_received_like_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.str_like);
            holder.tv_message_nums = (TextView) convertView.findViewById(R.id.tv_message_nums);
            holder.iv_circle = (ImageView) convertView.findViewById(R.id.iv_circle);
            holder.expand_imgView = (ImageView) convertView.findViewById(R.id.iv_expand);
            convertView.setTag(holder);
        }else{
            holder = (GroupHolder) convertView.getTag();
        }

        if(receivedLikeList != null && receivedLikeList.size() > 0 ){
            count = 0;
            for(int i = 0; i < receivedLikeList.size(); i++){
                Log.d(TAG, "getGroupView: receivedLikeList.get(i).getHandleOrNot()------>" + receivedLikeList.get(i).getHandleOrNot());
                if(receivedLikeList.get(i).getHandleOrNot() == 0){
                    count ++ ;
                }
            }
            if(count > 0){
                holder.iv_circle.setVisibility(View.VISIBLE);
                holder.tv_message_nums.setText(String.valueOf(count));
            }
            holder.title.setText("收到的喜欢" + "(" + receivedLikeList.size() +")");
        }else{
            holder.title.setText(R.string.str_like);
        }


        if(!isExpanded){
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_expand_small_holo_light);
        }else{
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_collapse_small_holo_light);
        }

        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        final ReceivedLikeChild receivedLikeChild = getChild(groupPosition, childPosition);
        if(convertView == null){
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.child_received_like_item, parent, false);
            holder.ll_received_like = (LinearLayout) convertView.findViewById(R.id.ll_received_like);
            holder.iv_user_icon = (ImageView) convertView.findViewById(R.id.iv_user_icon);
            holder.tv_received_time = (TextView) convertView.findViewById(R.id.tv_received_time);
            holder.iv_activity_img = (ImageView) convertView.findViewById(R.id.iv_activity_img);
            holder.tv_activity_name = (TextView) convertView.findViewById(R.id.tv_activity_name);
            convertView.setTag(holder);
        }else{
            holder = (ChildHolder) convertView.getTag();
        }
        convertView.setOnClickListener(this);
        holder.iv_user_icon.setImageResource(R.drawable.system_alarm);
        holder.tv_received_time.setText(receivedLikeChild.received_time);
        holder.tv_activity_name.setText(receivedLikeChild.activity_name);
        Picasso.with(mContext).load(receivedLikeChild.activityImagePath).into(holder.iv_activity_img);

        if(receivedLikeChild.handleOrNot == 0){
            holder.ll_received_like.setBackgroundResource(R.color.background);
        }else{
            holder.ll_received_like.setBackgroundResource(R.color.white);
        }

        holder.ll_received_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(receivedLikeChild.handleOrNot == 0){
                    Log.d(TAG, "onClick: 执行了==============" );
                    changeStatus(userName, receivedLikeChild.adviceId, 2);
                }
                if(receivedLikeChild.activityId != 0) {
                    getActivityInfo(receivedLikeChild.activityId);
                }else{
                    getProjectInfo(receivedLikeChild.projectId);
                }
                Log.d(TAG, "onClick: " + receivedLikeChild.activityId);
            }
        });
        return convertView;
    }

    public void changeStatus(String userName, final long adviceId, int type){
        url = ConstantValue.HANDLE_MESSAGE_STATUS;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("adviceId", adviceId);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            db.update(DbReceivedLike.class, WhereBuilder.b("adviceId", "=", adviceId),new KeyValue("handleOrNot",1));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    public void getActivityInfo(final int activityId) {
        String url = ConstantValue.GET_ACTIVITY_INFO;

        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("activityId", activityId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getActivityNewData(response, activityId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    public void getActivityNewData(JSONObject response, int activityId) {
        Log.d(TAG, "getActivityNewData: " + response);
        JSONObject result = response.optJSONObject("result");
        commentsList = com.alibaba.fastjson.JSONArray.parseArray(result.optJSONArray("commentUserInfoList").toString(), CommentContents.class);
        lovedUsersList =  com.alibaba.fastjson.JSONArray.parseArray(result.optJSONArray("lovedUserList").toString(), LovedUsers.class);
        DbActivity dbActivity = null;
        List<DbActivityImage> dbActivityImage = null;
        List<ActivityImage> activityImages;
        Activity activity = new Activity();
        try {
            dbActivity = db.selector(DbActivity.class).where("activityId", "=", activityId).findFirst();
            dbActivityImage = db.selector(DbActivityImage.class).where("activityId", "=", activityId).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(dbActivity != null) {
            activity.setActivityId(dbActivity.getActivityId());
            activity.setActivityName(dbActivity.getActivityName());
            activity.setActivityStartTime(dbActivity.getActivityStartTime());
            activity.setActivityEndTime(dbActivity.getActivityEndTime());
            activity.setActivityReleaserTel(dbActivity.getActivityReleaserTel());
            activity.setActivityDetailDescrip(dbActivity.getActivityDetailDescrip());
            activity.setActivityAddress(dbActivity.getActivityAddress());
        }
        if(dbActivityImage != null && dbActivityImage.size() > 0){
            int dbActivityImageSize = dbActivityImage.size();
            activityImages = new ArrayList<>(dbActivityImageSize);
            for(int i = 0; i < dbActivityImageSize; i++){
                ActivityImage activityImage = new ActivityImage();
                activityImage.setActivityImagePath(dbActivityImage.get(i).getActivityImagePath());
                Log.d(TAG, "getActivityNewData: " + dbActivityImage.get(i).getActivityImagePath());
                activityImages.add(activityImage);
            }
            activity.setActivityImages(activityImages);
        }
        activity.setLovedIs(result.optBoolean("lovedIs"));
        activity.setSignUpIs(result.optBoolean("signUpIs"));
        activity.setCommentContents(commentsList);
        activity.setLovedUsers(lovedUsersList);
        Intent intent = new Intent(mContext, ActivityInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("activity", activity);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    public void getProjectInfo(int projectId) {
        String url = ConstantValue.GET_PROJECT_INFO;

        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("projectId", projectId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response:" + response.toString());
                        getProject(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    private void getProject(JSONObject response) {
        try {
            String result = response.getJSONObject("result").toString();
            Project project = com.alibaba.fastjson.JSONObject.parseObject(result, Project.class);
            Intent intent = new Intent(mContext, ProjectInfo.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("project", project);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return receivedLikeGroupList.get(groupPosition).receivedLikeChildList.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ReceivedLikeGroup {
        public String title;
        public List<ReceivedLikeChild> receivedLikeChildList = new ArrayList<ReceivedLikeChild>();
    }

    @Override
    public void onClick(View v) {
        if(mOnChildItemClickListener != null){
            mOnChildItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public static class ReceivedLikeChild {
        public int adviceId;
        public String received_time;
        public String activity_name;
        public int activityId;
        public int projectId;
        public String activityImagePath;
        public int handleOrNot;
    }

    public static class GroupHolder {
        TextView title;
        TextView tv_message_nums;
        ImageView iv_circle;
        ImageView expand_imgView;
    }

    public static class ChildHolder {
        LinearLayout ll_received_like;
        ImageView iv_user_icon;
        ImageView iv_activity_img;
        TextView tv_received_time;
        TextView tv_activity_name;
    }

    public static interface OnChildItemClickListener {
        void onItemClick(View view , Object data);
    }

    public void setOnChildItemClickListener(OnChildItemClickListener listener) {
        this.mOnChildItemClickListener = listener;
    }
}
