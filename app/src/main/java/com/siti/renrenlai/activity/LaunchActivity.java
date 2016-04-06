package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.siti.renrenlai.R;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by Dong on 2016/3/29.
 */

public class LaunchActivity extends BaseActivity {

    private EditText et_subject, et_end, et_place, et_detail;
    private TextView et_time;
    private Button btn_preview, btn_publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("发起活动");

        et_subject = (EditText) findViewById(R.id.et_subject);
        et_time = (TextView) findViewById(R.id.et_time);
        et_end = (EditText) findViewById(R.id.et_end);
        et_place = (EditText) findViewById(R.id.et_place);
        et_detail = (EditText) findViewById(R.id.et_detail);

        btn_preview = (Button) findViewById(R.id.btn_preview);
        btn_publish = (Button) findViewById(R.id.btn_publish);

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(LaunchActivity.this).create();
                dialog.show();
                DatePicker picker = new DatePicker(LaunchActivity.this);
                picker.setDate(2015, 10);
                picker.setTodayDisplay(true);
                picker.setHolidayDisplay(false);
                picker.setMode(DPMode.SINGLE);
                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(String date) {
                        et_time.setText(date);
                        dialog.dismiss();
                    }
                });
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setContentView(picker, params);
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });

    }



}