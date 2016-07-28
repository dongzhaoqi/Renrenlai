package com.siti.renrenlai.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.ProjectInfo;
import com.siti.renrenlai.adapter.FundIntroAdapter;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.db.DbProject;
import com.siti.renrenlai.db.DbProjectImage;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/7/22.
 */
public class MoreDreamFragment extends BaseFragment {

    @Bind(R.id.tv_no_dream)
    TextView tvNoDream;
    @Bind(R.id.dream_fund_list)
    XRecyclerView dreamFundList;
    private View view;
    String userName;
    String url = ConstantValue.GET_PROJECT_LIST;
    private FundIntroAdapter fundAdapter;
    private List<Project> projectList;       //项目列表
    private DbManager db;
    private static final String TAG = "MoreDreamFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_more_dream, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userName");

        initView();
        cache();
    }

    private void initView(){

        db = x.getDb(CustomApplication.getInstance().getDaoConfig());

        dreamFundList.setNestedScrollingEnabled(false);      //若不加上此句,ScrollView 嵌套RecylerView 会导致滑动不流畅
        // 设置LinearLayoutManager
        dreamFundList.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /**
     * 判断缓存中是否已经有请求的数据，若已有直接从缓存中取，若没有，发起网络请求
     */
    private void cache() {

        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                Log.d(TAG, "cache: " + jsonObject);
                if (jsonObject.optJSONArray("result") != null) {
                    getData(jsonObject);
                } else {
                    initData();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Cache data
            System.out.println("initData");
            initData();
        }
    }

    private void initData() {
        showProcessDialog();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("type", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        getData(response);
                        dismissProcessDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "error:" + error.getMessage());
                showToast("出错了!");
                dismissProcessDialog();
            }
        });
        CustomApplication.getInstance().addToRequestQueue(req);      //加入请求队列
    }

    private void getData(JSONObject response) {
        try {
            db.delete(DbProject.class);
            db.delete(DbProjectImage.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        JSONArray result = response.optJSONArray("result");
        if (result != null) {
            projectList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), Project.class);

            for (Project project : projectList) {
                DbProject dbProject = new DbProject();
                DbProjectImage dbProjectImage = new DbProjectImage();
                dbProject.setProjectId(project.getProjectId());
                dbProject.setProjectAddress(project.getProjectAddress());
                dbProject.setProjectDescrip(project.getProjectDescrip());
                dbProject.setProjectName(project.getProjectName());
                dbProject.setProjectImagePath(project.getProjectImagePath());

                dbProjectImage.setProjectId(project.getProjectId());

                if (project.getProjectImagePath() != null) {
                    //projectImageSize = project.getProjectImageList().size();
                    //for (int i = 0; i < projectImageSize; i++) {
                    dbProjectImage.setProjectImagePath(project.getProjectImagePath());
                    try {
                        db.save(dbProjectImage);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    //}
                }
                try {
                    db.save(dbProject);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            fundAdapter = new FundIntroAdapter(getActivity(), projectList);
            fundAdapter.setOnItemClickListener(new FundIntroAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, Object data) {
                    int projectId = Integer.parseInt(data.toString());
                    //cacheProject(projectId);
                    getProjectInfo(projectId);
                }
            });
            dreamFundList.setAdapter(fundAdapter);
            dreamFundList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            dreamFundList.setArrowImageView(R.drawable.iconfont_downgrey);

        }
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
                showToast("出错了!");
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
            Intent intent = new Intent(getActivity(), ProjectInfo.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("project", project);
            intent.putExtras(bundle);
            startAnimActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
