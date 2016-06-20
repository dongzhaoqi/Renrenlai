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
public class SystemMessageExpandAdapter extends AnimatedExpandableListAdapter {

    private LayoutInflater inflater;
    private List<MessageGroup> items;

    public SystemMessageExpandAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<MessageGroup> items) {
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
    public MessageChild getChild(int groupPosition, int childPosition) {
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
            convertView = inflater.inflate(R.layout.group_system_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.str_system_alarm);
            holder.tv_message_nums = (TextView) convertView.findViewById(R.id.tv_message_nums);
            holder.iv_circle = (ImageView) convertView.findViewById(R.id.iv_circle);
            holder.expand_imgView = (ImageView) convertView.findViewById(R.id.iv_expand);
            convertView.setTag(holder);
        }else{
            holder = (GroupHolder) convertView.getTag();
        }

        if(items.size() > 0 ){
            holder.iv_circle.setImageResource(R.drawable.circle);
            holder.tv_message_nums.setTextColor(items.size());
        }
        if(!isExpanded){
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_expand_small_holo_light);
        }else{
            holder.expand_imgView.setBackgroundResource(R.drawable.ic_collapse_small_holo_light);
        }

        holder.title.setText(R.string.str_system_alarm);
        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        MessageChild item = getChild(groupPosition, childPosition);
        if(convertView == null){
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.child_system_item, parent, false);
            holder.iv_system_icon = (ImageView) convertView.findViewById(R.id.iv_system_icon);
            holder.tv_system_message = (TextView) convertView.findViewById(R.id.system_message);
            holder.iv_activity_img = (ImageView) convertView.findViewById(R.id.iv_activity_img);
            holder.tv_activity_name = (TextView) convertView.findViewById(R.id.tv_activity_name);
            convertView.setTag(holder);
        }else{
            holder = (ChildHolder) convertView.getTag();
        }

        //holder.iv_system_icon.setImageResource(R.drawable.system_alarm);
        holder.tv_system_message.setText(item.message);
        holder.tv_activity_name.setText(item.activity_name);
        //holder.iv_activity_img

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

    public static class MessageGroup {
        public String title;
        public List<MessageChild> items = new ArrayList<MessageChild>();
    }

    public static class MessageChild {
        public String icon_system;
        public String message;
        public String activity_image;
        public String activity_name;
    }

    public static class GroupHolder {
        TextView title;
        TextView tv_message_nums;
        ImageView iv_circle;
        ImageView expand_imgView;
    }

    public static class ChildHolder {
        ImageView iv_system_icon;
        TextView tv_system_message;
        ImageView iv_activity_img;
        TextView tv_activity_name;

    }
}
