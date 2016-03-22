package com.siti.renrenlai.util;

import android.app.Application;
import android.util.Log;

import com.siti.renrenlai.bean.User;


/**
 * @author Dong
 * @date 2016-03-22
 *
 */
public class CustomApplcation extends Application {

	public static CustomApplcation mInstance;
	public User user = new User();
	public static int mScreenWidth;
	public static int mScreenHight;

	public static CustomApplcation getmInstance() {
		return mInstance;
	}

	public static void setmInstance(CustomApplcation mInstance) {
		CustomApplcation.mInstance = mInstance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("", "application");
		mInstance = this;
	}

	public static CustomApplcation getInstance() {
		return mInstance;
	}

}
