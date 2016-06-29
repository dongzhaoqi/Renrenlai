package com.siti.renrenlai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.ActivityImagePre;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<ActivityImagePre> imagePathList;
    private List<ActivityImagePre> selectedDataList;

    public MyGridViewAdapter(Context pContext, List<ActivityImagePre> imagePathList) {
        this.mContext = pContext;
        this.imagePathList = imagePathList;
    }

    @Override
    public int getCount() {
        return imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ActivityImagePre image = imagePathList.get(position);
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.plugin_camera_select_imageview, parent, false);

            viewHolder.grid_img_item = (ImageView) convertView
                    .findViewById(R.id.image_view);
            viewHolder.toggleButton = (ToggleButton) convertView
                    .findViewById(R.id.toggle_button);
            viewHolder.choosetoggle = (Button) convertView
                    .findViewById(R.id.choosedbt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(mContext).load(image.getActivityImagePath()).into(viewHolder.grid_img_item);
        viewHolder.toggleButton.setTag(position);
        viewHolder.choosetoggle.setTag(position);
        viewHolder.toggleButton.setOnClickListener(new ToggleClickListener(viewHolder.choosetoggle));
        /*if (selectedDataList.contains(imagePathList.get(position))) {
            viewHolder.toggleButton.setChecked(true);
            viewHolder.choosetoggle.setVisibility(View.VISIBLE);
        } else {
            viewHolder.toggleButton.setChecked(false);
            viewHolder.choosetoggle.setVisibility(View.GONE);
        }*/
        return convertView;
    }

    private class ToggleClickListener implements View.OnClickListener {
        Button chooseBt;
        public ToggleClickListener(Button choosebt){
            this.chooseBt = choosebt;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (imagePathList != null && mOnItemClickListener != null
                        && position < imagePathList.size()) {
                    mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(),chooseBt);
                }
            }
        }
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        public void onItemClick(ToggleButton view, int position,
                                boolean isChecked, Button chooseBt);
    }

    private static class ViewHolder {
        ImageView grid_img_item;
        public ToggleButton toggleButton;
        public Button choosetoggle;
    }

}