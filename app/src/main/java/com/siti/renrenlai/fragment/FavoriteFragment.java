package com.siti.renrenlai.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.TimeLineAdapter;
import com.siti.renrenlai.bean.TimeLineModel;
import com.siti.renrenlai.view.FragmentBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/4/1.
 */
public class FavoriteFragment extends FragmentBase {
    private View view;
    private Button btn_to_top;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TimeLineAdapter mTimeLineAdapter;

    private List<TimeLineModel> mDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        initData();
    }

    private void initView(){
        btn_to_top= (Button) findViewById(R.id.btn_to_top);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

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
    }



    private void initData(){
        for(int i = 0;i < 30;i++) {
            TimeLineModel model = new TimeLineModel();
            model.setName("Random"+i);
            model.setAge(i);
            mDataList.add(model);
        }

        mTimeLineAdapter = new TimeLineAdapter(mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

}
