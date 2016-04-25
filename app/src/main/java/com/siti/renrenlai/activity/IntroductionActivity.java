package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout;

/**
 * Created by Dong on 2016/3/22.
 */
public class IntroductionActivity extends BaseActivity implements OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        initViews();
    }

    private void initViews() {
        initTopBarForBoth("个人简介", "保存", new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showToast("保存");
            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

}
