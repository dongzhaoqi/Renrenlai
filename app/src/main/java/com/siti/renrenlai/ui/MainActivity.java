package com.siti.renrenlai.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.CustomApplcation;
import com.siti.renrenlai.util.SharedPreferencesUtil;

public class MainActivity extends TitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(((CustomApplcation) getApplication()).getUser()
                .getUserName());
        // showBackwardViewWithoutWord(false);
        showBackwardViewWithoutWord(true);
        showForwardView(R.string.text_forward, true);
    }

    @Override
    protected void onForward(View forwardView) {
        super.onForward(forwardView);
        SharedPreferencesUtil.writeString(getSharedPreferences("login", Context.MODE_PRIVATE),"userName","0");
    }
}
