package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dong on 2016/3/22.
 */
public class LoginActivity extends BaseActivity implements OnClickListener{

    private Button btn_sign_in;
    private EditText et_email;
    private EditText et_password;
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

        String api = "/login?userName="+str_email+"&password="+str_password;
        String url = ConstantValue.urlRoot + api;
        /*JSONObject json = new JSONObject();
        try {
            json.put("userName", str_email);
            json.put("password", str_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d("response", response.toString());
                        loginSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                }
        });

        CustomApplication.getInstance().addToRequestQueue(req);
    }

    private void loginSuccess(JSONObject arg0) {
        JSONObject result = arg0.optJSONObject("result");
        int code = arg0.optInt("errorCode");
        if (code == 0) {
            User user = new User();
            user.setUserName(result.optString("userName"));
            ((CustomApplication) getApplication()).setUser(user);
            startAnimActivity(MainActivity.class);
            SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(this, "login"),
                    "userName", user.getUserName());
            showToast("登录成功");
        }else{
            showToast("登录失败,请检查用户名或密码");
        }

    }
}
