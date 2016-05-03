package com.siti.renrenlai.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.FundIntroActivity;
import com.siti.renrenlai.adapter.ActivityAdapter;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.activity.ActivityInfo;
import com.siti.renrenlai.activity.SearchActivity;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.view.FragmentBase;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;
import com.siti.renrenlai.view.HeaderLayout.onLeftTextClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 3/22/2016.
 */
public class FindFragment extends FragmentBase implements View.OnClickListener{

    private View view;
    private Context mContext;
    private XRecyclerView mXRecyclerView;
    private List<Activity> itemList;
    private ActivityAdapter adapter;
    private TextView tv_fund_intro;
    private String[] images = new String[]{
            "http://img1.imgtn.bdimg.com/it/u=1056505034,278532731&fm=206&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=19688821,301685728&fm=206&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=249146884,1242359836&fm=206&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=4124932944,3228346692&fm=206&gp=0.jpg",
            "http://api.androidhive.info/music/images/mj.png"
    };
    private String[] strs = new String[]{"缤纷广场舞","南新七色馆\n儿童绘画营","春季夜跑族","野外踏青","草莓音乐节"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_find,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initData2();
        initView();
        initEvent();
    }

    private void initView() {

        initTopBarForLeftTextBoth("发现", "阳光小区", new onLeftTextClickListener() {
            @Override
            public void onClick() {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new String[]{"上海","北京","广州","深圳"}))
                        .setCancelable(true)
                        .setGravity(Gravity.TOP)
                        .setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogPlus dialog) {

                            }
                        })
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                initLeftText(item.toString());
                                dialog.dismiss();
                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogPlus dialog) {

                            }
                        })
                        .setOnBackPressListener(new OnBackPressListener() {
                            @Override
                            public void onBackPressed(DialogPlus dialogPlus) {

                            }
                        })
                        .create();

                dialogPlus.show();
            }
        }, R.drawable.ic_action_search, new onRightImageButtonClickListener(){

            @Override
            public void onClick() {
                startAnimActivity(SearchActivity.class);
            }
        });


        tv_fund_intro= (TextView) findViewById(R.id.tv_fund_intro);

        mXRecyclerView = (XRecyclerView) findViewById(R.id.list);
        // 设置LinearLayoutManager
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ActivityAdapter(getActivity(), itemList);
        mXRecyclerView.setAdapter(adapter);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mXRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        loadData();
                        mXRecyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });
    }


    private void initData2(){
        /*String api = "/getActivityListForApp.action";
        String url = ConstantValue.urlRoot + api;
        Log.d("FindFragment", "url:" + url);
        //请求成功
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onResponse", response.toString());
            }
        };

        //请求失败
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(getContext(),
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }
                }
                Log.e("onErrorResponse", error.getMessage(), error);
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, listener, errorListener);
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列*/

        String api = "api/content/libao/getList";
        String url = ConstantValue.urlRoot2 + api;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", "dq");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //请求成功
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject arg0) {
                Log.d("onResponse", arg0.toString());
            }
        };

        //请求失败
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.getMessage(), error);
                byte[] htmlBodyBytes = error.networkResponse.data;
                Log.e("onErrorResponse", new String(htmlBodyBytes), error);
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(url, jsonObject, listener, errorListener);
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    private void initData() {
        itemList = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            Activity item = new Activity();
            item.setImg(images[i]);
            item.setTv(strs[i]);
            itemList.add(item);
        }
    }

    private void loadData() {
        for (int i = 0; i < 3; i++) {
            Activity item = new Activity();
            item.setImg(images[i]);
            item.setTv(strs[i]);
            itemList.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private void initEvent(){
        //layout_introduction.setOnClickListener(this);
        //layout_apply.setOnClickListener(this);
        //layout_view_project.setOnClickListener(this);
        tv_fund_intro.setOnClickListener(this);

        adapter.setOnItemClickListener(new ActivityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                int pos = Integer.parseInt(data.toString());
                Activity activity = itemList.get(pos);
                Intent intent = new Intent(getActivity(),ActivityInfo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity", activity);
                intent.putExtras(bundle);
                startAnimActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            /*case R.id.layout_introduction:
                startAnimActivity(IntroductionActivity.class);
                break;
            case R.id.layout_apply:
                startAnimActivity(ApplyActivity.class);
                break;
            case R.id.layout_view_project:
                startAnimActivity(ViewProjectActivity.class);
                break;*/
            case R.id.tv_fund_intro:
                startAnimActivity(FundIntroActivity.class);
                break;
        }
    }
}
