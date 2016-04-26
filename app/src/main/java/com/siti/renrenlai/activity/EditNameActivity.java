package com.siti.renrenlai.activity;

import android.os.Bundle;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout;

/**
 * Created by Dong on 2016/4/26.
 */
public class EditNameActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        initTopBarForBoth("编辑昵称", "保存", new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showToast("保存");
            }
        });
    }
}
