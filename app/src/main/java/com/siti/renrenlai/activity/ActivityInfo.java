package com.siti.renrenlai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.CommentAdapter;
import com.siti.renrenlai.adapter.ImageAdapter;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.bean.ActivityImage;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.dialog.CommentDialog;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.ExpandableTextView;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;
import com.siti.renrenlai.view.NoScrollGridView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by Dong on 2016/3/22.
 */
public class ActivityInfo extends BaseActivity implements OnClickListener {

    @Bind(R.id.scroll)
    ScrollView scrollView;
    @Bind(R.id.activity_img)
    ImageView activity_img;
    @Bind(R.id.activity_name)
    TextView tv_avtivity_name;
    @Bind(R.id.layout_fund)
    RelativeLayout layoutFund;
    @Bind(R.id.layout_contact)
    RelativeLayout layout_contact;
    @Bind(R.id.tv_activity_address)
    TextView tv_activity_address;
    @Bind(R.id.tv_activity_time)
    TextView tv_activity_time;
    /*@Bind(R.id.expandable_text)
    RichEditor expandable_text;*/
    @Bind(R.id.webview)
    WebView webview;
    /*@Bind(R.id.expand_text_view)
    ExpandableTextView expTv1;*/
    @Bind(R.id.ll_image)
    LinearLayout ll_image;
    @Bind(R.id.rl_comment)
    RelativeLayout rl_comment;
    @Bind(R.id.list_comment)
    RecyclerView list_comment;
    @Bind(R.id.empty_view)
    TextView empty_view;
    @Bind(R.id.empty_view_like)
    TextView empty_view_like;
    /*@Bind(R.id.detail_scrollgridview)
    NoScrollGridView noScrollGridView;*/
    @Bind(R.id.btn_comment)
    Button btnComment;
    @Bind(R.id.btn_favor)
    Button btnFavor;
    @Bind(R.id.btn_participate)
    Button btnParticipate;

    Activity activity;
    String activity_title, contact_tel, activity_address, activity_describ, activity_time;
    String userName, userHeadImgePath;
    String url;             //接口地址
    int activity_id, userId;
    boolean lovedIs, signUpIs, isFavorPressed = false;
    private List<LovedUsers> lovedUsersList;            //所有喜欢的用户的头像
    private List<CommentContents> commentsList;         //评论列表
    private List<ActivityImage> imageList;              //活动封面
    private ArrayList<String> imagePath;

    private ImageAdapter picAdapter;
    private CommentAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        list_comment.setNestedScrollingEnabled(false);      //若不加上此句,ScrollView 嵌套RecylerView 会导致滑动不流畅
        imagePath = new ArrayList<>();
        userId = SharedPreferencesUtil.readInt(SharedPreferencesUtil.getSharedPreference(this, "login"), "userId");
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        userHeadImgePath = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userHeadPicImagePath");

        activity = (Activity) getIntent().getExtras().getSerializable("activity");

        if (activity != null) {
            lovedIs = activity.isLovedIs();
            signUpIs = activity.isSignUpIs();
            activity_id = activity.getActivityId();
            activity_title = activity.getActivityName();
            contact_tel = activity.getActivityReleaserTel();
            activity_address = activity.getActivityAddress();
            activity_describ = activity.getActivityDetailDescrip();
            activity_time = activity.getActivityStartTime().substring(0, 16) + " - " + activity.getActivityEndTime().substring(0, 16);
            imageList = activity.getActivityImages();
            lovedUsersList = activity.getLovedUsers();
            commentsList = activity.getCommentContents();
        }

