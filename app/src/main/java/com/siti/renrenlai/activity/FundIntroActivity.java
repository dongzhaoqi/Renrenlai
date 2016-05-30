package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
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
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Dong on 2016/4/12.
 */
public class FundIntroActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.fund_list)
    XRecyclerView fundList;
    @Bind(R.id.layout_dream_go)
    RelativeLayout layoutDreamGo;

    private List<Project> projectList;       //项目列表
    private FundIntroAdapter fundAdapter;
    private static final String TAG = "FundIntroActivity";
    String api = "/getProjectListForApp";
    String url = ConstantValue.urlRoot + api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_intro);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        initView();

        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                getData(jsonObject);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            // Cache data
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
        /*View header = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
        fundList.addHeaderView(header);*/
        fundList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        fundList.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        fundList.loadMoreComplete();
                    }
                }, 1000);
            }
        });
    }

    private void initData() {
        showProcessDialog();
        Log.d("FindFragment", "url:" + url);
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
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
                getProjectInfo(Integer.parseInt(data.toString()));
            }
        });
        fundList.setAdapter(fundAdapter);
        fundList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        fundList.setArrowImageView(R.drawable.iconfont_downgrey);
    }

    @OnClick({R.id.layout_dream_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_dream_go:
                startAnimActivity(ApplyWishActivity.class);
                break;
        }
    }

    public void getProjectInfo(int projectId){
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(FundIntroActivity.this, "login"), "userName");
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
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                showToast("出错了!");
            }
        });

        CustomApplication.getInstance().addToRequestQueue(req);
    }

}
