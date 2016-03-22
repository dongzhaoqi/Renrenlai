package com.siti.renrenlai.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	
	public static SharedPreferences getSharedPreference(Context context,String name){
		
		//获取SharedPreferences对象     
        SharedPreferences sp = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        //存入数据       
        return sp;
		
	}
	    
    public static void writeString(SharedPreferences sp,String name,String value){
    	
    	Editor editor = sp.edit();
        editor.putString(name, value);
        editor.commit();
    	
    }
    
  public static String readString(SharedPreferences sp,String name){
    	
        return sp.getString(name, "0");
    	
    }
	
	

}
