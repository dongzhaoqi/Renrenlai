package com.siti.renrenlai.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;


public class HSVAdapter extends PagerAdapter {

	private Context mContext;
	private ArrayList<String>imagePath;

	public HSVAdapter(Context context, ArrayList<String>images){
		this.mContext = context;
		this.imagePath = images;
	}

	@Override
	public int getCount() {
		return imagePath.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}


}
