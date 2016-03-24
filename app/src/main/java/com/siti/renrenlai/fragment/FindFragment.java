package com.siti.renrenlai.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ImageAdapter;
import com.siti.renrenlai.bean.ItemBean;
import com.siti.renrenlai.ui.ActivityInfo;
import com.siti.renrenlai.ui.SearchActivity;
import com.siti.renrenlai.view.FragmentBase;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import java.util.ArrayList;

/**
 * Created by Dong on 3/22/2016.
 */
public class FindFragment extends FragmentBase {

    private View view;
    private Context mContext;
    private ListView mListView;
    private ArrayList<Object> itemList;
    private String[] images = new String[]{
            "http://api.androidhive.info/music/images/adele.png",
            "http://img1.imgtn.bdimg.com/it/u=4092462854,1557898995&fm=21&gp=0.jpg",
            "http://api.androidhive.info/music/images/eminem.png",
            "http://www.ld12.com/upimg358/allimg/c140921/14112A4V34010-219218.jpg",
            "http://www.ld12.com/upimg358/allimg/c150619/1434F6225920Z-105122.jpg",
            "http://api.androidhive.info/music/images/mj.png",
            "http://img0.imgtn.bdimg.com/it/u=4256610768,425170762&fm=21&gp=0.jpg",
            "http://gpgxx.nje.cn/JinBo_xscz_gpgxx/Space/uploadfile/photo/20130106201708.jpg",
            "http://p.3761.com/pic/88301406163694.jpg",
            "http://api.androidhive.info/music/images/rihanna.png",
            "http://www.feizl.com/upload2007/2012_06/120617142092295.jpg",
            "http://www.ld12.com/upimg358/allimg/c150715/143DPU03IF-5H92.jpg"
    };
    private String[] strs = new String[]{"item1","item2","item3","item4","item5",
            "item6","item7","item8","item9","item10","item11","item12"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_find,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {

        initTopBarForRight("发现", R.drawable.ic_action_action_search, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                startAnimActivity(SearchActivity.class);
            }
        });

        initData();

        mListView = (ListView) findViewById(R.id.list);

        ImageAdapter adapter = new ImageAdapter(getActivity(), itemList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAnimActivity(ActivityInfo.class);
            }
        });

        mContext = getActivity();

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

}
