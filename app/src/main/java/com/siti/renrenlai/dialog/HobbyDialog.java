package com.siti.renrenlai.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.MyProfileActivity;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.TagGroup;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class HobbyDialog extends Dialog implements OnClickListener{

	private Activity mActivity;
	private Dialog dialog;
	private Button btn_confirm,btn_cancel;
	private TagGroup mTagGroup;
	String userName;
	public HobbyDialog(Activity activity, int theme) {
		super(activity, theme);
		this.mActivity = activity;
	}

	public HobbyDialog(Activity activity) {
		this(activity, R.style.dialog_hobby);
		dialog= new Dialog(activity);		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_editor);
		initView();
	}

	private void initView() {
		mTagGroup = (TagGroup) findViewById(R.id.tag_group);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_confirm.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		String[] tags = new String[]{"健身", "游泳", "跑步", "遛狗", "羽毛球", "乒乓球", "篮球", "足球", "看书", "音乐"};
		mTagGroup.setTags(tags);
		TagGroup.clearCheckedTags();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_confirm:
				String choosen = TagGroup.getCheckedTags();
				//Toast.makeText(mActivity, choosen, Toast.LENGTH_SHORT).show();
				MyProfileActivity.setHobby(choosen);
				SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(mActivity, "login"), "hobby", choosen);
				HobbyDialog.this.dismiss();
				modifyHobby(choosen);
				break;
			case R.id.btn_cancel:
				HobbyDialog.this.dismiss();
				break;
		default:
			break;
		}
	}

	public void modifyHobby(final String hobby){
		String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(mActivity, "login"), "userName");

		String api = null;
		try {
			api = "/updateUserHobby?userName="+URLEncoder.encode(userName, "utf-8")+"&userHobby="+ URLEncoder.encode(hobby, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = ConstantValue.urlRoot + api;
		System.out.println("url:" + url);
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("response", "response:" + response.toString());
						Toast.makeText(mActivity, "修改成功!", Toast.LENGTH_SHORT).show();
						SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(mActivity, "login"),
								"hobby", hobby);

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Error: ", error.getMessage());
				Toast.makeText(mActivity, "出错了!", Toast.LENGTH_SHORT).show();
			}
		});
		req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		CustomApplication.getInstance().addToRequestQueue(req);
	}

}
