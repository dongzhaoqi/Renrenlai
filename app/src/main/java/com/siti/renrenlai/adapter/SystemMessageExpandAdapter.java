package com.siti.renrenlai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.db.SystemMessage;
import com.siti.renrenlai.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/5/20.
 */
public class SystemMessageExpandAdapter extends AnimatedExpandableListAdapter implements View.OnClickListener{

    private LayoutInflater inflater;
    private List<MessageGroup> systemMessageGroupList;
    private List<SystemMessage> systemMessageList;
    private OnChildItemClickListener mOnChildItemClickListener;
    private Context mContext;

    public SystemMessageExpandAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setData(List<MessageGroup> items, List<SystemMessage> systemMessageList) {
        this.systemMessageGroupList = items;
        this.systemMessageList = systemMessageList;
    }

    @Override
    public int getGroupCount() {
        return systemMessageGroupList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return systemMessageGroupList.get(groupPosition);
    }

    @Override
    public MessageChild getChild(int groupPosition, int childPosition) {
        return systemMessageGroupList.get(groupPosition).systemMessageChildList.get(childPosition);
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

        if(systemMessageGroupList.size() > 0 ){
            holder.iv_circle.setVisibility(View.VISIBLE);
            holder.tv_message_nums.setText(String.valueOf(systemMessageList.size()));
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
    public View getRealChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        MessageChild messageChild = getChild(groupPosition, childPosition);
        if(convertView == null){
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.child_system_item, parent, false);
            holder.ll_system_message = (LinearLayout) convertView.findViewById(R.id.ll_system_message);
            holder.iv_system_icon = (ImageView) convertView.findViewById(R.id.iv_system_icon);
            holder.tv_system_message = (TextView) convertView.findViewById(R.id.system_message);
            holder.iv_activity_img = (ImageView) convertView.findViewById(R.id.iv_activity_img);
            holder.tv_activity_name = (TextView) convertView.findViewById(R.id.tv_activity_name);
            convertView.setTag(holder);
        }else{
            holder = (ChildHolder) convertView.getTag();
        }
        convertView.setOnClickListener(this);
        //holder.iv_system_icon.setImageResource(R.drawable.system_alarm);

        holder.ll_system_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "position:" + childPosition, Toast.LENGTH_SHORT).show();
            }
        });
        holder.tv_system_message.setText(messageChild.message);
        holder.tv_activity_name.setText(messageChild.activity_name);

        return convertView;
    }


    @Override
    public int getRealChildrenCount(int groupPosition) {
        return systemMessageGroupList.get(groupPosition).systemMessageChildList.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onClick(View v) {
        if(mOnChildItemClickListener != null){
            mOnChildItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public static class MessageGroup {
        public String title;
        public List<MessageChild> systemMessageChildList = new ArrayList<MessageChild>();
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
        LinearLayout ll_system_message;
        ImageView iv_system_icon;
        TextView tv_system_message;
        ImageView iv_activity_img;
        TextView tv_activity_name;

    }

    public static interface OnChildItemClickListener {
        void onItemClick(View view , Object data);
    }

    public void setOnChildItemClickListener(OnChildItemClickListener listener) {
        this.mOnChildItemClickListener = listener;
    }
}
