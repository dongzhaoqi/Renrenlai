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
import com.siti.renrenlai.activity.MainActivity;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    @Bind(R.id.tv_favorite)
    TextView tv_favorite;
    @Bind(R.id.layout_enroll)
    LinearLayout layout_enroll;
    @Bind(R.id.tv_enroll)
    TextView tv_enroll;
    @Bind(R.id.layout_launch)
    LinearLayout layout_launch;
    @Bind(R.id.tv_launch)
    TextView tv_launch;
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
    String userHeadPicImagePath, userName, realName, url, url1, url2, url3;
    boolean isSignedin = false;
    private DbManager db;
    private List<DbSystemMessage> systemMessageList;
    private List<DbReceivedComment> receivedCommentList;
    private List<DbReceivedLike> receivedLikeList;
    int count;                              //未读消息条数
    int myLikeActivitySize;                 //我喜欢的活动数量
    int myParticipateActivitySize;          //我报名的活动的数量
    int myLaunchActivitySize;               //我发起的活动的数量
    int systemMessageSize, receivedReviewSize, receivedLikeSize;
    private static final String TAG = "MeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        initTopBarForOnlyTitle("我的");
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userName");
        realName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "realName");
        userHeadPicImagePath = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userHeadPicImagePath");
        if (userName.equals("0")) {
            tv_userName.setText("请登录");
            layout_logout.setVisibility(View.GONE);
        } else {
            tv_userName.setText(realName);
            isSignedin = true;
            if(!userHeadPicImagePath.contains("http")){
                userHeadPicImagePath = ConstantValue.urlRoot + userHeadPicImagePath;
            }
            Log.e("userHeadPicImagePath: " , userHeadPicImagePath );
            Picasso.with(getActivity()).load(userHeadPicImagePath).placeholder(R.drawable.no_img).into(img_photo);

            db = x.getDb(CustomApplication.getInstance().getDaoConfig());
            initMyActivity();

            //刚进到 我的 页面时,从消息数据库中取数据
            initMessage();
        }
    }

    private void initMyActivity() {

        initLikeActivity();

        initParticipateActivity();

        initLaunchActivity();

    }

    /**
     * 初始化 我喜欢的活动 的数量
     */
    private void initLikeActivity() {
        try {
            url = ConstantValue.GET_LOVED_ACTIVITY_LIST + "?userName=" + URLEncoder.encode(userName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("url:" + url);
        JsonObjectRequest likeRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        myLikeActivitySize = response.optJSONArray("result").length();
                        tv_favorite.setText(String.valueOf(myLikeActivitySize));
                        //Log.d(TAG, "onResponse: " + myLikeActivitySize);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        likeRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(likeRequest);
    }

    /**
     * 初始化 我报名的活动 的数量
     */
    private void initParticipateActivity() {
        try {
            url = ConstantValue.GET_PARTICIPATE_ACTIVITY_LIST + "?userName=" + URLEncoder.encode(userName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        myParticipateActivitySize = response.optJSONArray("result").length();
                        tv_enroll.setText(String.valueOf(myParticipateActivitySize));
                        //Log.d(TAG, "onResponse: " + myParticipateActivitySize);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    /**
     * 初始化 我发起的活动 的数量
     */
    private void initLaunchActivity() {
        try {
            url = ConstantValue.GET_PUBLISH_ACTIVITY_LIST + "?userName=" + URLEncoder.encode(userName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        myLaunchActivitySize = response.optJSONArray("result").length();
                        tv_launch.setText(String.valueOf(myLaunchActivitySize));
                        //Log.d(TAG, "onResponse: " + myLaunchActivitySize);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    private void initMessage2() {

        try {
            List<DbSystemMessage> systemList = db.selector(DbSystemMessage.class).findAll();
            List<DbReceivedComment> commentList = db.selector(DbReceivedComment.class).findAll();
            List<DbReceivedLike> likeList = db.selector(DbReceivedLike.class).findAll();
            db.delete(systemList);
            db.delete(commentList);
            db.delete(likeList);
        } catch (DbException e) {
            e.printStackTrace();
        }

        url1 = ConstantValue.urlRoot + ConstantValue.GET_SYSTEM_MESSAGE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request1 = new JsonObjectRequest(url1, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "GET_SYSTEM_MESSAGE onResponse: " + response);

                        JSONArray result = response.optJSONArray("result");
                        if (result != null && result.length() > 0) {
                            systemMessageList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbSystemMessage.class);

                            for (DbSystemMessage systemMessage : systemMessageList) {
                                try {
                                    db.save(systemMessage);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                systemMessageList = db.selector(DbSystemMessage.class).where("handleOrNot", "=", "0").findAll();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request1.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request1);      //加入请求队列


        url2 = ConstantValue.urlRoot + ConstantValue.GET_COMMENT_MESSAGE;
        JsonObjectRequest request2 = new JsonObjectRequest(url2, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "GET_COMMENT_MESSAGE onResponse: " + response);

                        JSONArray result = response.optJSONArray("result");
                        if (result != null && result.length() > 0) {
                            receivedCommentList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbReceivedComment.class);

                            for (DbReceivedComment receivedComment : receivedCommentList) {
                                try {
                                    db.save(receivedComment);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                receivedCommentList = db.selector(DbReceivedComment.class).where("handleOrNot", "=", "0").findAll();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request2.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request2);      //加入请求队列


        url3 = ConstantValue.urlRoot + ConstantValue.GET_LIKE_MESSAGE;
        JsonObjectRequest request3 = new JsonObjectRequest(url3, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "GET_LIKE_MESSAGE onResponse: " + response);

                        JSONArray result = response.optJSONArray("result");
                        if (result != null && result.length() > 0) {
                            receivedLikeList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbReceivedLike.class);

                            for (DbReceivedLike receivedLike : receivedLikeList) {
                                try {
                                    db.save(receivedLike);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                receivedLikeList = db.selector(DbReceivedLike.class).where("handleOrNot", "=", "0").findAll();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request3.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request3);      //加入请求队列

        setIcon();
    }

    /**
     * 设置 未读消息 的数量
     */
    private void setIcon() {
        systemMessageSize = systemMessageList == null ? 0 : systemMessageList.size();
        receivedReviewSize = receivedCommentList == null ? 0 : receivedCommentList.size();
        receivedLikeSize = receivedLikeList == null ? 0 : receivedLikeList.size();
        count = systemMessageSize + receivedReviewSize + receivedLikeSize;

        //Log.e(TAG, "systemMessageSize:" + systemMessageSize + " receivedReviewSize:" + receivedReviewSize + " receivedLikeSize:" + receivedLikeSize );

        if (count > 0) {
            iv_circle.setVisibility(View.VISIBLE);
            tv_message_nums.setText(String.valueOf(count));

        } else {
            iv_circle.setVisibility(View.INVISIBLE);
            tv_message_nums.setText("");
        }
        ((MainActivity) getActivity()).setIconVisibleOrInvisible(count);
    }

    private void initMessage() {
        try {
            systemMessageList = db.selector(DbSystemMessage.class).where("handleOrNot", "=", "0").findAll();
            receivedCommentList = db.selector(DbReceivedComment.class).where("handleOrNot", "=", "0").findAll();
            receivedLikeList = db.selector(DbReceivedLike.class).where("handleOrNot", "=", "0").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        setIcon();
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
        if (!"0".equals(userName)) {
            init();
        }
    }
}
