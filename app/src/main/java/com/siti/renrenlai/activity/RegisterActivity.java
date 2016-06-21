package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.AddressInitTask;
import com.siti.renrenlai.util.AddressInitTask.AsyncResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/3/22.
 */
public class RegisterActivity extends BaseActivity {

    //@Bind(R.id.tv_city) TextView tvCity;
    @Bind(R.id.btn_sign_up) Button btnSignUp;
    @Bind(R.id.et_username) EditText etUsername;
    @Bind(R.id.et_password) EditText etPassword;
    @Bind(R.id.et_confirm_password) EditText etConfirmPassword;

    private static final String TAG = "RegisterActivity";
    int i = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initTopBarForLeft("注册");

    }


    @OnClick({R.id.btn_sign_up})
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.tv_city:
                AddressInitTask task = new AddressInitTask(this, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        tvCity.setText(output);
                    }
                });
                task.execute("上海", "上海市", "浦东新区");
                break;*/

            case R.id.btn_sign_up:
                break;
        }
    }


    protected void onDestroy() {
        // 销毁回调监听接口
        super.onDestroy();
    }
}
