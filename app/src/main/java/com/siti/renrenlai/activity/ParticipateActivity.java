package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ParticipateAdapter;
import com.siti.renrenlai.bean.ParticipateUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/5/19.
 */
public class ParticipateActivity extends BaseActivity {

    @Bind(R.id.rv_favorite)
    RecyclerView rvFavorite;
    @Bind(R.id.tv_nouser)
    TextView tv_nouser;

    private RecyclerView.LayoutManager layoutManager;
    private ParticipateAdapter mAdapter;
    private List<ParticipateUser> participateUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate_user);
        ButterKnife.bind(this);

        initTopBarForLeft("报名的用户");
        participateUserList = (List<ParticipateUser>) getIntent().getSerializableExtra("participateUserList");
        if(participateUserList == null || participateUserList.size() == 0){
            tv_nouser.setVisibility(View.VISIBLE);
            return;
        }
        mAdapter = new ParticipateAdapter(this, participateUserList);
        layoutManager = new LinearLayoutManager(this);
        rvFavorite.setLayoutManager(layoutManager);
        rvFavorite.setAdapter(mAdapter);
    }
}
