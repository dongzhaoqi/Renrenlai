package com.siti.renrenlai.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.MyProfileActivity;
import com.siti.renrenlai.view.TagGroup;


public class HobbyDialog extends Dialog implements OnClickListener{

	private Activity mActivity;
	private Dialog dialog;
	private Button btn_confirm,btn_cancel;
	private TagGroup mTagGroup;

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
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_confirm:
				String choosen = TagGroup.getCheckedTags();
				//Toast.makeText(mActivity, choosen, Toast.LENGTH_SHORT).show();
				MyProfileActivity.setHobby(choosen);
				HobbyDialog.this.dismiss();
				break;
			case R.id.btn_cancel:
				HobbyDialog.this.dismiss();
				break;
		default:
			break;
		}
	}



}
