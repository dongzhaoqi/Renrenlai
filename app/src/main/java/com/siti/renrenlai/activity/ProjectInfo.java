package com.siti.renrenlai.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.CommentAdapter;
import com.siti.renrenlai.adapter.ImageAdapter;
import com.siti.renrenlai.bean.ActivityImage;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.dialog.CommentDialog;
import com.siti.renrenlai.util.SharedPreferencesUtil;
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
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dong on 2016/5/27.
 */
public class ProjectInfo extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.activity_img)
    ImageView activity_img;
    @Bind(R.id.activity_name)
    TextView tv_avtivity_name;
    @Bind(R.id.layout_contact) RelativeLayout layout_contact;
    @Bind(R.id.tv_activity_address) TextView tv_activity_address;
    @Bind(R.id.tv_activity_time) TextView tv_activity_time;
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
    @Bind(R.id.btn_favor) Button btnFavor;
    @Bind(R.id.btn_publish) Button btnPublish;

    String projectName, telephone, projectAddress, projectDescrip, projectTime;
    int projectId;
    String userName;
    boolean isFavorPressed = false;
    private List<LovedUsers> lovedUsersList;            //所有喜欢的用户的头像
    private List<CommentContents> commentsList;         //评论列表
    private List<ActivityImage> imageList;              //活动封面
    private ArrayList<String> imagePath;
    private Project project;
    private ImageAdapter picAdapter;
    private CommentAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_info);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        imagePath = new ArrayList<>();
        initTopBarForBoth("项目详情", R.drawable.share, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showShare();
            }
        });
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(ProjectInfo.this, "login"), "userName");
        project = (Project) getIntent().getExtras().getSerializable("project");

        if(project != null){
            projectName = project.getProjectName();
            telephone = project.getTelephone();
            projectAddress = project.getProjectAddress();
            projectDescrip = project.getProjectDescrip();
            projectTime = project.getBeginTimeOfProject().substring(0, 16) + " - " + project.getEndTimeOfProject().substring(0, 16);
            lovedUsersList = project.getLovedImages();
            commentsList = project.getCommentList();
        }


        btnFavor.setText("喜欢(" + (lovedUsersList.size()) + ")");
        for(int i = 0; i < lovedUsersList.size(); i++){
            CircleImageView image = new CircleImageView(this);
            String imagePath = lovedUsersList.get(i).getUserHeadPicImagePath().replace("\\", "");
            //image.setBorderColorResource(R.color.colorPrimary);
            //image.setBorderWidth(2);
            System.out.println("imagePath:" + imagePath);
            Picasso.with(this).load(imagePath).resize(96, 96).into(image);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 15, 0);
            ll_image.addView(image, params);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectInfo.this, FavoriteActivity.class);
                    intent.putExtra("likeList", (Serializable) lovedUsersList);
                    startActivity(intent);

                }
            });
        }

        expTv1.setText(projectDescrip);
        tv_avtivity_name.setText(projectName);
        tv_activity_address.setText(projectAddress);
        tv_activity_time.setText(projectTime);
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

    @OnClick({R.id.layout_contact, R.id.btn_comment, R.id.btn_favor, R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_contact:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + telephone));
                startActivity(intent);
                break;
            case R.id.btn_comment:
                showCommentDialog();
                break;
            case R.id.btn_favor:
                if (!isFavorPressed) {
                    btnFavor.setSelected(true);         //喜欢
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
     * @param commentsList
     * @param position
     */
    public void showCommentDialog(List<CommentContents> commentsList, int position){
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
    public void showCommentDialog(){
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
     * 点赞,将头像添加到最前面
     */
    public void addImage(){
        CircleImageView image = new CircleImageView(this);
        Picasso.with(this).load(R.drawable.arduino).resize(96, 96).into(image);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 15, 0);
        ll_image.addView(image, 0, params);
        btnFavor.setText("喜欢(" + (lovedUsersList.size()+1) + ")");
    }

    /**
     * 取消点赞,去除头像
     */
    public void removeImage(){
        CircleImageView image = new CircleImageView(this);
        Picasso.with(this).load(R.drawable.arduino).resize(48, 48).into(image);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 15, 0);
        ll_image.removeViewAt(0);
        btnFavor.setText("喜欢(" + lovedUsersList.size() + ")");
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
        oks.setText(projectName);
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

