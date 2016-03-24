package com.siti.renrenlai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.ItemBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Object> itemList;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private ImageLoader loader;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ImageAdapter(Context context, ArrayList<Object> itemList) {
        this.mContext = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);

        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(android.R.drawable.ic_delete)
        .showImageForEmptyUri(android.R.drawable.ic_menu_share)
        .showImageOnFail(R.drawable.icon_me)
        .cacheInMemory()
        .cacheOnDisc()
        .displayer(new RoundedBitmapDisplayer(100))
        .build();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_layout, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemBean bean = (ItemBean) itemList.get(position);
        loader.displayImage(bean.getImg(), holder.img, options,
                animateFirstListener);
        holder.tv.setText(bean.getTv());

        return convertView;
    }

    static class ViewHolder{
        private ImageView img;
        private TextView tv;
    }

    /**图片加载监听事件,当图片首次在屏幕上显示时,有一个淡入效果**/
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