        initTopBarForBoth("活动详情", R.drawable.share, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                CommonUtils.showShare(ActivityInfo.this, activity_title);
            }
        });

        if (lovedIs) {
            btnFavor.setText("已喜欢(" + (lovedUsersList.size()) + ")");
            btnFavor.setSelected(true);
        } else {
            btnFavor.setText("喜欢(" + (lovedUsersList.size()) + ")");
        }
        if (signUpIs) {
            btnParticipate.setText("已报名");
            btnParticipate.setBackgroundColor(Color.parseColor("#E5E5E5"));
            btnParticipate.setClickable(false);
        }
        if (imageList != null && imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                String path = imageList.get(i).getActivityImagePath();
                System.out.println("ActivityInfo path:" + path);
                imagePath.add(path);
            }
        }
        if (imagePath != null && imagePath.size() > 0) {
            Picasso.with(this).load(imagePath.get(0)).into(activity_img);
        } else {
            Picasso.with(this).load(R.drawable.no_img).into(activity_img);
        }
        /*noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        if (imagePath.size() == 0) {
            empty_view_like.setVisibility(View.VISIBLE);
            ll_image.setVisibility(View.GONE);
        } else {
            picAdapter = new ImageAdapter(this, imagePath);
            noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ActivityInfo.this, GalleryImageActivity.class);
                    intent.putStringArrayListExtra("imagePath", imagePath);
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            });
            noScrollGridView.setAdapter(picAdapter);
        }*/
        btnComment.setText("评论(" + (commentsList.size()) + ")");

        if(lovedUsersList.size() == 0){
            empty_view_like.setVisibility(View.VISIBLE);
            ll_image.setVisibility(View.GONE);
        }

        for (int i = 0; i < lovedUsersList.size(); i++) {
            CircleImageView image = new CircleImageView(this);
            String imagePath = lovedUsersList.get(i).getUserHeadPicImagePath().replace("\\", "");
            //image.setBorderColorResource(R.color.colorPrimary);
            //image.setBorderWidth(2);
            System.out.println("imagePath:" + imagePath);
            Picasso.with(this).load(imagePath).resize(96, 96).into(image);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 0, 0, 0);
            ll_image.addView(image, params);

            /*image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityInfo.this, ParticipateActivity.class);
                    intent.putExtra("likeList", (Serializable) lovedUsersList);
                    startActivity(intent);

                }
            });*/
        }
        Display display = getWindowManager().getDefaultDisplay();
        double width=display.getWidth() * 0.3;
        activity_describ += "<head><style type='text/css'>"
                +"img{margin:auto auto;display:block;} </style></head>"
                +"<body>";
        for(int i = 1; i < imagePath.size(); i++){
            activity_describ += "<br><img src='" + imagePath.get(i) + "' width='" + width + "'/>" +
                    "";
        }
        activity_describ += "</body>";
        webview.loadData(activity_describ,"text/html; charset=UTF-8", null);
        webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        //expandable_text.setHtml(activity_describ);
        //expTv1.setText(activity_describ);
        tv_avtivity_name.setText(activity_title);
        tv_activity_address.setText(activity_address);
        tv_activity_time.setText(activity_time);
        //Picasso.with(this).load(activity.getActivityImg()).into(activity_img);

        mAdapter = new CommentAdapter(this, commentsList);

        if (commentsList.size() == 0) {
            empty_view.setVisibility(View.VISIBLE);
            list_comment.setVisibility(View.GONE);
            return;
        }
        mAdapter.setOnItemClickListener(new CommentAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                int pos = Integer.parseInt(data.toString());
                showCommentDialog(view, mAdapter, commentsList, pos);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        list_comment.setLayoutManager(layoutManager);
        list_comment.setAdapter(mAdapter);
    }

    @OnClick({R.id.layout_fund, R.id.layout_contact, R.id.btn_comment, R.id.btn_favor, R.id.btn_participate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_fund:
                break;
            case R.id.layout_contact:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + contact_tel));
                startActivity(intent);
                break;
            case R.id.btn_comment:
                showCommentDialog(mAdapter);
                break;
            case R.id.btn_favor:
                if (userName.equals("0")) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    if (!lovedIs) {
                        like();
                        lovedIs = true;
                        btnFavor.setSelected(true);         //喜欢
                        btnFavor.setText("已喜欢(" + (lovedUsersList.size() + 1) + ")");
                    } else {
                        Toast.makeText(ActivityInfo.this, "已经喜欢!", Toast.LENGTH_SHORT).show();
                        //btnFavor.setSelected(false);        //取消喜欢
                        //removeImage();
                        //btnFavor.setText("喜欢(" + (lovedUsersList.size()) + ")");
                    }
                }
                break;
            case R.id.btn_participate:
                if (userName.equals("0")) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    participate(activity_id);
                }
                break;
        }
    }

    /**
     * 报名活动
     *
     * @param activity_id 活动的id
     */
    private void participate(int activity_id) {
        JSONObject jsonObject = new JSONObject();
        System.out.println("userName:" + userName + " activityId:" + activity_id);
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("activityId", activity_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url = ConstantValue.PARTICIPATE_ACTIVITY;
        System.out.println("url:" + url);
        JsonObjectRequest req = new JsonObjectRequest(url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("活动报名response", response.toString());
                        showToast("报名成功!");
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("活动报名Error: ", "error:" + error.getMessage());
                showToast("出错了!");
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);

    }

    /**
     * 点击某个人的评论，弹出评论框，进行回复
     *
     * @param commentsList
     * @param position
     */
    public void showCommentDialog(View view, CommentAdapter mAdapter, List<CommentContents> commentsList, int position) {
        scrollToSpecifiedComment(view);
        CommentDialog dialog = new CommentDialog(this, mAdapter);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.5f;
        dialog.setCommentList(commentsList, position, activity_id);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 点击评论按钮，弹出评论框
     */
    public void showCommentDialog(CommentAdapter mAdapter) {
        scrollToComment();
        CommentDialog dialog = new CommentDialog(this, mAdapter, commentsList, activity_id);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.5f;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 点击评论按钮，屏幕滚动到评论处
     */
    public void scrollToComment(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, rl_comment.getBottom());
            }
        });
    }

    /**
     * 点击某个人的评论，将屏幕滚动到该评论下方
     * @param view 要评论的item
     */
    public void scrollToSpecifiedComment(final View view){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, rl_comment.getBottom() + view.getTop());
                //((LinearLayoutManager) list_comment.getLayoutManager()).scrollToPositionWithOffset(view.getBottom(), 0);
            }
        });
    }

    /**
     * 喜欢该活动
     */
    public void like() {
        addImage();
        url = null;
        try {
            url = ConstantValue.LOVE_THIS_ACTIVITY + "?userName=" + URLEncoder.encode(userName, "utf-8") + "&activityId=" + activity_id;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //System.out.println("url:" + url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d("response", response.toString());
                        showToast("修改成功!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                showToast("出错了!");
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }


    /**
     * 点赞,将头像添加到最前面
     */
    public void addImage() {
        CircleImageView image = new CircleImageView(this);
        Picasso.with(this).load(userHeadImgePath).placeholder(R.drawable.no_img).resize(96, 96).into(image);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 0, 0);
        ll_image.addView(image, 0, params);
        btnFavor.setText("喜欢(" + (lovedUsersList.size() + 1) + ")");
    }

    /**
     * 取消点赞,去除头像
     */
    public void removeImage() {
        CircleImageView image = new CircleImageView(this);
        Picasso.with(this).load(R.drawable.arduino).resize(48, 48).into(image);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 15, 0);
        ll_image.removeViewAt(0);
        btnFavor.setText("喜欢(" + lovedUsersList.size() + ")");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

}
