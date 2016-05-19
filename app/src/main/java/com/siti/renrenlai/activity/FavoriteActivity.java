package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.LikeAdapter;
import com.siti.renrenlai.bean.LovedUsers;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/5/19.
 */
public class FavoriteActivity extends BaseActivity {

    @Bind(R.id.rv_favorite)
    RecyclerView rvFavorite;
    private RecyclerView.LayoutManager layoutManager;
    private LikeAdapter mAdapter;
    private List<LovedUsers>likeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        ButterKnife.bind(this);

        initTopBarForLeft("喜欢");
        likeList = (List<LovedUsers>) getIntent().getSerializableExtra("likeList");
        System.out.println("likeList:" + likeList.get(0).getUserHeadPicImagePath());
        mAdapter = new LikeAdapter(this, likeList);
        layoutManager = new LinearLayoutManager(this);
        rvFavorite.setLayoutManager(layoutManager);
        rvFavorite.setAdapter(mAdapter);
    }
}
