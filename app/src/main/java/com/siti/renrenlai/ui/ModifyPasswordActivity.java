package com.siti.renrenlai.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.siti.renrenlai.R;

/**
 * Created by Dong on 2016/3/31.
 */
public class ModifyPasswordActivity extends BaseActivity implements OnClickListener{

    private EditText et_old_password, et_confirm_password, et_new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initViews();
    }

    private void initViews() {
        initTopBarForBoth("修改密码","提交",new OnRightButtonClickListener(){
            @Override
            public void onClick() {
                showToast("提交");
            }
        });

        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

}
