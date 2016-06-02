package com.siti.renrenlai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.util.ConstantValue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/5/26.
 */
public class FundIntroAdapter extends RecyclerView.Adapter<FundIntroAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Project> projectList;
    private List<CommentContents> commentList;
    private DisplayImageOptions options;
    private ImageLoader loader;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Object data);
    }

    public FundIntroAdapter(Context context, List<Project> list) {
        this.mContext = context;
        this.projectList = list;
        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(android.R.drawable.ic_delete)
                .showImageForEmptyUri(android.R.drawable.ic_menu_share)
                .showImageOnFail(R.drawable.icon_me)
                .cacheInMemory()
                .cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(2))
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_project, parent, false);
        //将创建的View注册点击事件
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project project = projectList.get(position);
        commentList = project.getCommentList();
        loader.displayImage(ConstantValue.urlRoot + project.getProjectImagePath(), holder.ivProject, options,
                animateFirstListener);
        //Picasso.with(mContext).load(project.getProjectImagePath()).into(holder.ivProject);
        holder.tvProjectName.setText(project.getProjectName());
        holder.tvCommentsNumber.setText(project.getCommentCount());
        holder.tvLikeNumber.setText(project.getLovedCount());
        if(commentList != null && commentList.size() >= 2) {
            holder.tvUsername1.setText(commentList.get(0).getUserName());
            holder.tvUsername2.setText(commentList.get(1).getUserName());
            holder.tvComment1.setText(commentList.get(0).getCommentContent());
            holder.tvComment2.setText(commentList.get(1).getCommentContent());
        }
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(project.getProjectId());
    }

    @Override
    public int getItemCount() {
        return projectList == null ? 0 : projectList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @OnClick(R.id.rl_project)
    public void onClick() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_project) ImageView ivProject;
        @Bind(R.id.tv_project_name) TextView tvProjectName;
        @Bind(R.id.tv_username1) TextView tvUsername1;
        @Bind(R.id.tv_comment1) TextView tvComment1;
        @Bind(R.id.ll_comment1) LinearLayout llComment1;
        @Bind(R.id.tv_username2) TextView tvUsername2;
        @Bind(R.id.tv_comment2) TextView tvComment2;
        @Bind(R.id.ll_comment2) LinearLayout llComment2;
        @Bind(R.id.tv_comments_number) TextView tvCommentsNumber;
        @Bind(R.id.tv_like_number) TextView tvLikeNumber;
        @Bind(R.id.rl_project) RelativeLayout rlProject;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 图片加载监听事件,当图片首次在屏幕上显示时,有一个淡入效果
     **/
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500); //设置image隐藏动画500ms
                    displayedImages.add(imageUri); //将图片uri添加到集合中
                }
            }
        }
    }
}
