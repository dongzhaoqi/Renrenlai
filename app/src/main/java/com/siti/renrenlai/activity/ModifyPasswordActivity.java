package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/3/31.
 */
public class ModifyPasswordActivity extends BaseActivity {


    @Bind(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.btn_reset_password)
    Button btnResetPassword;
    String telephone, password;
    private static final String TAG = "ModifyPasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("修改密码");
        telephone = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "telephone");
        password = etNewPassword.getText().toString();
    }

    @OnClick(R.id.btn_reset_password)
    public void onClick() {
        showProcessDialog();
        String url = ConstantValue.UPDATE_USER_PASSWORD;
        JSONObject json = new JSONObject();
        try {
            json.put("telephone", telephone);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        int errorCode = response.optInt("errorCode");
                        if(errorCode == 0){
                            Toast.makeText(ModifyPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }else if(errorCode == 1){
                            Toast.makeText(ModifyPasswordActivity.this, "手机号未注册", Toast.LENGTH_SHORT).show();
                        }
                        dismissProcessDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProcessDialog();
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }
}
