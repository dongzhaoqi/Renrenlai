package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

//import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.igexin.sdk.PushManager;
import com.siti.renrenlai.R;
import com.siti.renrenlai.fragment.FindFragment;
import com.siti.renrenlai.fragment.MeFragment;
import com.software.shell.fab.ActionButton;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Button[] mTabs;
    //private AddFloatingActionButton btn_activity;
    private ActionButton btn_activity;
    private Fragment[] mFragments;
    private FindFragment mFindFragment;
    private MeFragment mMeFragment;
    private int currentTabIndex = 0;
    private int index;
    private long firstTime;     //记录初次按下后退键的时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initTab();

        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    private void initTab() {
        mFindFragment = new FindFragment();
        mMeFragment = new MeFragment();
        mFragments = new Fragment[]{mFindFragment, mMeFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,mFindFragment)
                .show(mFindFragment).commit();
    }

    private void initView() {
        mTabs = new Button[2];
        mTabs[0] = (Button) findViewById(R.id.btn_find);
        mTabs[1] = (Button) findViewById(R.id.btn_me);
        mTabs[0].setSelected(true);
        index = 0;

        btn_activity = (ActionButton) findViewById(R.id.btn_activity);
        btn_activity.setOnClickListener(this);
    }

    public void onTabSelect(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_find:
                index = 0;
                break;
            case R.id.btn_me:
                index = 1;
                break;
        }

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.remove(mFragments[currentTabIndex]);
            if (!mFragments[index].isAdded()) {
                trx.replace(R.id.fragment_container, mFragments[index]);
            }
            trx.show(mFragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity:
                startAnimActivity(LaunchActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - firstTime < 2000){
            super.onBackPressed();
        }else{
            showToast("再按一次退出");
        }
        firstTime = System.currentTimeMillis();
    }
}
