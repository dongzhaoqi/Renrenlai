package com.siti.renrenlai.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

/**
 * Created by Dong on 2016/3/22.
 */
public class ActivityInfo extends BaseActivity implements OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
    }

    private void initViews() {
        initTopBarForBoth("活动详情", android.R.drawable.ic_menu_share, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {

            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

}
