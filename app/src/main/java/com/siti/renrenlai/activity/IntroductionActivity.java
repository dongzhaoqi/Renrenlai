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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

        final String intro = getIntent().getStringExtra("intro");
        et_intro.setText(intro);
        tv_len.setText(et_intro.getText().length() + "/50");
        initTopBarForBoth("个人简介", "保存", new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                modifyIntroduction(et_intro.getText().toString());
            }
        });

        et_intro.addTextChangedListener(watcher);

    }

    public void modifyIntroduction(String intro){
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        String api = null;
        try {
            api = "/updateUserIntroduction?userName="+URLEncoder.encode(userName, "utf-8")+"&userIntroduction="+ URLEncoder.encode(intro, "utf-8");
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

        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(getApplicationContext(), "login"),
                "intro", intro);
        Intent intent = new Intent();
        intent.putExtra("intro", intro);
        IntroductionActivity.this.setResult(RESULT_OK, intent);
        IntroductionActivity.this.finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}