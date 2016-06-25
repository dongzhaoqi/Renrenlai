package com.siti.renrenlai.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.db.Activity;
import com.siti.renrenlai.db.ReceivedLike;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
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
    private List<ReceivedLike> receivedLikeList;
    private OnChildItemClickListener mOnChildItemClickListener;
    private Context mContext;
    private DbManager db;
    String userName;
    private List<LovedUsers> lovedUsersList = new ArrayList<>();
    private List<CommentContents> commentsList = new ArrayList<>();
    private static final String TAG = "ReceivedLikeAdapter";

    public ReceivedLikeExpandAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(mContext, "login"), "userName");
    }

    public void setData(List<ReceivedLikeGroup> receivedLikeGroupList, List<ReceivedLike> receivedLikeList) {
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
            holder.iv_circle.setVisibility(View.VISIBLE);
            holder.tv_message_nums.setText(String.valueOf(receivedLikeList.size()));
        }

        if(!isExpanded){
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_expand_small_holo_light);
        }else{
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_collapse_small_holo_light);
        }

        holder.title.setText(R.string.str_like);
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

        holder.ll_received_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityInfo(receivedLikeChild.activityId);
                Log.d(TAG, "onClick: " + receivedLikeChild.activityId);
            }
        });
        return convertView;
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
        Activity activity = null;
        try {
            activity = db.selector(Activity.class).where("activityId", "=", activityId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
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
        public String received_time;
        public String activity_name;
        public int activityId;
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
