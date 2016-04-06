package com.siti.renrenlai.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ActivityAdapter;
import com.siti.renrenlai.bean.ItemBean;
import com.siti.renrenlai.activity.ActivityInfo;
import com.siti.renrenlai.activity.ApplyActivity;
import com.siti.renrenlai.activity.IntroductionActivity;
import com.siti.renrenlai.activity.SearchActivity;
import com.siti.renrenlai.activity.ViewProjectActivity;
import com.siti.renrenlai.view.FragmentBase;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 3/22/2016.
 */
public class FindFragment extends FragmentBase implements View.OnClickListener{

    private View view;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<ItemBean> itemList;
    private RelativeLayout layout_introduction, layout_apply, layout_view_project;
    private ActivityAdapter adapter;
    private String[] images = new String[]{
            "http://api.androidhive.info/music/images/adele.png",
            "http://api.androidhive.info/music/images/eminem.png",
            "http://www.ld12.com/upimg358/allimg/c140921/14112A4V34010-219218.jpg",
            "http://www.ld12.com/upimg358/allimg/c150619/1434F6225920Z-105122.jpg",
            "http://api.androidhive.info/music/images/mj.png"
    };
    private String[] strs = new String[]{"item1","item2","item3","item4","item5"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_find,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initView();
        initEvent();
    }

    private void initView() {

        initTopBarForRight("发现", R.drawable.ic_action_action_search, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                startAnimActivity(SearchActivity.class);
            }
        });

        layout_introduction = (RelativeLayout) findViewById(R.id.layout_introduction);
        layout_apply = (RelativeLayout) findViewById(R.id.layout_apply);
        layout_view_project = (RelativeLayout) findViewById(R.id.layout_view_project);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        // 设置LinearLayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ActivityAdapter(getActivity(), itemList);
        mRecyclerView.setAdapter(adapter);

    }

    private void initData() {
        itemList = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            ItemBean item = new ItemBean();
            item.setImg(images[i]);
            item.setTv(strs[i]);
            itemList.add(item);
        }
    }

    private void initEvent(){
        layout_introduction.setOnClickListener(this);
        layout_apply.setOnClickListener(this);
        layout_view_project.setOnClickListener(this);
        adapter.setOnItemClickListener(new ActivityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                //showToast(data.toString());
                int pos = Integer.parseInt(data.toString());
                Intent intent = new Intent(getActivity(),ActivityInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", itemList.get(pos).getTv());
                bundle.putString("img", itemList.get(pos).getImg());
                intent.putExtras(bundle);
                startAnimActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_introduction:
                startAnimActivity(IntroductionActivity.class);
                break;
            case R.id.layout_apply:
                startAnimActivity(ApplyActivity.class);
                break;
            case R.id.layout_view_project:
                startAnimActivity(ViewProjectActivity.class);
                break;
        }
    }
}
