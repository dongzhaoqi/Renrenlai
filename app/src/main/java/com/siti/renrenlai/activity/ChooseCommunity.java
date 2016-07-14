package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.CityAdapter;
import com.siti.renrenlai.adapter.MyArrayAdapter;
import com.siti.renrenlai.adapter.ProvinceAdapter;
import com.siti.renrenlai.bean.City;
import com.siti.renrenlai.bean.Group;
import com.siti.renrenlai.bean.Province;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.view.HeaderLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/7/14.
 */
public class ChooseCommunity extends BaseActivity {
    @Bind(R.id.spinner_province)
    Spinner spinnerProvince;
    @Bind(R.id.spinner_city)
    Spinner spinnerCity;
    @Bind(R.id.tv_community)
    AutoCompleteTextView tvCommunity;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<Group> groupList;
    MyArrayAdapter<String> arrayAdapter;
    ProvinceAdapter provinceAdapter;
    CityAdapter cityAdapter;
    int provinceId, cityId, groupId;
    String url, groupName;
    private static final String TAG = "ChooseCommunity";
    private static int CHOOSE_COMMUNITY = 4;        //选择我的小区

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        ButterKnife.bind(this);
        initTopBarForBoth("选择我的小区", "提交", new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent();
                intent.putExtra("groupName", groupName);
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(ChooseCommunity.this, "小区名称:" + groupName, Toast.LENGTH_SHORT).show();
            }
        });
        initProvince();
    }

    /**
     * 初始化省份
     */
    private void initProvince() {
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
                Log.e(TAG, "initProvince onErrorResponse: " + error.getMessage());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    private void getProvince(JSONObject response) {
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
    private void initCity(int provinceId) {
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
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    private void getCity(JSONObject response) {

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

    private void initCommunity(int cityId) {
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

    private void getGroupList(JSONObject response) {
        JSONArray result = response.optJSONArray("result");
        groupList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), Group.class);
        List<String> groups = new ArrayList<>();
        for (Group group : groupList) {
            groups.add(group.getGroupProName());
        }
        arrayAdapter = new MyArrayAdapter<>(ChooseCommunity.this, android.R.layout.simple_dropdown_item_1line, groups);
        tvCommunity.setAdapter(arrayAdapter);
        tvCommunity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                groupName = parent.getAdapter().getItem(position).toString();
                Log.d(TAG, "onItemClick: " + groupName);
            }
        });
        tvCommunity.setThreshold(1);
    }

}
