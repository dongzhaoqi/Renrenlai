package com.siti.renrenlai.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.TimeLineAdapter;
import com.siti.renrenlai.bean.TimeLineModel;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.software.shell.fab.ActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/4/1.
 */
public class EnrollFragment extends BaseFragment {

    private View view;
    private ActionButton btn_to_top;
    private XRecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TimeLineAdapter mTimeLineAdapter;

    private List<TimeLineModel> mDataList = new ArrayList<>();
    private int refreshTime = 0;
    private int times = 0;
    String api = null;
    String url = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_enroll,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userName");

        try {
            api = "/getParticipateActivityList?userName="+ URLEncoder.encode(userName, "utf-8");
            url = ConstantValue.urlRoot + api;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        btn_to_top = (ActionButton) findViewById(R.id.btn_to_top);

        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mTimeLineAdapter = new TimeLineAdapter(getActivity(), mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SquareSpin);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    btn_to_top.setVisibility(View.VISIBLE);
                } else {
                    btn_to_top.setVisibility(View.INVISIBLE);
                }
            }
        });

        btn_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
                //ObjectAnimator.ofInt(mRecyclerView, "scrollY", 0).setDuration(1000).start();
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Log.d("refresh", "refreshTime:" + refreshTime);
                        refreshData();
                        mRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                Log.d("refresh", "refreshTime:" + refreshTime);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Log.d("refresh", "refreshTime:" + refreshTime);
                        loadData();
                        mRecyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });
    }

    private void initData() {

        System.out.println("url:" + url);
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "报名 response:" + response.toString());
                        showToast("获取报名成功!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                showToast("出错了!");
            }
        });
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    private void getData(JSONObject response){

    }

    private void refreshData(){
        initData();
        /*for (int i = 0; i < 2; i++) {
            TimeLineModel model = new TimeLineModel();
            model.setTime("2016年4月3号");
            model.setTitle("refresh:" + i + "春季亲子运动会报名");
            mDataList.add(0, model);
        }
        mTimeLineAdapter.notifyDataSetChanged();*/
    }

    private void loadData() {
        for (int i = 0; i < 3; i++) {
            TimeLineModel model = new TimeLineModel();
            model.setActivityStartTime("2016年4月3号");
            model.setActivityName("load:" + i + "春季亲子运动会报名");
            mDataList.add(mDataList.size(), model);
        }
        mTimeLineAdapter.notifyDataSetChanged();
    }
}