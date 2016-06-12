package com.siti.renrenlai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.Project;
import com.siti.renrenlai.bean.ProjectBaseInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/5/5.
 */
public class SpinnerProjectAdapter extends BaseAdapter{

    private List<ProjectBaseInfo> projects;
    private Context mContext;
    private LayoutInflater inflater;

    public SpinnerProjectAdapter(Context context, List<ProjectBaseInfo> list) {
        this.mContext = context;
        this.projects = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return projects == null ? 0 : projects.size();
    }

    @Override
    public Object getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ProjectBaseInfo project = projects.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_project_name, parent, false);
            holder = new ViewHolder();
            holder.tv_project_name = (TextView) convertView
                    .findViewById(R.id.tv_project_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_project_name.setText(project.getProjectName());
        return convertView;
    }



    public static class ViewHolder{
        TextView tv_project_name;
    }
}
