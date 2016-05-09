package com.siti.renrenlai.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Dong on 2016/3/31.
 */
public class CommonUtils {

    public static void showKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }
}
