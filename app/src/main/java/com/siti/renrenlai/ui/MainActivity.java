package com.siti.renrenlai.ui;

import android.os.Bundle;

import com.siti.renrenlai.R;

public class MainActivity extends TitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("第一个Activity");
        // showBackwardViewWithoutWord(false);
        showBackwardViewWithoutWord(true);
        showForwardView(R.string.text_forward, true);
    }
}
