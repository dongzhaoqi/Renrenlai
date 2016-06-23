package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/3/22.
 */
public class RegisterActivity extends BaseActivity {

    //@Bind(R.id.tv_city) TextView tvCity;
    @Bind(R.id.btn_sign_up)
    Button btnSignUp;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_confirm_password)
    EditText etConfirmPassword;
    String telphone, userName, password, confirmPassword;
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
                register();
                break;
        }
    }

    private void register() {

        telphone = getIntent().getStringExtra("tel");
        userName = etUsername.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();
        if(!password.equals(confirmPassword)){
            Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }else if(!CommonUtils.isValidPassword(password)){
            Toast.makeText(RegisterActivity.this, "请输入六位以上的纯数字密码", Toast.LENGTH_SHORT).show();
            return;
        }

        showProcessDialog("正在注册...");
        String url = ConstantValue.USER_REGISTER;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telephone", telphone);
            jsonObject.put("userName", userName);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("register:" + url + " tel:" + telphone + " userName:" + userName +
                " password:" + password);
        JsonObjectRequest req = new JsonObjectRequest(url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        registerSuccess(response);
                        dismissProcessDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                dismissProcessDialog();
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    private void registerSuccess(JSONObject response) {
        String errorCode = response.optString("errorCode");
        if ("0".equals(errorCode)) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        } else {
            Toast.makeText(RegisterActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onDestroy() {
        // 销毁回调监听接口
        super.onDestroy();
    }
}
