package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.view.View;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

/**
 * Created by Dong on 2016/4/12.
 */
public class ApplyWishActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);
        initView();
        initEvent();
    }

    private void initView(){
        initTopBarForBoth("申请意愿", "暂存", new onRightImageButtonClickListener() {
            @Override
            public void onClick() {

            }
        });


    }

    private void initEvent(){

    }

    @Override
    public void onClick(View v) {

    }
}
