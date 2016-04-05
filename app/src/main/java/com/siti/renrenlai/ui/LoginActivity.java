package com.siti.renrenlai.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.User;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplcation;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.xwray.passwordview.PasswordView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dong on 2016/3/22.
 */
public class LoginActivity extends BaseActivity implements OnClickListener{

    private Button btn_sign_in;
    private EditText et_email;
    private PasswordView et_password;
    private String str_email,str_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initTopBarForTitleAndRight("登录","注册",new OnRightButtonClickListener(){
            @Override
            public void onClick() {
                super.onClick();
                startAnimActivity(RegisterActivity.class);
            }
        });
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
            showToast("用户名不能为空");
        }else if(TextUtils.isEmpty(str_password)){
            showToast("密码不能为空");
        }else{
            doLogin();
        }
    }

    private void doLogin() {

        /*String url = "/login";

        JSONObject json = new JSONObject();
        try {
            json.put("userName", str_email);
            json.put("password", str_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest("http://www.baidu.com", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d("response", response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                }
        });

        CustomApplcation.getInstance().addToRequestQueue(req);*/

        User user = new User();
        user.setUserName(str_email);
        showToast("登录成功");
        startAnimActivity(MainActivity.class);
        SharedPreferencesUtil.writeString(SharedPreferencesUtil
                        .getSharedPreference(getApplicationContext(), "login"),
                "userName", user.getUserName());
        finish();
    }
}
