package com.siti.renrenlai.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

/**
 * Created by Dong on 2016/3/22.
 */
public class ActivityInfo extends BaseActivity implements OnClickListener {

    private TextView txt_avtivity_name;
    private ImageView activity_img;
    private RelativeLayout layout_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViews();
        initEvent();
    }

    private void initViews() {
        initTopBarForBoth("活动详情", android.R.drawable.ic_menu_share, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showShare();
            }
        });
        String activity_name = getIntent().getExtras().getString("name");
        final String str_activity_img = getIntent().getExtras().getString("img");
        Log.d("TAG",str_activity_img);
        txt_avtivity_name = (TextView) findViewById(R.id.activity_name);
        activity_img = (ImageView) findViewById(R.id.activity_img);
        layout_contact = (RelativeLayout) findViewById(R.id.layout_contact);

        txt_avtivity_name.setText(activity_name);

    }

    private void initEvent(){
        layout_contact.setOnClickListener(this);
    }

    private void showShare() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_contact:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + "13761076562"));
                startActivity(intent);
                break;
        }
    }

}
