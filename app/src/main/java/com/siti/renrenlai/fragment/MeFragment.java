package com.siti.renrenlai.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.FeedbackActivity;
import com.siti.renrenlai.activity.LoginActivity;
import com.siti.renrenlai.activity.MessageActivity;
import com.siti.renrenlai.activity.MyActivity;
import com.siti.renrenlai.activity.MyProfileActivity;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.FragmentBase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Dong on 3/22/2016.
 */
public class MeFragment extends FragmentBase implements View.OnClickListener {

    @Bind(R.id.layout_name) RelativeLayout layout_name;
    @Bind(R.id.layout_favorite) LinearLayout layout_favorite;
    @Bind(R.id.layout_enroll) LinearLayout layout_enroll;
    @Bind(R.id.layout_launch) LinearLayout layout_launch;
    @Bind(R.id.layout_message) RelativeLayout layout_message;
    @Bind(R.id.layout_invite) RelativeLayout layout_invite;
    @Bind(R.id.layout_feedback) RelativeLayout layout_feedback;
    @Bind(R.id.layout_logout) RelativeLayout layout_logout;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTopBarForOnlyTitle("我的");
    }


    private void showShare() {
        ShareSDK.initSDK(getActivity());
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
        oks.setText("仁人来");
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
        oks.show(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.layout_name, R.id.layout_favorite, R.id.layout_enroll, R.id.layout_launch,
            R.id.layout_message, R.id.layout_invite, R.id.layout_feedback, R.id.layout_logout})
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.layout_name:
                startAnimActivity(MyProfileActivity.class);
                break;
            case R.id.layout_favorite:
                intent = new Intent(getActivity(), MyActivity.class);
                intent.putExtra("pos", 0);
                startAnimActivity(intent);
                break;
            case R.id.layout_enroll:
                intent = new Intent(getActivity(), MyActivity.class);
                intent.putExtra("pos", 1);
                startAnimActivity(intent);
                break;
            case R.id.layout_launch:
                intent = new Intent(getActivity(), MyActivity.class);
                intent.putExtra("pos", 2);
                startAnimActivity(intent);
                break;
            case R.id.layout_message:
                startAnimActivity(MessageActivity.class);
                break;
            case R.id.layout_invite:
                showShare();
                break;
            case R.id.layout_logout:
                SharedPreferencesUtil.writeString(SharedPreferencesUtil
                                .getSharedPreference(getActivity(), "login"),
                        "userName", "0");
                startAnimActivity(LoginActivity.class);
                break;
            case R.id.layout_feedback:
                startAnimActivity(FeedbackActivity.class);
                break;
        }
    }
}
