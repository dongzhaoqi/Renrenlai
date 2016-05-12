package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/4/26.
 */
public class EditNameActivity extends BaseActivity {

    @Bind(R.id.et_modify_name) EditText etModifyName;
    private static int MODIFY_NAME = 2;
    String userName, newName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        ButterKnife.bind(this);
        initTopBarForBoth("编辑昵称", "保存", new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                editName();
            }
        });
    }

    public void editName(){
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        newName = etModifyName.getText().toString();

        String api = null;
        try {
            api = "/updateUserName?userName="+userName+"&userNameChange="+ URLEncoder.encode(newName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = ConstantValue.urlRoot + api;

        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d("response", response.toString());
                        showToast("修改成功!");
                        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(getApplicationContext(), "login"),
                                "userName", newName);
                        Intent intent = new Intent();
                        intent.putExtra("nickName", newName);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                showToast("出错了!");
            }
        });
        CustomApplication.getInstance().addToRequestQueue(req);

    }
}
