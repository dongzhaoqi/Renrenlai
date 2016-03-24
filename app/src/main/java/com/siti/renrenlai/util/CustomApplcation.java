package com.siti.renrenlai.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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
		initImageLoader(getApplicationContext());
	}

	public static CustomApplcation getInstance() {
		return mInstance;
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
