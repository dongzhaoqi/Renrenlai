package com.siti.renrenlai.ui;

import android.os.Bundle;

import com.siti.renrenlai.R;

/**
 * Created by Dong on 2016/3/22.
 */
public class LoginActivity extends TitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
    }
}
