package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.ProjectIntention;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/6/13.
 */
public class PreviewWish extends BaseActivity {

    @Bind(R.id.et_how)
    TextView etHow;
    @Bind(R.id.et_which)
    TextView etWhich;
    @Bind(R.id.et_what)
    TextView etWhat;
    @Bind(R.id.et_who)
    TextView etWho;
    @Bind(R.id.tv_when)
    TextView tvWhen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_wish);
        ButterKnife.bind(this);
        initTopBarForLeft("预览项目意愿");

        initView();
    }

    private void initView(){
        ProjectIntention intention = (ProjectIntention) getIntent().getSerializableExtra("projectIntention");
        etHow.setText(intention.getProjectIntentionName());
        etWhich.setText(intention.getProjectIntentionDescrip());
        etWhat.setText(intention.getProjectIntentionPurpose());
        etWho.setText(intention.getProjectIntentionBenefitForWho());
        tvWhen.setText(intention.getProjectIntentionExecuteTime());
    }
}
