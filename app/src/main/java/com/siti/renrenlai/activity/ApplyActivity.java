package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.siti.renrenlai.R;

/**
 * Created by Dong on 2016/3/22.
 */
public class ApplyActivity extends BaseActivity implements OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("申请");

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

}
