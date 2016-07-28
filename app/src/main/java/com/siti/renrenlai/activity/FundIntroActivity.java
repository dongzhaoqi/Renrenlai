package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.FundIntroAdapter;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.fragment.MoreDreamFragment;
import com.siti.renrenlai.fragment.MyHomeFragment;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout;
import com.siti.renrenlai.view.ObservableScrollView;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Dong on 2016/4/12.
 */
public class FundIntroActivity extends BaseActivity implements View.OnClickListener, ObservableScrollView.ScrollViewListener {

    /*@Bind(R.id.fund_list)
    XRecyclerView fundList;*/
    @Bind(R.id.layout_dream_go)
    RelativeLayout layoutDreamGo;
    @Bind(R.id.ll_head_default)
    LinearLayout llHeadDefault;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.iv_project)
    ImageView ivProject;
    @Bind(R.id.ll_head)
    LinearLayout ll_head;
    @Bind(R.id.scroll_fund)
    ObservableScrollView scroll_fund;
    @Bind(R.id.btn_home)
    Button btnHome;
    @Bind(R.id.btn_dream)
    Button btnDream;
    private Fragment[] fragments;
    private MyHomeFragment myHomeFragment;
    private MoreDreamFragment moreDreamFragment;
    private int index;
    private int currentTabIndex;
    private Button[] mTabs;
    private ImageView iv_project;
    private RelativeLayout layout_home, layout_dream, layout_home_default, layout_dream_default;
    private Button btn_home_default, btn_dream_default;
    private List<Project> projectList;       //项目列表
    private FundIntroAdapter fundAdapter;
    private LinearLayout llHead;
    private static final String TAG = "FundIntroActivity";
    String userName;
    String url = ConstantValue.GET_PROJECT_LIST;
    private DbManager db;
    int mTotalScrolled = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_intro);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        initView();
        //initTab();
        //cache();
    }

    /**
     * 判断缓存中是否已经有请求的数据，若已有直接从缓存中取，若没有，发起网络请求
     */
    /*private void cache() {

        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                //Log.d(TAG, "cache: " + jsonObject);
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
    }*/
    private void initView() {
        initTopBarForBoth("家园创变大赛", R.drawable.share, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                CommonUtils.showShare(FundIntroActivity.this, "家园创变大赛");
            }
        });
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());

        if ("0".equals(userName)) {
            ivProject.setVisibility(View.GONE);
            ll_head.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            return;
        }

        mTabs = new Button[2];

        if(llHeadDefault.getVisibility() == View.VISIBLE){
            mTabs[0] = (Button) findViewById(R.id.btn_home_default);
            mTabs[1] = (Button) findViewById(R.id.btn_dream_default);
        }else{
            mTabs[0] = (Button) findViewById(R.id.btn_home);
            mTabs[1] = (Button) findViewById(R.id.btn_dream);
        }
        currentTabIndex = 0;
        mTabs[currentTabIndex].setSelected(true);

        initTab();

        scroll_fund.setScrollViewListener(this);

        /*// 设置LinearLayoutManager
        fundList.setLayoutManager(new LinearLayoutManager(this));
        View header = LayoutInflater.from(this).inflate(R.layout.project_header, (ViewGroup) findViewById(android.R.id.content), false);
        llHead = (LinearLayout) header.findViewById(R.id.ll_head);
        iv_project = (ImageView) header.findViewById(R.id.iv_project);
        layout_home = (RelativeLayout) header.findViewById(R.id.layout_home);
        layout_dream = (RelativeLayout) header.findViewById(R.id.layout_dream);

        layout_home.setOnClickListener(this);
        layout_dream.setOnClickListener(this);*/

        /*fundList.addHeaderView(header);

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
        });*/

    }

    private void initTab() {
        myHomeFragment = new MyHomeFragment();
        moreDreamFragment = new MoreDreamFragment();
        fragments = new Fragment[]{myHomeFragment, moreDreamFragment};

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fund_fragment_container, myHomeFragment)
                .show(myHomeFragment).commit();
    }

    /*private void initData() {
        showProcessDialog();
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
        mTotalScrolled = 0;
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

            fundAdapter = new FundIntroAdapter(this, projectList);
            fundAdapter.setOnItemClickListener(new FundIntroAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, Object data) {
                    int projectId = Integer.parseInt(data.toString());
                    //cacheProject(projectId);
                    getProjectInfo(projectId);
                }
            });
            fundList.setAdapter(fundAdapter);
            fundList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            fundList.setArrowImageView(R.drawable.iconfont_downgrey);
            fundList.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    mTotalScrolled += dy;
                    Log.e(TAG, "onScrolled getBottom: " + llHead.getBottom() + " llHead getHeight:" + llHead.getHeight() + " getTop():" + llHead.getTop() + " mTotalScrolled:" + mTotalScrolled);
                    if (mTotalScrolled >= llHead.getTop()) {
                        llHeadDefault.setVisibility(View.VISIBLE);
                        layout_home_default = (RelativeLayout) findViewById(R.id.layout_home_default);
                        layout_dream_default = (RelativeLayout) findViewById(R.id.layout_dream_default);
                        layout_home_default.setOnClickListener(FundIntroActivity.this);
                        layout_dream_default.setOnClickListener(FundIntroActivity.this);
                    } else {
                        llHeadDefault.setVisibility(View.GONE);
                    }
                    if (mTotalScrolled <= llHead.getBottom()) {
                        llHeadDefault.setVisibility(View.GONE);
                    }
                    if (dy > 0) {
                        layoutDreamGo.setVisibility(View.VISIBLE);
                        layoutDreamGo.setBackgroundResource(R.drawable.layout_top_border);
                    } else {
                        layoutDreamGo.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    *//**
     * 下拉刷新获取数据
     *//*
    private void refreshData() {
        initData();
        llHeadDefault.setVisibility(View.GONE);
    }

    */

    /**
     * 上拉加载更多
     *//*
    private void loadData() {
        initData();
        llHeadDefault.setVisibility(View.GONE);
    }*/
    @OnClick({R.id.layout_dream_go, R.id.btnLogin, R.id.iv_project, R.id.btn_home, R.id.btn_dream,
            R.id.btn_home_default, R.id.btn_dream_default})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.layout_dream_go:
                startAnimActivity(ApplyWishActivity.class);
                break;
            case R.id.btn_home:
            case R.id.btn_home_default:
                index = 0;
                break;
            case R.id.btn_dream:
            case R.id.btn_dream_default:
                index = 1;
                break;
            /*case R.id.btn_home_default:
                index = 0;
                break;
            case R.id.btn_dream_default:
                index = 1;
                break;*/

            case R.id.btnLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.iv_project:
                break;
        }

        if (id == R.id.btn_dream || id == R.id.btn_home || id == R.id.btn_home_default || id == R.id.btn_dream_default) {
            if (currentTabIndex != index) {
                FragmentTransaction trx = getSupportFragmentManager()
                        .beginTransaction();
                trx.remove(fragments[currentTabIndex]);
                if (!fragments[index].isAdded()) {
                    trx.replace(R.id.fund_fragment_container, fragments[index]);
                }
                trx.show(fragments[index]).commit();
            }
            mTabs[currentTabIndex].setSelected(false);
            mTabs[index].setSelected(true);
            currentTabIndex = index;
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.e(TAG, "onScrollChanged: " + "  y:" + y + " oldy:" + oldy + " llhead getTop:" + ll_head.getTop());
        if(y >= ll_head.getTop()){
            llHeadDefault.setVisibility(View.VISIBLE);
            layout_home_default = (RelativeLayout) findViewById(R.id.layout_home_default);
            layout_dream_default = (RelativeLayout) findViewById(R.id.layout_dream_default);
            btn_home_default = (Button) findViewById(R.id.btn_home_default);
            btn_dream_default = (Button) findViewById(R.id.btn_dream_default);
            btn_home_default.setOnClickListener(FundIntroActivity.this);
            btn_dream_default.setOnClickListener(FundIntroActivity.this);
            if(index == 0){
                btn_home_default.setSelected(true);
                btn_dream_default.setSelected(false);
            }else if(index == 1){
                btn_home_default.setSelected(false);
                btn_dream_default.setSelected(true);
            }
        }else {
            llHeadDefault.setVisibility(View.INVISIBLE);
        }

        if(index == 0 && oldy - y < 0){
            //向上滑 old < y
            layoutDreamGo.setVisibility(View.VISIBLE);
            layoutDreamGo.setBackgroundResource(R.drawable.layout_top_border);
        } else {
            layoutDreamGo.setVisibility(View.GONE);
        }
    }
    /**
     * 用户点击某一具体项目时,若有缓存则直接从缓存中拿数据,再跳转到项目详情, 若没有进行缓存
     *
     * @param projectId
     */
    /*private void cacheProject(int projectId) {
        url = ConstantValue.GET_PROJECT_INFO;
        String projectUrl = url + projectId;
        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(projectUrl);
        if (entry != null) {              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                System.out.println("data:" + jsonObject);
                getProject(jsonObject);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Cache data
            System.out.println("initData");
            getProjectInfo(projectId);
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
            Intent intent = new Intent(FundIntroActivity.this, ProjectInfo.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("project", project);
            intent.putExtras(bundle);
            startAnimActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


}
