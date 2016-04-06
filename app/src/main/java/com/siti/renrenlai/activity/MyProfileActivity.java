package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.siti.renrenlai.R;

/**
 * Created by Dong on 2016/3/22.
 */
public class MyProfileActivity extends BaseActivity implements OnClickListener{

    private RelativeLayout layout_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("我的资料");

        layout_password = (RelativeLayout) findViewById(R.id.layout_password);
        layout_password.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_password:
                startAnimActivity(ModifyPasswordActivity.class);
                break;
        }
    }

}
