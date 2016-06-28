package com.siti.renrenlai.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.FeedbackActivity;
import com.siti.renrenlai.activity.LoginActivity;
import com.siti.renrenlai.activity.MessageActivity;
import com.siti.renrenlai.activity.MyActivity;
import com.siti.renrenlai.activity.MyProfileActivity;
import com.siti.renrenlai.bean.User;
import com.siti.renrenlai.db.DbReceivedComment;
import com.siti.renrenlai.db.DbReceivedLike;
import com.siti.renrenlai.db.DbSystemMessage;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    String userHeadPicImagePath, userName, url1, url2, url3;
    boolean isSignedin = false;
    private DbManager db;
    private List<DbSystemMessage> systemMessageList;
    private List<DbReceivedComment> receivedCommentList;
    private List<DbReceivedLike> receivedLikeList;
    int systemMessageSize, receivedReviewSize, receivedLikeSize;
    private static final String TAG = "MeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTopBarForOnlyTitle("我的");

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

            initMessage2();
        }
    }

    private void initMessage2(){
        try {
            systemMessageList = db.selector(DbSystemMessage.class).where("handleOrNot", "=", "0").findAll();
            receivedCommentList = db.selector(DbReceivedComment.class).where("handleOrNot", "=", "0").findAll();
            receivedLikeList = db.selector(DbReceivedLike.class).where("handleOrNot", "=", "0").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        systemMessageSize = systemMessageList == null ?  0 : systemMessageList.size();
        receivedReviewSize = receivedCommentList == null ? 0 : receivedCommentList.size();
        receivedLikeSize = receivedLikeList == null ? 0 : receivedLikeList.size();

        if ((systemMessageList != null && systemMessageList.size() > 0) || (receivedCommentList != null && receivedCommentList.size() >0)
                || (receivedLikeList != null && receivedLikeList.size() > 0)) {
            iv_circle.setVisibility(View.VISIBLE);
            tv_message_nums.setText(String.valueOf(systemMessageSize + receivedReviewSize + receivedLikeSize));
        }
    }

    private void initMessage() {
        try {
            systemMessageList = db.selector(DbSystemMessage.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            receivedCommentList = db.selector(DbReceivedComment.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            receivedLikeList = db.selector(DbReceivedLike.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        systemMessageSize = systemMessageList == null ?  0 : systemMessageList.size();
        receivedReviewSize = receivedCommentList == null ? 0 : receivedCommentList.size();
        receivedLikeSize = receivedLikeList == null ? 0 : receivedLikeList.size();

        if ((systemMessageList != null && systemMessageList.size() > 0) || (receivedCommentList != null && receivedCommentList.size() >0)
                || (receivedLikeList != null && receivedLikeList.size() > 0)) {
            iv_circle.setVisibility(View.VISIBLE);
            tv_message_nums.setText(String.valueOf(systemMessageSize + receivedReviewSize + receivedLikeSize));
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
        if(!"0".equals(userName)){
            initMessage2();
        }
    }
}
