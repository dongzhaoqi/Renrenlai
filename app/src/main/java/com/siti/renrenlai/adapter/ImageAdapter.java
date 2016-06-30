package com.siti.renrenlai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.Bimp;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Dong on 2016/5/27.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> imageList;
    private LayoutInflater inflater;

    public ImageAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.imageList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(imageList.size() == 9)
            return 9;
        if(imageList != null)
            return imageList.size();
        return imageList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_singal_item, parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position == imageList.size()){
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    convertView.getResources(), R.drawable.icon_add_pic_unfocused));
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        }else{
            Picasso.with(mContext).load(imageList.get(position)).into(holder.image);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }
}
