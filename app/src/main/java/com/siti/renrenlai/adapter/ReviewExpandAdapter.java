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

import com.alibaba.fastjson.JSONArray;
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
import com.siti.renrenlai.db.DbSystemMessage;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dong on 2016/5/20.
 */
public class ReviewExpandAdapter extends AnimatedExpandableListAdapter implements View.OnClickListener {

    private LayoutInflater inflater;
    private List<ReviewGroup> receivedCommentGroupList;
    private List<DbReceivedComment> receivedCommentList;
    private OnChildItemClickListener mOnChildItemClickListener;
    private Context mContext;
    private DbManager db;
    String userName, url;
    private List<LovedUsers> lovedUsersList = new ArrayList<>();
    private List<CommentContents> commentsList = new ArrayList<>();
    int count;
    private static final String TAG = "ReviewExpandAdapter";

    public ReviewExpandAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(mContext, "login"), "userName");
    }

    public void setData(List<ReviewGroup> receivedCommentGroupList, List<DbReceivedComment> receivedCommentList) {
        this.receivedCommentGroupList = receivedCommentGroupList;
        this.receivedCommentList = receivedCommentList;
    }

    @Override
    public int getGroupCount() {
        return receivedCommentGroupList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return receivedCommentGroupList.get(groupPosition);
    }

    @Override
    public ReviewChild getChild(int groupPosition, int childPosition) {
        return receivedCommentGroupList.get(groupPosition).receivedCommentChildList.get(childPosition);
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
            convertView = inflater.inflate(R.layout.group_review_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.str_review);
            holder.tv_message_nums = (TextView) convertView.findViewById(R.id.tv_message_nums);
            holder.iv_circle = (ImageView) convertView.findViewById(R.id.iv_circle);
            holder.expand_imgView = (ImageView) convertView.findViewById(R.id.iv_expand);
            convertView.setTag(holder);
        }else{
            holder = (GroupHolder) convertView.getTag();
        }

        if(receivedCommentList != null && receivedCommentList.size() > 0 ){
            count = 0;
            for(int i = 0; i < receivedCommentList.size(); i++){
                Log.d(TAG, "getGroupView: receivedCommentList.get(i).getHandleOrNot()------>" + receivedCommentList.get(i).getHandleOrNot());
                if(receivedCommentList.get(i).getHandleOrNot() == 0){
                    count ++ ;
                }
            }
            if(count > 0){
                holder.iv_circle.setVisibility(View.VISIBLE);
                holder.tv_message_nums.setText(String.valueOf(count));
            }
            holder.title.setText("评论" + "(" + receivedCommentList.size() +")");
        }else{
            holder.title.setText(R.string.str_review);
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
        final ReviewChild reviewChild = getChild(groupPosition, childPosition);
        if(convertView == null){
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.child_review_item, parent, false);
            holder.ll_review = (LinearLayout) convertView.findViewById(R.id.ll_review);
            holder.iv_review_icon = (CircleImageView) convertView.findViewById(R.id.iv_review_icon);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_review = (TextView) convertView.findViewById(R.id.tv_review);
            holder.tv_review_time = (TextView) convertView.findViewById(R.id.tv_review_time);
            holder.iv_activity_img = (ImageView) convertView.findViewById(R.id.iv_activity_img);
            holder.tv_activity_name = (TextView) convertView.findViewById(R.id.tv_activity_name);
            convertView.setTag(holder);
        }else{
            holder = (ChildHolder) convertView.getTag();
        }
        Log.d(TAG, "getRealChildView: " + reviewChild.userHeadImagePath);
        Log.d(TAG, "getRealChildImageView: " + reviewChild.activityImagePath);
        Picasso.with(mContext).load(reviewChild.userHeadImagePath).placeholder(R.drawable.no_img).into(holder.iv_review_icon);
        holder.tv_username.setText(reviewChild.username);
        holder.tv_review.setText(reviewChild.review);
        holder.tv_review_time.setText(reviewChild.review_time);
        holder.tv_activity_name.setText(reviewChild.activity_name);
        Picasso.with(mContext).load(reviewChild.activityImagePath).into(holder.iv_activity_img);

        if(reviewChild.handleOrNot == 0){
            holder.ll_review.setBackgroundResource(R.color.background);
        }else{
            holder.ll_review.setBackgroundResource(R.color.white);
        }
        holder.ll_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reviewChild.handleOrNot == 0){
                    Log.d(TAG, "onClick: 执行了==============" );
                    changeStatus(userName, reviewChild.adviceId, 1);
                }
                if(reviewChild.activityId != 0){
                    getActivityInfo(reviewChild.activityId);
                }else{
                    getProjectInfo(reviewChild.projectId);
                }
                Log.d(TAG, "onClick: " + reviewChild.activityId);
                //Toast.makeText(mContext, "position:" + reviewChild.activityId, Toast.LENGTH_LONG).show();
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
                            db.update(DbReceivedComment.class, WhereBuilder.b("adviceId", "=", adviceId),new KeyValue("handleOrNot",1));
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
        commentsList = JSONArray.parseArray(result.optJSONArray("commentUserInfoList").toString(), CommentContents.class);
        lovedUsersList = JSONArray.parseArray(result.optJSONArray("lovedUserList").toString(), LovedUsers.class);
        DbActivity dbActivity = null;
        List<DbActivityImage> dbActivityImage = null;
        List<ActivityImage> activityImages;
        Activity activity = new Activity();
        try {
            dbActivity = db.selector(DbActivity.class).where("activityId", "=", activityId).findFirst();
            dbActivityImage = db.selector(DbActivityImage.class).where("activityId", "=", activityId).findAll();
            Log.d(TAG, "getActivityNewData: " + dbActivityImage.size() + dbActivityImage.get(0).getActivityImagePath());
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
        return receivedCommentGroupList.get(groupPosition).receivedCommentChildList.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ReviewGroup {
        public String title;
        public List<ReviewChild> receivedCommentChildList = new ArrayList<ReviewChild>();
    }

    @Override
    public void onClick(View v) {
        if(mOnChildItemClickListener != null){
            mOnChildItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public static class ReviewChild {
        public int adviceId;
        public String username;
        public String userHeadImagePath;
        public String review;
        public String review_time;
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
        LinearLayout ll_review;
        CircleImageView iv_review_icon;
        TextView tv_username;
        TextView tv_review;
        TextView tv_review_time;
        ImageView iv_activity_img;
        TextView tv_activity_name;
    }

    public static interface OnChildItemClickListener {
        void onItemClick(View view , Object data);
    }

    public void setOnChildItemClickListener(OnChildItemClickListener listener) {
        this.mOnChildItemClickListener = listener;
    }
}
