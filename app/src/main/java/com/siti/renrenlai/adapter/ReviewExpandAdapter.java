package com.siti.renrenlai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/5/20.
 */
public class ReviewExpandAdapter extends AnimatedExpandableListAdapter {

    private LayoutInflater inflater;
    private List<ReviewGroup> items;
    public ReviewExpandAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<ReviewGroup> items) {
        this.items = items;
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public ReviewChild getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).items.get(childPosition);
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
            convertView = inflater.inflate(R.layout.group_review_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.str_review);
            holder.expand_imgView = (ImageView) convertView.findViewById(R.id.iv_expand);
            convertView.setTag(holder);
        }else{
            holder = (GroupHolder) convertView.getTag();
        }

        if(!isExpanded){
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_expand_small_holo_light);
        }else{
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_collapse_small_holo_light);
        }

        holder.title.setText(R.string.str_review);
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        ReviewChild item = getChild(groupPosition, childPosition);
        if(convertView == null){
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.child_review_item, parent, false);
            holder.iv_review_icon = (ImageView) convertView.findViewById(R.id.iv_review_icon);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_review = (TextView) convertView.findViewById(R.id.tv_review);
            holder.tv_review_time = (TextView) convertView.findViewById(R.id.tv_review_time);
            holder.iv_activity_img = (ImageView) convertView.findViewById(R.id.iv_activity_img);
            holder.tv_activity_name = (TextView) convertView.findViewById(R.id.tv_activity_name);
            convertView.setTag(holder);
        }else{
            holder = (ChildHolder) convertView.getTag();
        }

        holder.iv_review_icon.setImageResource(R.drawable.system_alarm);
        holder.tv_username.setText(item.username);
        holder.tv_review.setText(item.review);
        holder.tv_review_time.setText(item.review_time);
        holder.tv_activity_name.setText(item.activity_name);

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).items.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ReviewGroup {
        public String title;
        public List<ReviewChild> items = new ArrayList<ReviewChild>();
    }

    public static class ReviewChild {
        public String username;
        public String review;
        public String review_time;
        public String activity_name;
    }

    public static class GroupHolder {
        TextView title;
        ImageView expand_imgView;
    }

    public static class ChildHolder {
        ImageView iv_review_icon;
        TextView tv_username;
        TextView tv_review;
        TextView tv_review_time;
        ImageView iv_activity_img;
        TextView tv_activity_name;

    }
}
