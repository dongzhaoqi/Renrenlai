package com.siti.renrenlai.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.Bimp;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Hankkin on 2015/6/30.
 */
@SuppressLint("HandlerLeak")
public class PictureAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private boolean shape;
    private ArrayList<String> list;

    public boolean isShape() {
        return shape;
    }

    private Activity context;

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public PictureAdapter(Activity context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public PictureAdapter(Activity context, ArrayList<String> images) {
        this.context = context;
        this.list = images;
        inflater = LayoutInflater.from(context);

    }

    public int getCount() {
        if(list != null && list.size() > 0){
            if(list.get(0).contains("http")){
                return list.size();
            }
        }

        if (Bimp.tempSelectBitmap.size() == 9) {
            return 9;
        }
        if(list != null){
            return (Bimp.tempSelectBitmap.size());
        }
        return (Bimp.tempSelectBitmap.size() + 1);
    }


    public Object getItem(int arg0) {
        return 0;
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_singal_item,
                    parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == Bimp.tempSelectBitmap.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    convertView.getResources(), R.drawable.icon_add_pic_unfocused));
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else if (list != null) {
            if(list.get(position).contains("http")){
                Picasso.with(context).load(list.get(position)).into(holder.image);
            }else {
                File imgFile = new File(list.get(position));
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.image.setImageBitmap(myBitmap);
                }
            }
            if(position == list.size()+1){
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
        }


        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }
}