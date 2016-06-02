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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/4/1.
 */
public class FavoriteFragment extends BaseFragment {
    private View view;
    private ActionButton btn_to_top;
    private XRecyclerView favorite_recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TimeLineAdapter mTimeLineAdapter;

    private List<TimeLineModel> mDataList = new ArrayList<>();
    private int refreshTime = 0;
    int position;
    private int times = 0;
    String api = null;
    String url = null;
    private static final String TAG = "FavoriteFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userName");

        try {
            api = "/getLovedActivityList?userName="+ URLEncoder.encode(userName, "utf-8");
            url = ConstantValue.urlRoot + api;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        cache();
    }


    /**
     * 判断缓存中是否已经有请求的数据，若已有直接从缓存中取，若没有，发起网络请求
     */
    private void cache() {
        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                System.out.println(TAG+"data:"+jsonObject);
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
        position = getArguments().getInt("position");
        btn_to_top = (ActionButton) findViewById(R.id.btn_to_top);

        favorite_recyclerView = (XRecyclerView) findViewById(R.id.favorite_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        favorite_recyclerView.setLayoutManager(linearLayoutManager);

        favorite_recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        favorite_recyclerView.setLaodingMoreProgressStyle(ProgressStyle.SquareSpin);
        favorite_recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        favorite_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                //ObjectAnimator.ofInt(favorite_recyclerView, "scrollY", 0).setDuration(1000).start();
            }
        });

        favorite_recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG+"refresh", "refreshTime:" + refreshTime);
                        refreshData();
                        favorite_recyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                Log.d("refresh", "refreshTime:" + refreshTime);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG+"load", "refreshTime:" + refreshTime);
                        loadData();
                        favorite_recyclerView.loadMoreComplete();
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
                        Log.d("response", "喜欢 response:" + response.toString());
                        getData(response);
                        showToast("获取喜欢成功!");
                        dismissProcessDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage().toString());
                showToast("出错了!");
                dismissProcessDialog();
            }
        });
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    private void getData(JSONObject response) {
        JSONArray result = response.optJSONArray("result");
        if(result != null){
            mDataList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), TimeLineModel.class);
        }
        mTimeLineAdapter = new TimeLineAdapter(getActivity(), mDataList, position);
        favorite_recyclerView.setAdapter(mTimeLineAdapter);
    }

    private void refreshData(){
        initData();
    }

    private void loadData() {
        initData();
    }


}
