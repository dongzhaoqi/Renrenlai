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
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.bean.ProjectImage;
import com.siti.renrenlai.dialog.CommentDialog;
import com.siti.renrenlai.util.CommonUtils;
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

    String projectName, projectImagePath, telephone, projectAddress, projectDescrip, projectTime;
    int projectId;
    String userName;
    boolean isFavorPressed = false;
    private List<LovedUsers> lovedUsersList;            //所有喜欢的用户的头像
    private List<CommentContents> commentsList;         //评论列表
    private List<ProjectImage> imageList;              //活动封面
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
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(ProjectInfo.this, "login"), "userName");
        project = (Project) getIntent().getExtras().getSerializable("project");
        if(project != null){
            projectName = project.getProjectName();
            projectImagePath = project.getProjectImagePath();
            telephone = project.getTelephone();
            projectAddress = project.getProjectAddress();
            projectDescrip = project.getProjectDescrip();
            projectTime = project.getBeginTimeOfProject().substring(0, 16) + " - " + project.getEndTimeOfProject().substring(0, 16);
            lovedUsersList = project.getLovedImages();
            imageList = project.getProjectImageList();
            commentsList = project.getCommentList();
        }

        initTopBarForBoth("项目详情", R.drawable.share, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                CommonUtils.showShare(ProjectInfo.this, projectName);
            }
        });

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
        if(imageList != null && imageList.size() > 0){
            System.out.println("aaaa:" + imageList.get(0).getProjectImagePath());
            Picasso.with(this).load(imageList.get(0).getProjectImagePath()).into(activity_img);
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }


}
