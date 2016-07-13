package com.siti.renrenlai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.City;
import com.siti.renrenlai.bean.City;

import java.util.List;

/**
 * Created by Dong on 2016/5/5.
 */
public class CityAdapter extends BaseAdapter{

    private List<City> cityList;
    private Context mContext;
    private LayoutInflater inflater;

    public CityAdapter(Context context, List<City> list) {
        this.mContext = context;
        this.cityList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cityList == null ? 0 : cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        City city = cityList.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_project_name, parent, false);
            holder = new ViewHolder();
            holder.tv_project_name = (TextView) convertView
                    .findViewById(R.id.tv_project_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_project_name.setText(city.getAreaName());
        return convertView;
    }



    public static class ViewHolder{
        TextView tv_project_name;
    }
}
