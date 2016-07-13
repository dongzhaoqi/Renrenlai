package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.CityAdapter;
import com.siti.renrenlai.adapter.ProvinceAdapter;
import com.siti.renrenlai.bean.City;
import com.siti.renrenlai.bean.Group;
import com.siti.renrenlai.bean.Province;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.AddressPicker;

/**
 * Created by Dong on 2016/3/22.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.btn_sign_up)
    Button btnSignUp;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_confirm_password)
    EditText etConfirmPassword;

    @Bind(R.id.spinner_province)
    Spinner spinnerProvince;
    @Bind(R.id.spinner_city)
    Spinner spinnerCity;
    @Bind(R.id.tv_community)
    AutoCompleteTextView tvCommunity;
    @Bind(R.id.login_form)
    ScrollView loginForm;
    String url, telphone, userName, password, confirmPassword;
    private static final String TAG = "RegisterActivity";
    private List<Province> provinceList;
    private List<City> cityList;
    private List<Group> groupList;
    private ArrayList<String> communities;              //所选择的城市下所有的小区
    private ArrayAdapter<String> arrayAdapter;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    int i = 60;
    int provinceId, cityId, groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initTopBarForLeft("注册");

        initProvince();

    }

    /**
     * 初始化省份
     */
    private void initProvince(){
        url = ConstantValue.GET_PROVINCE_LIST;
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        getProvince(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "initProvince onErrorResponse: " + error.getMessage() );
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    private void getProvince(JSONObject response){
        JSONArray result = response.optJSONArray("result");
        provinceList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), Province.class);

        provinceAdapter = new ProvinceAdapter(this, provinceList);
        spinnerProvince.setAdapter(provinceAdapter);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(RegisterActivity.this, "areaId:" + provinceList.get(position).getAreaId() + "areaName:" + provinceList.get(position).getAreaName(), Toast.LENGTH_SHORT).show();
                provinceId = provinceList.get(position).getAreaId();
                initCity(provinceId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 初始化城市
     */
    private void initCity(int provinceId){
        url = ConstantValue.GET_CITY_LIST;
        JSONObject json = new JSONObject();
        try {
            json.put("provinceId", provinceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "initCity:" + response.toString());
                        getCity(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage() );
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    private void getCity(JSONObject response){

        JSONArray result = response.optJSONArray("result");
        cityList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), City.class);
        cityAdapter = new CityAdapter(this, cityList);
        spinnerCity.setAdapter(cityAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(RegisterActivity.this, "areaId:" + cityList.get(position).getAreaId() + "areaName:" + cityList.get(position).getAreaName(), Toast.LENGTH_SHORT).show();
                cityId = cityList.get(position).getAreaId();
                initCommunity(cityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initCommunity(int cityId){
        url = ConstantValue.GET_GROUP_LIST + "/" + cityId;
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "initCommunity:" + response.toString());
                        getGroupList(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    private void getGroupList(JSONObject response){
        JSONArray result = response.optJSONArray("result");
        groupList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), Group.class);
        List<String> groups = new ArrayList<>();
        for (Group group : groupList){
            groups.add(group.getGroupProName());
        }
        arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1,groups);
        tvCommunity.setAdapter(arrayAdapter);
        tvCommunity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groupId = groupList.get(position).getGroupNameId();
            }
        });
        tvCommunity.setThreshold(1);
    }

    @OnClick({R.id.btn_sign_up})
    public void onClick(View view) {
        switch (view.getId()) {
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
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(RegisterActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return;
        } else if (!CommonUtils.isValidPassword(password)) {
            Toast.makeText(RegisterActivity.this, "请输入六位以上的纯数字密码", Toast.LENGTH_SHORT).show();
            return;
        }

        showProcessDialog("正在注册...");
        String url = ConstantValue.USER_REGISTER;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telephone", telphone);
            jsonObject.put("realName", userName);
            jsonObject.put("password", password);
            jsonObject.put("groupId", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("register:" + url + " tel:" + telphone + " userName:" + userName +
                " password:" + password + " groupId:" + groupId);
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
