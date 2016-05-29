package com.siti.renrenlai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout;
import com.siti.renrenlai.view.HeaderLayout.HeaderStyle;
import com.siti.renrenlai.view.HeaderLayout.onLeftImageButtonClickListener;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class BaseActivity extends FragmentActivity {

	protected HeaderLayout mHeaderLayout;
	protected int mScreenWidth;
	protected int mScreenHeight;
	private ACProgressFlower dialog;
	private ACProgressFlower.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;

		builder = new ACProgressFlower.Builder(this)
				.direction(ACProgressConstant.DIRECT_CLOCKWISE)
				.themeColor(Color.WHITE)
				.fadeColor(Color.DKGRAY);
	}

	Toast mToast;

	public void showToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_SHORT);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});
			
		}
	}

	public void showToast(final int resId) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), resId,
							Toast.LENGTH_SHORT);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}

	public void showLog(String msg){
		Log.i("life",msg);
	}
	

	public void initTopBarForOnlyTitle(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
	}


	public void initTopBarForBoth(String titleName, int rightDrawableId,String text,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId,text,
				listener);
	}

	public void initTopBarForTitleAndRight(String titleName,String text,
									  onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndRightText(titleName,text,
				listener);
	}
	
	public void initTopBarForBoth(String titleName, String text,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightText(titleName,text,
				listener);
	}
	
	public void initTopBarForBoth(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}

	public void initTopBarForLeft(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
	}
	
	public class OnLeftButtonClickListener implements
			onLeftImageButtonClickListener {

		@Override
		public void onClick() {
			finish();
		}
	}
	
	public class OnRightButtonClickListener implements
	onRightImageButtonClickListener {
		
		@Override
		public void onClick() {
			//To Do
		}
	}
	
	public void startAnimActivity(Class<?> cla) {
		this.startActivity(new Intent(this, cla));
	}
	
	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}

	public void showProcessDialog() {
		dialog = builder.build();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public void showProcessDialog(String message) {
		dialog = builder.text(message).build();
		dialog.setCanceledOnTouchOutside(true);
		if(!dialog.isShowing()){
			dialog.show();
		}
	}

	public void dismissProcessDialog() {
		if(dialog != null){
			dialog.dismiss();
		}
	}

}
