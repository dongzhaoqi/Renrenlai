package com.siti.renrenlai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.CommentContents;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dong on 2016/5/5.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements View.OnClickListener{

    private List<CommentContents> comments;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Object data);
    }

    public CommentAdapter(Context context, List<CommentContents> list) {
        this.mContext = context;
        this.comments = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentContents comment = comments.get(position);
        Picasso.with(mContext).load(comment.getUserHeadPicImagePath()).placeholder(R.drawable.no_img).into(holder.ivUserHead);
        holder.tvUsername.setText(comment.getUserName());
        holder.tvCommentTime.setText(comment.getCommentTime());
        holder.tvComment.setText(comment.getCommentContent());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_user_head) CircleImageView ivUserHead;
        @Bind(R.id.tv_username) TextView tvUsername;
        @Bind(R.id.tv_comment) TextView tvComment;
        @Bind(R.id.tv_comment_time) TextView tvCommentTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
