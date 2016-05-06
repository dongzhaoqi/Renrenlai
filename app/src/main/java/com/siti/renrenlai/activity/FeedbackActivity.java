package com.siti.renrenlai.activity;

import android.os.Bundle;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

/**
 * Created by Dong on 2016/4/5.
 */
public class FeedbackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
    }

    private void initView(){
        initTopBarForBoth("反馈", "发送", new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showToast("感谢您的反馈!");
            }
        });
    }
}
