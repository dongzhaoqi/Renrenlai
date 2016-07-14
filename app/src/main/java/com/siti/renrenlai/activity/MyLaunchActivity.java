package com.siti.renrenlai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ImageAdapter;
import com.siti.renrenlai.bean.ActivityImage;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.ParticipateUser;
import com.siti.renrenlai.bean.TimeLineModel;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.view.HeaderLayout;
import com.siti.renrenlai.view.NoScrollGridView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
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
    @Bind(R.id.layout_enroll_number)
    RelativeLayout layout_enroll_number;
    @Bind(R.id.iv_expand_user)
    ImageView iv_expand_user;
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
    private List<ParticipateUser> participateUserList;
    private List<ActivityImage> imageList;              //活动封面
    private List<CommentContents> commentContentsList;
    private ArrayList<String> imagePath;
    private ImageAdapter picAdapter;
    private int position;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_launch);
        ButterKnife.bind(this);

        position = getIntent().getExtras().getInt("position");
        if(position == 0){
            title = "我喜欢的活动";
            layout_enroll_number.setEnabled(false);
        }else if(position == 1){
            title = "我报名的活动";
            layout_enroll_number.setEnabled(false);
        }else if(position == 2){
            title = "我发起的活动";
            iv_expand_user.setVisibility(View.VISIBLE);
            layout_enroll_number.setClickable(true);
        }

        model = (TimeLineModel) getIntent().getExtras().getSerializable("model");
        initTopBarForBoth(title, R.drawable.share, new HeaderLayout.onRightImageButtonClickListener() {
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
        str_activity_location = model.getActivityAddress();
        str_activity_loved_number = model.getLovedPersonNum();
        str_activity_enroll_number = model.getSignPersonNum();
        participateUserList = model.getParticipateUserList();
        commentContentsList = model.getCommentContents();
        imageList = model.getActivityImageList();
        activity_time.append(str_activity_begin_time.substring(0, 10)).append("  ").append(str_activity_begin_time.substring(11, 16))
                .append(" - ").append(str_activity_end_time.substring(11, 16));

        activityName.setText(str_activity_name);
        activityTime.setText(activity_time);
        activityLocation.setText(str_activity_location);
        if(commentContentsList != null && commentContentsList.size() > 0){
            tvCommentsNumber.setText(String.valueOf(commentContentsList.size()));
        }else{
            tvCommentsNumber.setText("0");
        }
        tvLikeNumber.setText(str_activity_loved_number);
        activityLovedNumber.setText(str_activity_loved_number);
        activityEnrollNumber.setText(str_activity_enroll_number);

        for(int i = 0; i < imageList.size(); i++){
            String path = imageList.get(i).getActivityImagePath();
            System.out.println("info path:" + path);
            imagePath.add(path);
        }
        if(imagePath != null && imagePath.size() > 0){
            Picasso.with(this).load(imagePath.get(0)).into(activityImg);
        }
        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        picAdapter = new ImageAdapter(this, imagePath);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyLaunchActivity.this, GalleryImageActivity.class);
                intent.putStringArrayListExtra("imagePath", imagePath);
                intent.putExtra("ID", i);
                startActivity(intent);

            }
        });
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
                if(layout_enroll_number.isClickable()) {
                    Intent intent = new Intent(this, ParticipateActivity.class);
                    intent.putExtra("participateUserList", (Serializable) participateUserList);
                    startActivity(intent);
                }
                break;
            case R.id.layout_favor_number:
                break;
        }
    }
}
