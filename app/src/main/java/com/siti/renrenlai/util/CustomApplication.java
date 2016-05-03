package com.siti.renrenlai.util;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.siti.renrenlai.bean.User;

import java.io.File;


/**
 * @author Dong
 * @date 2016-03-22
 *
 */
public class CustomApplication extends Application {

	public static CustomApplication mInstance;
	public User user = new User();
	public static int mScreenWidth;
	public static int mScreenHight;
	/**
	 * Log or request TAG
	 */
	public static final String TAG = "VolleyPatterns";
	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;

	public static synchronized CustomApplication getInstance() {
		return mInstance;
	}

	public static void setmInstance(CustomApplication mInstance) {
		CustomApplication.mInstance = mInstance;
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
		initImageLoader(getApplicationContext());
	}


	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified
	 * then it is used else Default TAG is used.
	 *
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 *
	 * @param req
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	private void initImageLoader(Context context){

		//图片的缓存路径
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/aa");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.memoryCache(new LruMemoryCache(20 * 1024 * 1024))
				.denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}

}
