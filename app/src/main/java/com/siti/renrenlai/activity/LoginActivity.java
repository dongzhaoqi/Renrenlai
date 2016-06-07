package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.User;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Dong on 2016/3/22.
 */
public class LoginActivity extends BaseActivity implements OnClickListener{

    private Button btn_sign_in;
    private EditText et_email;
    private EditText et_password;
    private String str_email,str_password;
    private User user;
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
        et_password = (EditText) findViewById(R.id.et_password);
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
        showProcessDialog("登录中");
        String url = null;
        try {
            url = ConstantValue.USER_LOGIN + "?userName="+ URLEncoder.encode(str_email, "utf-8")+"&password="+str_password;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("login:" + url);
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginResponse", "response:" + response.toString());
                        loginSuccess(response);
                        dismissProcessDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        dismissProcessDialog();
                }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    private void loginSuccess(JSONObject arg0) {
        JSONObject result = arg0.optJSONObject("result");
        int code = arg0.optInt("errorCode");
        if (code == 0) {
            user = com.alibaba.fastjson.JSONObject.parseObject(result.toString(), User.class);
            Log.d("TAG", "loginSuccess: " + user);
            ((CustomApplication) getApplication()).setUser(user);
            startAnimActivity(MainActivity.class);
            finish();
            saveUser();
            /*SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(this, "login"),
                    "user", result.toString());
            SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(this, "login"),
                    "userName", user.getUserName());*/
            showToast("登录成功");
        }else{
            showToast("登录失败,请检查用户名或密码");
        }

    }

    private void saveUser() {
        /*SharedPreferencesUtil.writeObject(SharedPreferencesUtil.getSharedPreference(this, "login"),
                "user", user);*/
        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(this, "login"),
                "userName", user.getUserName());
        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(this, "login"),
                "userHeadPicImagePath", user.getUserHeadPicImagePath());
        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(this, "login"),
                "gender", user.getSex());
        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(this, "login"),
                "interetsAndHobbies", user.getInteretsAndHobbies());
        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(this, "login"),
                "intro", user.getInteretsAndHobbies());
        SharedPreferencesUtil.writeInt(SharedPreferencesUtil.getSharedPreference(this, "login"),
                "userId", user.getUserId());
    }
}
