package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ActivityAdapter;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/6/23.
 */
public class ActivityList extends BaseActivity {

    private List<Activity> activityList;
    private ActivityAdapter adapter;
    private RecyclerView recyclerView;
    private List<LovedUsers> lovedUsersList = new ArrayList<>();
    private List<CommentContents> commentsList = new ArrayList<>();
    int activityId;
    String userName;
    private static final String TAG = "ActivityList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initTopBarForLeft("相关活动");
        recyclerView = (RecyclerView) findViewById(R.id.activity_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        initData();
    }

    private void initData(){
        activityList = (List<Activity>) getIntent().getSerializableExtra("activityList");
        adapter = new ActivityAdapter(this, activityList);
        adapter.setOnItemClickListener(new ActivityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {

                int pos = Integer.parseInt(data.toString());
                activityId = activityList.get(pos).getActivityId();
                getActivityInfo(pos, activityId);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void getActivityInfo(final int pos, final int activityId) {
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
                        Log.d(TAG, response.toString());
                        getActivityNewData(pos, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                showToast("出错了!");
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }


    public void getActivityNewData(int pos, JSONObject response) {
        Log.d(TAG, "getActivityNewData: " + response);
        JSONObject result = response.optJSONObject("result");
        commentsList = com.alibaba.fastjson.JSONArray.parseArray(result.optJSONArray("commentUserInfoList").toString(), CommentContents.class);
        lovedUsersList =  com.alibaba.fastjson.JSONArray.parseArray(result.optJSONArray("lovedUserList").toString(), LovedUsers.class);
        Activity activity = activityList.get(pos);
        activity.setLovedIs(result.optBoolean("lovedIs"));
        activity.setSignUpIs(result.optBoolean("signUpIs"));
        activity.setCommentContents(commentsList);
        activity.setLovedUsers(lovedUsersList);
        Intent intent = new Intent(this, ActivityInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("activity", activity);
        intent.putExtras(bundle);
        startAnimActivity(intent);
    }
}
