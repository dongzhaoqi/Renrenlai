package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.view.NoScrollGridView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/5/19.
 */
public class PreviewActivity extends BaseActivity {

    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.tv_subject)
    TextView tvSubject;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.tv_deadline)
    TextView tvDeadline;
    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_people)
    TextView tvPeople;
    @Bind(R.id.noScrollgridview)
    NoScrollGridView noScrollgridview;
    @Bind(R.id.tv_detail)
    TextView tvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        initTopBarForLeft("预览活动");

        initView();
    }

    private void initView(){
        Activity activity = (Activity) getIntent().getSerializableExtra("activity");
        tvCategory.setText(activity.getActivityType());
        tvSubject.setText(activity.getActivityName());
        tvStartTime.setText(activity.getActivityStartTime());
        tvEndTime.setText(activity.getActivityEndTime());
        tvDeadline.setText(activity.getDeadline());
        tvPlace.setText(activity.getActivityAddress());
        tvPeople.setText(activity.getParticipateNum());
        tvDetail.setText(activity.getActivityDescrip());
    }
}
