package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/4/5.
 */
public class FeedbackActivity extends BaseActivity {

    @Bind(R.id.et_feedback) EditText etFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        initTopBarForBoth("反馈", "发送", new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showToast("感谢您的反馈!");
            }
        });
    }
}
