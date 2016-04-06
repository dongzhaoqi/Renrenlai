package com.siti.renrenlai.activity;

import android.os.Bundle;

import com.siti.renrenlai.R;

/**
 * Created by Dong on 2016/3/22.
 */
public class RegisterActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initTopBarForLeft("注册");
        initViews();
    }

    private void initViews() {


    }


}
