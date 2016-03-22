package com.siti.renrenlai.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class LoginActivity extends AppCompatActivity implements OnClickListener{

    private Button btn_sign_in;
    private EditText et_email;
    private PasswordView et_password;
    private String str_email,str_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");

        initViews();
    }

    private void initViews() {

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (PasswordView) findViewById(R.id.et_password);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_sign_in){
            attemptLogin();
        }
    }

    private void attemptLogin() {

        str_email = et_email.getText().toString();
        str_password = et_password.getText().toString();

        if(TextUtils.isEmpty(str_email)) {
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_password)){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            doLogin();
        }
    }

    private void doLogin() {

        User user = new User();
        user.setUserName(str_email);

        SharedPreferencesUtil.writeString(SharedPreferencesUtil
                        .getSharedPreference(getApplicationContext(), "login"),
                "userName", user.getUserName());

    }
}
