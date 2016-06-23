package com.siti.renrenlai.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.FeedbackActivity;
import com.siti.renrenlai.activity.LoginActivity;
import com.siti.renrenlai.activity.MessageActivity;
import com.siti.renrenlai.activity.MyActivity;
import com.siti.renrenlai.activity.MyProfileActivity;
import com.siti.renrenlai.bean.User;
import com.siti.renrenlai.db.SystemMessage;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.squareup.picasso.Picasso;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dong on 3/22/2016.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.layout_name)
    RelativeLayout layout_name;
    @Bind(R.id.img_photo)
    CircleImageView img_photo;
    @Bind(R.id.layout_favorite)
    LinearLayout layout_favorite;
    @Bind(R.id.layout_enroll)
    LinearLayout layout_enroll;
    @Bind(R.id.layout_launch)
    LinearLayout layout_launch;
    @Bind(R.id.layout_message)
    RelativeLayout layout_message;
    @Bind(R.id.layout_invite)
    RelativeLayout layout_invite;
    @Bind(R.id.layout_feedback)
    RelativeLayout layout_feedback;
    @Bind(R.id.layout_logout)
    RelativeLayout layout_logout;
    @Bind(R.id.tv_userName)
    TextView tv_userName;
    @Bind(R.id.iv_circle)
    ImageView iv_circle;
    @Bind(R.id.tv_message_nums)
    TextView tv_message_nums;
    private View view;
    private User user;
    String userHeadPicImagePath, userName;
    boolean isSignedin = false;
    private DbManager db;
    private List<SystemMessage> systemMessageList;

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
        init();
    }

    private void init() {
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userName");
        userHeadPicImagePath = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userHeadPicImagePath");

        if (userName.equals("0")) {
            tv_userName.setText("请登录");
        } else {
            tv_userName.setText(userName);
            isSignedin = true;
            Picasso.with(getActivity()).load(userHeadPicImagePath).placeholder(R.drawable.no_img).into(img_photo);

            db = x.getDb(CustomApplication.getInstance().getDaoConfig());
            initMessage();
        }
    }

    private void initMessage() {
        try {
            systemMessageList = db.selector(SystemMessage.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (systemMessageList != null && systemMessageList.size() > 0) {
            iv_circle.setVisibility(View.VISIBLE);
            tv_message_nums.setText(String.valueOf(systemMessageList.size()));
        }

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
                if (!isSignedin) {
                    startAnimActivity(LoginActivity.class);
                } else {
                    startAnimActivity(MyProfileActivity.class);
                }
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
                CommonUtils.showShare(getActivity(), null);
                break;
            case R.id.layout_logout:
                showLogoutDialog();
                break;
            case R.id.layout_feedback:
                startAnimActivity(FeedbackActivity.class);
                break;
        }
    }

    public void showLogoutDialog() {
        new MaterialDialog.Builder(getActivity())
                .content(R.string.str_logout)
                .positiveText(R.string.okBtn)
                .negativeText(R.string.cancelBtn)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        JPushInterface.setAlias(getActivity(), "", null);
                        SharedPreferencesUtil.clearAll(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"));
                        startAnimActivity(LoginActivity.class);
                        getActivity().finish();
                    }
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        initMessage();
    }
}
