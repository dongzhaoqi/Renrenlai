package com.siti.renrenlai.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout;
import com.siti.renrenlai.view.HeaderLayout.HeaderStyle;
import com.siti.renrenlai.view.HeaderLayout.onLeftBtnClickListener;
import com.siti.renrenlai.view.HeaderLayout.onLeftImageButtonClickListener;
import com.siti.renrenlai.view.HeaderLayout.onLeftTextClickListener;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public abstract class BaseFragment extends Fragment {
	
	public HeaderLayout mHeaderLayout;
	protected View contentView;
	public LayoutInflater mInflater;
	private ACProgressFlower dialog;
	private ACProgressFlower.Builder builder;
	private Handler handler = new Handler();
	
	public void runOnWorkThread(Runnable action) {
		new Thread(action).start();
	}

	public void runOnUiThread(Runnable action) {
		handler.post(action);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		builder = new ACProgressFlower.Builder(getActivity())
				.direction(ACProgressConstant.DIRECT_CLOCKWISE)
				.themeColor(Color.WHITE)
				.fadeColor(Color.DKGRAY);
	}

	
	public BaseFragment() {
		
	}

	Toast mToast;

	public void showToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public void showToast(int text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	
	public View findViewById(int paramInt) {
		return getView().findViewById(paramInt);
	}


	public void initTopBarForOnlyTitle(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
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


	public void initTopBarForLeftBtnBoth(String titleName,
			int leftDrawableId, onLeftBtnClickListener listener1,
			int rightDrawableId, onRightImageButtonClickListener listener2) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_BUTTON);
		mHeaderLayout.setTitleAndLeftButton(titleName, leftDrawableId,
				listener1);
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener2);
	}

	public void initTopBarForLeftTextBoth(String titleName,
			String leftText, onLeftTextClickListener listener1,
			int rightDrawableId, onRightImageButtonClickListener listener2) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_LEFT_TEXT);
		mHeaderLayout.setTitleAndLeftText(titleName, leftText,
				listener1);
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener2);
	}

	public void initLeftText(String leftText){
		mHeaderLayout.setLeftText(leftText);
	}

	public void initTopBarForLeft(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
	}


	public void initTopBarForRight(String titleName,int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}
	

	public class OnLeftButtonClickListener implements
			onLeftImageButtonClickListener {

		@Override
		public void onClick() {
			getActivity().finish();
		}
	}


	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}
	
	public void startAnimActivity(Class<?> cla) {
		getActivity().startActivity(new Intent(getActivity(), cla));
	}

	public void showProcessDialog() {
		dialog = builder.build();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public void showProcessDialog(String message) {
		dialog = builder.text(message).build();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public void dismissProcessDialog() {
		if(dialog != null){
			dialog.dismiss();
		}
	}

}
