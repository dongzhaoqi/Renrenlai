package com.siti.renrenlai.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.MyLaunchActivity;
import com.siti.renrenlai.bean.TimeLineModel;
import com.siti.renrenlai.util.ConstantValue;
import com.squareup.picasso.Picasso;
import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    @Bind(R.id.layout_activity_item)
    RelativeLayout layout_activity_item;
    private static Context mContext;
    private static List<TimeLineModel> mFeedList;
    static int position;
    int activityStatus;     //活动状态 1.报名中 2.审核通过3.报名截止4.活动结束

    public TimeLineAdapter(Context context, List<TimeLineModel> feedList, int position) {
        this.mContext = context;
        this.mFeedList = feedList;
        this.position = position;
    }

    public TimeLineAdapter(Context context, List<TimeLineModel> feedList) {
        this.mContext = context;
        this.mFeedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_favorite, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        TimeLineModel timeLineModel = mFeedList.get(position);
        holder.itemView.setTag(position);
        if(timeLineModel.getDateTimeForActiv() != null){
            holder.tvTime.setText(timeLineModel.getDateTimeForActiv().substring(0, 10));
        }
        holder.tvTitle.setText(timeLineModel.getActivityName());
        if (timeLineModel.getActivityImageList() != null && timeLineModel.getActivityImageList().size() > 0){
            //System.out.println("TimeLine:" + ConstantValue.urlRoot + timeLineModel.getActivityImageList().get(0).getActivityImagePath());
            Picasso.with(mContext).load(ConstantValue.urlRoot + timeLineModel.getActivityImageList().get(0).getActivityImagePath()).into(holder.tvPic);
        }
        activityStatus = timeLineModel.getActivityStatus();
        if(activityStatus == 1){
            holder.btnProgress.setText(R.string.txt_enrolling);
            holder.btnProgress.setBackgroundColor(mContext.getResources().getColor(R.color.start));
        }else if(activityStatus == 3){
            holder.btnProgress.setText(R.string.txt_close);
            holder.btnProgress.setBackgroundColor(mContext.getResources().getColor(R.color.end));
        }else{
            holder.btnProgress.setText(R.string.txt_already_end);
            holder.btnProgress.setBackgroundColor(mContext.getResources().getColor(R.color.end));
        }
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    public static class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.time_marker)
        TimelineView mTimelineView;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_pic)
        ImageView tvPic;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.btn_progress)
        Button btnProgress;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    TimeLineModel model = mFeedList.get(Integer.parseInt(v.getTag().toString()));
                    bundle.putSerializable("model", model);
                    bundle.putInt("position", position);
                    mContext.startActivity(new Intent(mContext, MyLaunchActivity.class).putExtras(bundle));
                }
            });

            mTimelineView.initLine(viewType);
        }

    }

}
