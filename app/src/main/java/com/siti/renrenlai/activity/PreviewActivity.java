package com.siti.renrenlai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ImageAdapter;
import com.siti.renrenlai.adapter.PictureAdapter;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.view.NoScrollGridView;
import com.siti.renrenlai.view.PictureAndTextEditorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/5/19.
 */
public class PreviewActivity extends BaseActivity {

    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.tv_subject)
    TextView tvSubject;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.tv_deadline)
    TextView tvDeadline;
    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_people)
    TextView tvPeople;
    @Bind(R.id.rl_project)
    RelativeLayout rl_project;
    @Bind(R.id.tv_project) TextView tvProject;
    @Bind(R.id.noScrollgridview)
    NoScrollGridView noScrollGridView;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    int activity_type;
    private ImageAdapter picAdapter;
    private static final String TAG = "PreviewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        initTopBarForLeft("预览活动");

        initView();
    }

    private void initView() {
        Activity activity = (Activity) getIntent().getSerializableExtra("activity");
        ArrayList<String> images = getIntent().getStringArrayListExtra("images");

        for(String str: images){
            Log.d(TAG, "initView: " + str);
        }
        activity_type = activity.getActivityType();
        if(activity_type == 1){
            tvCategory.setText("兴趣");
        }else if(activity_type == 2){
            tvCategory.setText("公益");
        }else{
            tvCategory.setText("议事");
        }
        tvSubject.setText(activity.getActivityName());
        tvStartTime.setText(activity.getActivityStartTime());
        tvEndTime.setText(activity.getActivityEndTime());
        tvDeadline.setText(activity.getDeadline());
        tvPlace.setText(activity.getActivityAddress());
        tvPeople.setText(activity.getParticipateNum());
        tvDetail.setText(activity.getActivityDetailDescrip());
        /*List<String> detail = new ArrayList<>();
        String[] descrip = activity.getActivityDetailDescrip().split(" ");
        for(String str : descrip){
            if(str.contains("/") && str.contains(".")){
                str += "&";
            }
            detail.add(str);
            Log.e(TAG, "detail: "+ str);
        }
        etDetail.setmContentList(detail);
        etDetail.setFocusable(false);*/
        if(activity.getProjectName() != null){
            tvProject.setText(activity.getProjectName());
        }else{
            rl_project.setVisibility(View.GONE);
        }
        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        picAdapter = new ImageAdapter(this, images);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PreviewActivity.this, GalleryActivity.class);
                intent.putExtra("ID", i);
                startActivity(intent);
            }
        });
        noScrollGridView.setAdapter(picAdapter);
    }

}
