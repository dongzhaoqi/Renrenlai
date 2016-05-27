package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.FundIntroAdapter;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_intro);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        initView();

        initData();
    }

    private void initView() {
        initTopBarForBoth("家园创变大赛", R.drawable.share, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showShare();
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
        String api = "/getProjectListForApp";
        String url = ConstantValue.urlRoot + api;
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "error:" + error.getMessage());
                showToast("出错了!");
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
                        String result = null;
                        try {
                            result = response.getString("result");
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

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.ssdk_oks_share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("dd");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //oks.setImageUrl("http://img05.tooopen.com/images/20160108/tooopen_sy_153700436869.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }


}
