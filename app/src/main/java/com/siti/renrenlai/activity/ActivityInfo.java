package com.siti.renrenlai.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;
import com.squareup.picasso.Picasso;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Dong on 2016/3/22.
 */
public class ActivityInfo extends BaseActivity implements OnClickListener {

    private TextView txt_avtivity_name;
    private ImageView activity_img;
    private RelativeLayout layout_contact;
    private String activity_title;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViews();
        initEvent();
    }

    private void initViews() {
        initTopBarForBoth("活动详情", R.drawable.ic_share_white_24dp, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showShare();
            }
        });
        activity = (Activity) getIntent().getExtras().getSerializable("activity");

        activity_title = activity.getTv();

        txt_avtivity_name = (TextView) findViewById(R.id.activity_name);
        activity_img = (ImageView) findViewById(R.id.activity_img);
        layout_contact = (RelativeLayout) findViewById(R.id.layout_contact);
        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view);
        expTv1.setText(getString(R.string.dummy_text1));
        txt_avtivity_name.setText(activity.getTv());
        Picasso.with(this).load(activity.getImg()).into(activity_img);

    }

    private void initEvent(){
        layout_contact.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_contact:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + "13761076562"));
                startActivity(intent);
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
