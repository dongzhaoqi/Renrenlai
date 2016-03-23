package com.siti.renrenlai.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.siti.renrenlai.R;
import com.siti.renrenlai.fragment.ActivityFragment;
import com.siti.renrenlai.fragment.FindFragment;
import com.siti.renrenlai.fragment.MeFragment;

public class MainActivity extends BaseActivity{

    private Button[] mTabs;
    private Fragment[] mFragments;
    private FindFragment mFindFragment;
    private ActivityFragment mActivityFragment;
    private MeFragment mMeFragment;
    private int currentTabIndex = 0;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initTab();
    }

    private void initTab() {
        mFindFragment = new FindFragment();
        mActivityFragment = new ActivityFragment();
        mMeFragment = new MeFragment();
        mFragments = new Fragment[]{mFindFragment,mActivityFragment,mMeFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,mFindFragment)
                .show(mFindFragment).commit();
    }

    private void initView() {
        mTabs = new Button[3];
        mTabs[0] = (Button) findViewById(R.id.btn_find);
        mTabs[1] = (Button) findViewById(R.id.btn_activity);
        mTabs[2] = (Button) findViewById(R.id.btn_me);

        mTabs[0].setSelected(true);
        index = 0;
    }

    public void onTabSelect(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_find:
                index = 0;
                break;
            case R.id.btn_activity:
                index = 1;
                break;
            case R.id.btn_me:
                index = 2;
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

}
