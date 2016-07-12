package com.siti.renrenlai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.ParticipateUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dong on 2016/5/19.
 */
public class ParticipateAdapter extends RecyclerView.Adapter<ParticipateAdapter.ViewHolder> {

    private Context mContext;
    private List<ParticipateUser> participateUserList;

    public ParticipateAdapter(Context context, List<ParticipateUser> list) {
        this.mContext = context;
        this.participateUserList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_participate, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParticipateUser participateUser = participateUserList.get(position);
        Picasso.with(mContext).load(participateUser.getUserHeadPicImagePath()).into(holder.iv_user_head);
        holder.tv_username.setText(participateUser.getRealName());
        holder.tv_tel.setText(participateUser.getTelephone());
    }

    @Override
    public int getItemCount() {
        return participateUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_user_head)
        CircleImageView iv_user_head;
        @Bind(R.id.tv_username)
        TextView tv_username;
        @Bind(R.id.tv_tel)
        TextView tv_tel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
