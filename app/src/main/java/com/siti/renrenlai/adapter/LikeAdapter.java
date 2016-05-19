package com.siti.renrenlai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.LovedUsers;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dong on 2016/5/19.
 */
public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {

    private Context mContext;
    private List<LovedUsers> likeList;

    public LikeAdapter(Context context, List<LovedUsers> list) {
        this.mContext = context;
        this.likeList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_like, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LovedUsers like = likeList.get(position);
        Picasso.with(mContext).load(like.getUserHeadPicImagePath()).into(holder.ivLike);
        holder.tvLike.setText(like.getUserId());
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_like)
        CircleImageView ivLike;
        @Bind(R.id.tv_like)
        TextView tvLike;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
