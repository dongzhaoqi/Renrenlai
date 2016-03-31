package com.siti.renrenlai.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.CommonUtils;

/**
 * Created by Dong on 2016/3/29.
 */

public class LaunchActivity extends BaseActivity {

    private EditText et_subject, et_time, et_end, et_place, et_detail;
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
        et_time = (EditText) findViewById(R.id.et_time);
        et_end = (EditText) findViewById(R.id.et_end);
        et_place = (EditText) findViewById(R.id.et_place);
        et_detail = (EditText) findViewById(R.id.et_detail);

        btn_preview = (Button) findViewById(R.id.btn_preview);
        btn_publish = (Button) findViewById(R.id.btn_publish);

    }



}