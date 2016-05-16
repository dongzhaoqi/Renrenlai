package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/4/5.
 */
public class FeedbackActivity extends BaseActivity {

    @Bind(R.id.et_feedback) EditText etFeedback;
    String userName, feedBackInfoContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        initTopBarForBoth("反馈", "发送", new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                doFeedBack();
            }
        });
    }

    public void doFeedBack(){
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        feedBackInfoContent = etFeedback.getText().toString();

        String api = "/insertFeedBackInfo";
        String url = ConstantValue.urlRoot + api;

        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("feedBackContent", feedBackInfoContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "response:" + response.toString());
                        String result = null;
                        try {
                            result = response.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showToast(result);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                showToast("出错了!");
            }
        });

        CustomApplication.getInstance().addToRequestQueue(req);
    }
}
