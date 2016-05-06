package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/3/22.
 */
public class IntroductionActivity extends BaseActivity{

    @Bind(R.id.et_intro) EditText et_intro;
    @Bind(R.id.tv_len) TextView tv_len;
    private static int MODIFY_INTRO = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {

        String intro = getIntent().getStringExtra("intro");
        et_intro.setText(intro);
        tv_len.setText(et_intro.getText().length() + "/50");
        initTopBarForBoth("个人简介", "保存", new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                String intro = et_intro.getText().toString();
                SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(getApplicationContext(), "login"),
                        "intro", intro);
                Intent intent = new Intent();
                intent.putExtra("intro", intro);
                setResult(MODIFY_INTRO, intent);
                finish();
            }
        });

        et_intro.addTextChangedListener(watcher);

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
            tv_len.setText(et_intro.getText().length() + "/50");
        }
    };

}