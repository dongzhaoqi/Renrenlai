package com.siti.renrenlai.ui;

import android.os.Bundle;

import com.siti.renrenlai.R;

/**
 * Created by Dong on 2016/3/29.
 */

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("发起活动");

    }

}