package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.FundIntroAdapter;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Dong on 2016/4/12.
 */
public class FundIntroActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.fund_list)
    XRecyclerView fundList;
    @Bind(R.id.layout_dream_go)
    RelativeLayout layoutDreamGo;
    private ImageView iv_project;
    private List<Project> projectList;       //项目列表
    private FundIntroAdapter fundAdapter;
    private static final String TAG = "FundIntroActivity";
    String api, url, userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_intro);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        initView();

        cache();
    }

    /**
     * 判断缓存中是否已经有请求的数据，若已有直接从缓存中取，若没有，发起网络请求
     */
    private void cache() {
        api = "/getProjectListForApp";
        url = ConstantValue.urlRoot + api;
        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                System.out.println("data:"+jsonObject);
                getData(jsonObject);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            // Cache data
            System.out.println("initData");
            initData();
        }
    }

    private void initView() {
        initTopBarForBoth("家园创变大赛", R.drawable.share, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                CommonUtils.showShare(FundIntroActivity.this, "家园创变大赛");
            }
        });
        // 设置LinearLayoutManager
        fundList.setLayoutManager(new LinearLayoutManager(this));
        View header = LayoutInflater.from(this).inflate(R.layout.project_header, (ViewGroup) findViewById(android.R.id.content), false);
        iv_project = (ImageView) header.findViewById(R.id.iv_project);
        fundList.addHeaderView(header);

        fundList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        refreshData();
                        fundList.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        loadData();
                        fundList.loadMoreComplete();
                    }
                }, 1000);
            }
        });
    }

    private void initData() {
        showProcessDialog();
        Log.d("FindFragment", "url:" + url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
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
        JSONArray result = response.optJSONArray("result");
        projectList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), Project.class);
        Log.d(TAG, "getData() returned: " + projectList.size());

        fundAdapter = new FundIntroAdapter(this, projectList);
        fundAdapter.setOnItemClickListener(new FundIntroAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                int projectId = Integer.parseInt(data.toString());
                cacheProject(projectId);
            }
        });
        fundList.setAdapter(fundAdapter);
        fundList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        fundList.setArrowImageView(R.drawable.iconfont_downgrey);
        fundList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    layoutDreamGo.setVisibility(View.VISIBLE);
                } else {
                    layoutDreamGo.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 下拉刷新获取数据
     */
    private void refreshData(){
        initData();
    }

    /**
     * 上拉加载更多
     */
    private void loadData() {
        initData();
    }


    @OnClick({R.id.layout_dream_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_dream_go:
                startAnimActivity(ApplyWishActivity.class);
                break;
        }
    }

    /**
     * 用户点击某一具体项目时,若有缓存则直接从缓存中拿数据,再跳转到项目详情, 若没有进行缓存
     * @param projectId
     */
    private void cacheProject(int projectId){
        api = "/getProjectInfoForApp";
        url = ConstantValue.urlRoot + api;
        String projectUrl = url + projectId;
        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(projectUrl);
        if(entry != null){              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                System.out.println("data:"+jsonObject);
                getProject(jsonObject);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            // Cache data
            System.out.println("initData");
            getProjectInfo(projectId);
        }
    }

    public void getProjectInfo(int projectId){
        String api = "/getProjectInfoForApp";
        String url = ConstantValue.urlRoot + api;

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

        CustomApplication.getInstance().addToRequestQueue(req);
    }

    private void getProject(JSONObject response) {
        try {
            String result = response.getJSONObject("result").toString();
            Project project = com.alibaba.fastjson.JSONObject.parseObject(result, Project.class);
            Intent intent = new Intent(FundIntroActivity.this, ProjectInfo.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("project", project);
            intent.putExtras(bundle);
            startAnimActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
