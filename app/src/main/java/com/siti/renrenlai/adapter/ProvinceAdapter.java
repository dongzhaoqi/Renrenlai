package com.siti.renrenlai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.ProjectBaseInfo;
import com.siti.renrenlai.bean.Province;

import java.util.List;

/**
 * Created by Dong on 2016/5/5.
 */
public class ProvinceAdapter extends BaseAdapter{

    private List<Province> provinceList;
    private Context mContext;
    private LayoutInflater inflater;

    public ProvinceAdapter(Context context, List<Province> list) {
        this.mContext = context;
        this.provinceList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return provinceList == null ? 0 : provinceList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Province province = provinceList.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_project_name, parent, false);
            holder = new ViewHolder();
            holder.tv_project_name = (TextView) convertView
                    .findViewById(R.id.tv_project_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_project_name.setText(province.getAreaName());
        return convertView;
    }



    public static class ViewHolder{
        TextView tv_project_name;
    }
}
