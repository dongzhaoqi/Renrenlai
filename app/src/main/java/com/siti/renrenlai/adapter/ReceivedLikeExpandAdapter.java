package com.siti.renrenlai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.db.ReceivedLike;
import com.siti.renrenlai.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/5/20.
 */
public class ReceivedLikeExpandAdapter extends AnimatedExpandableListAdapter {

    private LayoutInflater inflater;
    private List<ReceivedLikeGroup> receivedLikeGroupList;
    private List<ReceivedLike> receivedLikeList;
    private Context mContext;

    public ReceivedLikeExpandAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<ReceivedLikeGroup> receivedLikeGroupList, List<ReceivedLike> receivedLikeList) {
        this.receivedLikeGroupList = receivedLikeGroupList;
        this.receivedLikeList = receivedLikeList;
    }

    @Override
    public int getGroupCount() {
        return receivedLikeGroupList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return receivedLikeGroupList.get(groupPosition);
    }

    @Override
    public ReceivedLikeChild getChild(int groupPosition, int childPosition) {
        return receivedLikeGroupList.get(groupPosition).receivedLikeChildList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if(convertView == null){
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.group_received_like_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.str_like);
            holder.tv_message_nums = (TextView) convertView.findViewById(R.id.tv_message_nums);
            holder.iv_circle = (ImageView) convertView.findViewById(R.id.iv_circle);
            holder.expand_imgView = (ImageView) convertView.findViewById(R.id.iv_expand);
            convertView.setTag(holder);
        }else{
            holder = (GroupHolder) convertView.getTag();
        }

        if(receivedLikeList != null && receivedLikeList.size() > 0 ){
            holder.iv_circle.setVisibility(View.VISIBLE);
            holder.tv_message_nums.setText(String.valueOf(receivedLikeList.size()));
        }

        if(!isExpanded){
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_expand_small_holo_light);
        }else{
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_collapse_small_holo_light);
        }

        holder.title.setText(R.string.str_like);
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        ReceivedLikeChild item = getChild(groupPosition, childPosition);
        if(convertView == null){
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.child_received_like_item, parent, false);
            holder.iv_user_icon = (ImageView) convertView.findViewById(R.id.iv_user_icon);
            holder.tv_received_time = (TextView) convertView.findViewById(R.id.tv_received_time);
            holder.iv_activity_img = (ImageView) convertView.findViewById(R.id.iv_activity_img);
            holder.tv_activity_name = (TextView) convertView.findViewById(R.id.tv_activity_name);
            convertView.setTag(holder);
        }else{
            holder = (ChildHolder) convertView.getTag();
        }

        holder.iv_user_icon.setImageResource(R.drawable.system_alarm);
        holder.tv_received_time.setText(item.received_time);
        holder.tv_activity_name.setText(item.activity_name);

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return receivedLikeGroupList.get(groupPosition).receivedLikeChildList.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ReceivedLikeGroup {
        public String title;
        public List<ReceivedLikeChild> receivedLikeChildList = new ArrayList<ReceivedLikeChild>();
    }

    public static class ReceivedLikeChild {
        public String received_time;
        public String activity_name;
    }

    public static class GroupHolder {
        TextView title;
        TextView tv_message_nums;
        ImageView iv_circle;
        ImageView expand_imgView;
    }

    public static class ChildHolder {
        ImageView iv_user_icon;
        ImageView iv_activity_img;
        TextView tv_received_time;
        TextView tv_activity_name;

    }
}
