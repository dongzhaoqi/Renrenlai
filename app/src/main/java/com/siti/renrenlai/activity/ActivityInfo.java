package com.siti.renrenlai.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Dong on 2016/3/22.
 */
public class ActivityInfo extends BaseActivity implements OnClickListener {

    @Bind(R.id.activity_img) ImageView activity_img;
    @Bind(R.id.activity_name) TextView tv_avtivity_name;
    @Bind(R.id.layout_fund) RelativeLayout layoutFund;
    @Bind(R.id.layout_contact) RelativeLayout layout_contact;
    @Bind(R.id.tv_activity_address) TextView tv_activity_address;
    @Bind(R.id.tv_activity_time) TextView tv_activity_time;
    @Bind(R.id.expand_text_view) ExpandableTextView expTv1;
    @Bind(R.id.btn_comment) Button btnComment;
    @Bind(R.id.btn_favor) Button btnFavor;
    @Bind(R.id.btn_publish) Button btnPublish;

    Activity activity;
    String activity_title, contact_tel, activity_address, activity_describ, activity_time;
    boolean isFavorPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        initTopBarForBoth("活动详情", R.drawable.share, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showShare();
            }
        });
        activity = (Activity) getIntent().getExtras().getSerializable("activity");

        if(activity != null){
            activity_title = activity.getActivityName();
            contact_tel = activity.getContactTel();
            activity_address = activity.getActivityAddress();
            activity_describ = activity.getActivityDescrip();
            activity_time = activity.getActivityStartTime() + "-" + activity.getActivityEndTime();
        }


        expTv1.setText(activity_describ);
        tv_avtivity_name.setText(activity_title);
        tv_activity_address.setText(activity_address);
        tv_activity_time.setText(activity_time);

        Picasso.with(this).load(activity.getActivityImg()).into(activity_img);

    }

    @OnClick({R.id.layout_fund, R.id.layout_contact, R.id.btn_comment, R.id.btn_favor, R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_fund:
                break;
            case R.id.layout_contact:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + contact_tel));
                startActivity(intent);
                break;
            case R.id.btn_comment:


                break;
            case R.id.btn_favor:
                if (!isFavorPressed) {
                    btnFavor.setSelected(true);
                } else {
                    btnFavor.setSelected(false);
                }
                isFavorPressed = !isFavorPressed;
                break;
            case R.id.btn_publish:
                break;
        }
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
        oks.setText(activity_title);
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


}
