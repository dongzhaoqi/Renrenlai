package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.siti.renrenlai.R;

/**
 * Created by Dong on 2016/3/31.
 */
public class ModifyPasswordActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("修改密码");

    }

}
