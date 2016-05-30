package com.siti.renrenlai.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ImageAdapter;
import com.siti.renrenlai.bean.ActivityImage;
import com.siti.renrenlai.bean.TimeLineModel;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.view.HeaderLayout;
import com.siti.renrenlai.view.NoScrollGridView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Dong on 2016/4/26.
 */
public class MyLaunchActivity extends BaseActivity {

    @Bind(R.id.activity_img)
    ImageView activityImg;
    @Bind(R.id.activity_name)
    TextView activityName;
    @Bind(R.id.activity_time)
    TextView activityTime;
    @Bind(R.id.activity_location)
    TextView activityLocation;
    @Bind(R.id.activity_enroll_number)
    TextView activityEnrollNumber;
    @Bind(R.id.activity_loved_number)
    TextView activityLovedNumber;
    @Bind(R.id.detail_scrollgridview)
    NoScrollGridView noScrollGridView;
    @Bind(R.id.tv_comments_number)
    TextView tvCommentsNumber;
    @Bind(R.id.tv_like_number)
    TextView tvLikeNumber;
    private TimeLineModel model;
    private StringBuffer activity_time;
    private String str_activity_name, str_activity_begin_time, str_activity_end_time, str_activity_location,
            str_activity_enroll_number, str_activity_loved_number;
    private List<ActivityImage> imageList;              //活动封面
    private ArrayList<String> imagePath;
    private ImageAdapter picAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_launch);
        ButterKnife.bind(this);
        model = (TimeLineModel) getIntent().getExtras().getSerializable("model");
        initTopBarForBoth("我发起的活动", R.drawable.share, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                CommonUtils.showShare(MyLaunchActivity.this, model.getActivityName());
            }
        });

        initView();
    }

    public void initView() {
        imagePath = new ArrayList<>();
        activity_time = new StringBuffer();
        str_activity_name = model.getActivityName();
        str_activity_begin_time = model.getActivityStartTime();
        str_activity_end_time = model.getActivityEndTime();
        str_activity_loved_number = model.getLovedPersonNum();
        imageList = model.getActivityImageList();
        activity_time.append(str_activity_begin_time.substring(0, 10)).append("  ").append(str_activity_begin_time.substring(11, 16))
                .append("-").append(str_activity_end_time.substring(11, 16));

        activityName.setText(str_activity_name);
        activityTime.setText(activity_time);
        tvLikeNumber.setText(str_activity_loved_number);

        for(int i = 0; i < imageList.size(); i++){
            String path = imageList.get(i).getActivityImagePath();
            System.out.println("info path:" + path);
            imagePath.add(path);
        }
        if(imagePath != null && imagePath.size() > 0){
            Picasso.with(this).load(ConstantValue.urlRoot + imagePath.get(0)).into(activityImg);
        }
        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        picAdapter = new ImageAdapter(this, imagePath);
        noScrollGridView.setAdapter(picAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    @OnClick({R.id.layout_enroll_number, R.id.layout_favor_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_enroll_number:
                break;
            case R.id.layout_favor_number:
                break;
        }
    }
}
