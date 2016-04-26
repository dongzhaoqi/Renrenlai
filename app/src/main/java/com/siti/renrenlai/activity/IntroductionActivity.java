package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/3/22.
 */
public class IntroductionActivity extends BaseActivity{

    @Bind(R.id.et_intro) EditText etIntro;
    @Bind(R.id.tv_len) TextView tv_len;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        initTopBarForBoth("个人简介", "保存", new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showToast("保存");
            }
        });

        etIntro.addTextChangedListener(watcher);
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("TAG", "onTextChanged()");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            Log.d("TAG", "beforeTextChanged()");
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("TAG", "afterTextChanged()");
            tv_len.setText(etIntro.getText().length() + "/50");
        }
    };

}