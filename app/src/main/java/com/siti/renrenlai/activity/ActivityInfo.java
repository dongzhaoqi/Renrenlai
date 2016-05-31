package com.siti.renrenlai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.CommentAdapter;
import com.siti.renrenlai.adapter.ImageAdapter;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.bean.ActivityImage;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.dialog.CommentDialog;
import com.siti.renrenlai.util.Bimp;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;
import com.siti.renrenlai.view.NoScrollGridView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dong on 2016/3/22.
 */
public class ActivityInfo extends BaseActivity implements OnClickListener {

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
    @Bind(R.id.expand_text_view)
    ExpandableTextView expTv1;
    @Bind(R.id.ll_image)
    LinearLayout ll_image;
    @Bind(R.id.list_comment)
    RecyclerView list_comment;
    @Bind(R.id.detail_scrollgridview)
    NoScrollGridView noScrollGridView;
    @Bind(R.id.btn_comment)
    Button btnComment;
    @Bind(R.id.btn_favor)
    Button btnFavor;
    @Bind(R.id.btn_publish)
    Button btnPublish;

    Activity activity;
    String activity_title, contact_tel, activity_address, activity_describ, activity_time;
    int activity_id, userId;
    boolean isFavorPressed = false;
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
        imagePath = new ArrayList<>();
        userId = SharedPreferencesUtil.readInt(SharedPreferencesUtil.getSharedPreference(this, "login"), "userId");
        activity = (Activity) getIntent().getExtras().getSerializable("activity");

        if (activity != null) {
            activity_id = activity.getActivityId();
            activity_title = activity.getActivityName();
            contact_tel = activity.getactivityReleaserTel();
            activity_address = activity.getActivityAddress();
            activity_describ = activity.getactivityDetailDescrip();
            activity_time = activity.getActivityStartTime().substring(0, 16) + " - " + activity.getActivityEndTime().substring(0, 16);
            imageList = activity.getActivityImages();
            lovedUsersList = activity.getLovedUsers();
            commentsList = activity.getComments();
        }


        initTopBarForBoth("活动详情", R.drawable.share, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                CommonUtils.showShare(ActivityInfo.this, activity_title);
            }
        });

        for (int i = 0; i < imageList.size(); i++) {
            String path = imageList.get(i).getActivityImagePath();
            System.out.println("info path:" + path);
            imagePath.add(path);
        }
        if (imagePath != null && imagePath.size() > 0) {
            Picasso.with(this).load(imagePath.get(0)).into(activity_img);
        } else {
            Picasso.with(this).load(R.drawable.no_img).into(activity_img);
        }
        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        picAdapter = new ImageAdapter(this, imagePath);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ActivityInfo.this, "i:" + imagePath.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityInfo.this, GalleryActivity.class);
                intent.putStringArrayListExtra("imagePath", imagePath);
                intent.putExtra("ID", i);
                startActivity(intent);

            }
        });
        noScrollGridView.setAdapter(picAdapter);

        btnFavor.setText("喜欢(" + (lovedUsersList.size()) + ")");
        for (int i = 0; i < lovedUsersList.size(); i++) {
            CircleImageView image = new CircleImageView(this);
            String imagePath = lovedUsersList.get(i).getUserHeadPicImagePath().replace("\\", "");
            //image.setBorderColorResource(R.color.colorPrimary);
            //image.setBorderWidth(2);
            System.out.println("imagePath:" + imagePath);
            Picasso.with(this).load(imagePath).resize(96, 96).into(image);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 15, 0);
            ll_image.addView(image, params);

            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityInfo.this, FavoriteActivity.class);
                    intent.putExtra("likeList", (Serializable) lovedUsersList);
                    startActivity(intent);

                }
            });
        }

        expTv1.setText(activity_describ);
        tv_avtivity_name.setText(activity_title);
        tv_activity_address.setText(activity_address);
        tv_activity_time.setText(activity_time);
        //Picasso.with(this).load(activity.getActivityImg()).into(activity_img);

        mAdapter = new CommentAdapter(this, commentsList);
        mAdapter.setOnItemClickListener(new CommentAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                int pos = Integer.parseInt(data.toString());
                showCommentDialog(commentsList, pos);

            }
        });
        layoutManager = new LinearLayoutManager(this);
        list_comment.setLayoutManager(layoutManager);
        list_comment.setAdapter(mAdapter);
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
                showCommentDialog();
                break;
            case R.id.btn_favor:
                if (!isFavorPressed) {
                    btnFavor.setSelected(true);         //喜欢
                    like();
                } else {
                    btnFavor.setSelected(false);        //取消喜欢
                    removeImage();
                }
                isFavorPressed = !isFavorPressed;
                break;
            case R.id.btn_publish:
                break;
        }
    }

    /**
     * 点击某个人的评论，弹出评论框，进行回复
     *
     * @param commentsList
     * @param position
     */
    public void showCommentDialog(List<CommentContents> commentsList, int position) {
        CommentDialog dialog = new CommentDialog(this, position);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.5f;
        dialog.setCommentList(commentsList, position);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 点击评论按钮，弹出评论框
     */
    public void showCommentDialog() {
        CommentDialog dialog = new CommentDialog(this);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.5f;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 喜欢该活动
     */
    public void like() {
        addImage();

        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        String api = null;
        try {
            api = "/loveThisActivityForApp?userName=" + URLEncoder.encode(userName, "utf-8") + "&activityId=" + activity_id;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = ConstantValue.urlRoot + api;
        System.out.println("url:" + url);
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
        CustomApplication.getInstance().addToRequestQueue(req);
    }


    /**
     * 点赞,将头像添加到最前面
     */
    public void addImage() {
        CircleImageView image = new CircleImageView(this);
        Picasso.with(this).load(R.drawable.arduino).resize(96, 96).into(image);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 15, 0);
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
