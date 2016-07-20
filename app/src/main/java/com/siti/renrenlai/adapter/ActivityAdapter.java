package com.siti.renrenlai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.bean.ActivityImage;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> implements View.OnClickListener{

    private static List<Activity> activities;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private ImageLoader loader;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    static int pos = 0;
    private String imagePath;

    public ActivityAdapter(Context context, List<Activity> activities) {
        this.mContext = context;
        this.activities = activities;
        inflater = LayoutInflater.from(context);
        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(android.R.drawable.ic_delete)
                .showImageForEmptyUri(android.R.drawable.ic_menu_share)
                .showImageOnFail(R.drawable.icon_me)
                .cacheInMemory()
                .cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(2))
                .build();
    }

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Object data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        // 给ViewHolder设置布局文件
        //if(pos % 2 == 0) {
         //   v = inflater.inflate(R.layout.item_card_layout, viewGroup, false);
        //} else {
            v = inflater.inflate(R.layout.item_card_layout, viewGroup, false);
        //}
        pos++;
        ViewHolder vh = new ViewHolder(v);
        //将创建的View注册点击事件
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // 给ViewHolder设置元素
        Activity p = activities.get(i);
        if(p != null) {
            viewHolder.tv_title.setText(p.getActivityName());
            viewHolder.tv_time.setText(p.getActivityStartTime() + "开始");
            viewHolder.tv_address.setText(p.getActivityAddress());
            viewHolder.iv_cover.setTag(activities.get(i));
            List<ActivityImage> images = p.getActivityImages();
            if (images != null && images.size() > 0) {
                //System.out.println("images size:" + images.size());
                imagePath = p.getActivityImages().get(0).getActivityImagePath();
                //System.out.println("getActivityImages:" + imagePath);
            } else {
                //System.out.println("getActivityImages: null");
                imagePath = null;
            }
        }
        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(i);
        if (imagePath != null){
            //Picasso.with(mContext).load(imagePath).into(viewHolder.iv_cover);
            loader.displayImage(imagePath, viewHolder.iv_cover, options, animateFirstListener);
        }else{
            Picasso.with(mContext).load(R.drawable.no_img).into(viewHolder.iv_cover);
        }

        //viewHolder.itemView.setTag(p.getActivityId());
    }

    @Override
    public int getItemCount() {
        // 返回数据总数
        return activities == null ? 0 : activities.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_cover;
        public TextView tv_title;
        public TextView tv_time;
        public TextView tv_address;

        public ViewHolder(View v) {
            super(v);
            iv_cover = (ImageView) v.findViewById(R.id.iv_cover);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_address = (TextView) v.findViewById(R.id.tv_address);
        }
    }

    /**
     * 图片加载监听事件,当图片首次在屏幕上显示时,有一个淡入效果
     **/
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500); //设置image隐藏动画500ms
                    displayedImages.add(imageUri); //将图片uri添加到集合中
                }
            }
        }
    }
}