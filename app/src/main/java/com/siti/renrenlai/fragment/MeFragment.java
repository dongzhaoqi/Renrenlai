package com.siti.renrenlai.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.FeedbackActivity;
import com.siti.renrenlai.activity.LoginActivity;
import com.siti.renrenlai.activity.MessageActivity;
import com.siti.renrenlai.activity.MyActivity;
import com.siti.renrenlai.activity.MyProfileActivity;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.FragmentBase;

/**
 * Created by Dong on 3/22/2016.
 */
public class MeFragment extends FragmentBase implements View.OnClickListener {

    private View view;
    private RelativeLayout layout_name;
    private LinearLayout layout_favorite, layout_enroll, layout_launch;
    private RelativeLayout layout_logout, layout_message, layout_feedback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_me,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        initTopBarForOnlyTitle("我的");
        layout_name = (RelativeLayout) findViewById(R.id.layout_name);
        layout_favorite = (LinearLayout) findViewById(R.id.layout_favorite);
        layout_enroll = (LinearLayout) findViewById(R.id.layout_enroll);
        layout_launch = (LinearLayout) findViewById(R.id.layout_launch);

        layout_logout = (RelativeLayout) findViewById(R.id.layout_logout);
        layout_message = (RelativeLayout) findViewById(R.id.layout_message);
        layout_feedback = (RelativeLayout) findViewById(R.id.layout_feedback);

        layout_name.setOnClickListener(this);
        layout_favorite.setOnClickListener(this);
        layout_enroll.setOnClickListener(this);
        layout_launch.setOnClickListener(this);

        layout_logout.setOnClickListener(this);
        layout_message.setOnClickListener(this);
        layout_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id){
            case R.id.layout_name:
                startAnimActivity(MyProfileActivity.class);
                break;
            case R.id.layout_favorite:
                intent = new Intent(getActivity(),MyActivity.class);
                intent.putExtra("pos",0);
                startAnimActivity(intent);
                break;
            case R.id.layout_enroll:
                intent = new Intent(getActivity(),MyActivity.class);
                intent.putExtra("pos",1);
                startAnimActivity(intent);
                break;
            case R.id.layout_launch:
                intent = new Intent(getActivity(),MyActivity.class);
                intent.putExtra("pos",2);
                startAnimActivity(intent);
                break;
            case R.id.layout_message:
                startAnimActivity(MessageActivity.class);
                break;
            case R.id.layout_logout:
                SharedPreferencesUtil.writeString(SharedPreferencesUtil
                                .getSharedPreference(getActivity(), "login"),
                        "userName", "0");
                startAnimActivity(LoginActivity.class);
                break;
            case R.id.layout_feedback:
                startAnimActivity(FeedbackActivity.class);
                break;
        }
    }
}
