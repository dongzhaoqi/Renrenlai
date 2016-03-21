package com.siti.renrenlai;

import android.os.Bundle;

public class MainActivity extends TitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("第一个Activity");
        // showBackwardViewWithoutWord(false);
        showBackwardView(R.string.text_back, true);
        showForwardView(R.string.text_forward, true);
    }
}
