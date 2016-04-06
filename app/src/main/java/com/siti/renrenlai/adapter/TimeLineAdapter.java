package com.siti.renrenlai.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.TimeLineModel;
import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.List;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private List<TimeLineModel> mFeedList;

    public TimeLineAdapter(List<TimeLineModel> feedList) {
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

        holder.name.setText("name：" + timeLineModel.getName() + "\ntimeLineModel.getAge() age：" + timeLineModel.getAge());

    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    public static class TimeLineViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TimelineView mTimelineView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tx_name);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            mTimelineView.initLine(viewType);
        }

    }

}
