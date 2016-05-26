package com.siti.renrenlai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.Project;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/5/26.
 */
public class FundIntroAdapter extends RecyclerView.Adapter<FundIntroAdapter.ViewHolder> {

    private Context mContext;
    private List<Project> projectList;

    public FundIntroAdapter(Context context, List<Project> list) {
        this.mContext = context;
        this.projectList = list;
        android.util.Log.d("TAG", "FundIntroAdapter() returned: " +  projectList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_project, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project project = projectList.get(position);
        Log.d("TAG", "onBindViewHolder() returned: " + project.getProjectId());
        Log.d("TAG", "getProjectImagePath() returned: " + project.getProjectImagePath());
        Log.d("TAG", "getProjectName() returned: " + project.getProjectName());
        Log.d("TAG", "getCommentCount() returned: " + project.getCommentCount());
        Log.d("TAG", "getLovedCount() returned: " + project.getLovedCount());
        Log.d("TAG", "getUserName1() returned: " + project.getUserName1());
        Log.d("TAG", "getUserName2() returned: " + project.getUserName2());
        Log.d("TAG", "getComment1() returned: " + project.getComment1());
        Log.d("TAG", "getComment2() returned: " + project.getComment2());

        Picasso.with(mContext).load(project.getProjectImagePath()).into(holder.ivProject);
        holder.tvProjectName.setText(project.getProjectName());
        holder.tvCommentsNumber.setText(project.getCommentCount());
        holder.tvLikeNumber.setText(project.getLovedCount());
        holder.tvUsername1.setText(project.getUserName1());
        holder.tvUsername2.setText(project.getUserName2());
        holder.tvComment1.setText(project.getComment1());
        holder.tvComment2.setText(project.getComment2());
    }

    @Override
    public int getItemCount() {
        return projectList == null ? 0 : projectList.size();
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
}
