package com.siti.renrenlai.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.User;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.xwray.passwordview.PasswordView;

/**
 * Created by Dong on 2016/3/22.
 */
public class RegisterActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initTopBarForLeft("注册");
        initViews();
    }

    private void initViews() {


    }


}