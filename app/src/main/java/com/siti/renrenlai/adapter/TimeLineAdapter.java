package com.siti.renrenlai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.TimeLineModel;
import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private Context mContext;
    private List<TimeLineModel> mFeedList;

    public TimeLineAdapter(Context context, List<TimeLineModel> feedList) {
        this.mContext = context;
        mFeedList = feedList;
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
        holder.tvTime.setText(timeLineModel.getTime());
        holder.tvTitle.setText(timeLineModel.getTitle());
        if(position % 3 == 2){
            holder.tvPic.setImageResource(R.drawable.desert);
            holder.btnProgress.setText(R.string.txt_already_end);
            holder.btnProgress.setBackgroundColor(mContext.getResources().getColor(R.color.end));
        }else if(position % 3 == 1){
            holder.tvPic.setImageResource(R.drawable.django_python);
            holder.btnProgress.setText(R.string.txt_close);
            holder.btnProgress.setBackgroundColor(mContext.getResources().getColor(R.color.end));
        }
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    public static class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.time_marker) TimelineView mTimelineView;
        @Bind(R.id.tv_time) TextView tvTime;
        @Bind(R.id.tv_pic) ImageView tvPic;
        @Bind(R.id.tv_title) TextView tvTitle;
        @Bind(R.id.btn_progress) Button btnProgress;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTimelineView.initLine(viewType);
        }

    }

}
