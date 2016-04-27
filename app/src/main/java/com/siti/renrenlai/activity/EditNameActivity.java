package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/4/26.
 */
public class EditNameActivity extends BaseActivity {

    @Bind(R.id.et_modify_name) EditText etModifyName;
    private static int MODIFY_NAME = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        ButterKnife.bind(this);
        initTopBarForBoth("编辑昵称", "保存", new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                String nickName = etModifyName.getText().toString();
                SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(getApplicationContext(), "login"),
                        "nickName", nickName);
                Intent intent = new Intent();
                intent.putExtra("nickName", nickName);
                setResult(MODIFY_NAME, intent);
                finish();
            }
        });
    }
}
