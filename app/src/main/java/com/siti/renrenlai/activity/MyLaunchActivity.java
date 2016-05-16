package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.TimeLineModel;
import com.siti.renrenlai.view.HeaderLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
    private TimeLineModel model;
    private String str_activity_name, str_activity_time, str_activity_location,
            str_activity_enroll_number, str_activity_loved_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_launch);
        ButterKnife.bind(this);
        model = (TimeLineModel) getIntent().getExtras().getSerializable("model");
        initTopBarForBoth("我发起的活动", R.drawable.share, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showShare();
            }
        });

        initView();
    }

    public void initView(){
        str_activity_name = model.getTitle();
        str_activity_time = model.getTime();

        activityName.setText(str_activity_name);
        activityTime.setText(str_activity_time);

    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.ssdk_oks_share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(model.getTitle());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //oks.setImageUrl("http://img05.tooopen.com/images/20160108/tooopen_sy_153700436869.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
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
