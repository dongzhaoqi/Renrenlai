package com.siti.renrenlai.activity;

import android.os.Bundle;

import com.siti.renrenlai.R;

/**
 * Created by Dong on 2016/3/29.
 */

public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("消息");


    }



}