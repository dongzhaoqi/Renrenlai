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

import org.xutils.DbManager;
import org.xutils.x;

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
	private DbManager.DaoConfig daoConfig;
	private DbManager db;

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

		x.Ext.init(this);
		x.Ext.setDebug(true); // 是否输出debug日志

	}

	public DbManager.DaoConfig getDaoConfig() {
		DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
				.setDbName("renrenactivity.db")
				// 不设置dbDir时, 默认存储在app的私有目录.
				.setDbDir(new File("/RenrenLai")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
				.setDbVersion(2)
				.setDbOpenListener(new DbManager.DbOpenListener() {
					@Override
					public void onDbOpened(DbManager db) {
						// 开启WAL, 对写入加速提升巨大
						db.getDatabase().enableWriteAheadLogging();
					}
				});
		return daoConfig;
	}

	public void setDaoConfig(DbManager.DaoConfig daoConfig) {
		this.daoConfig = daoConfig;
	}

	public DbManager getDb() {
		return db;
	}

	public void setDb(DbManager db) {
		this.db = db;
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
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/RenrenLai");
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
