package com.siti.renrenlai.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.siti.renrenlai.R;
import com.siti.renrenlai.fragment.EnrollFragment;
import com.siti.renrenlai.fragment.FavoriteFragment;
import com.siti.renrenlai.fragment.LaunchFragment;

import net.yanzm.mth.MaterialTabHost;

import java.util.Locale;

/**
 * Created by Dong on 2016/3/22.
 */
public class MyActivity extends BaseActivity implements OnClickListener{

    private int pos;
    //private Fragment[] mFragments;
    private FavoriteFragment mFavoriteFragment = new FavoriteFragment();
    private EnrollFragment mEnrollFragment = new EnrollFragment();
    private LaunchFragment mLaunchFragment = new LaunchFragment();
    private SectionsPagerAdapter pagerAdapter;
    private MaterialTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initTab();
        initViews();
    }

    private void initTab(){
        tabHost = (MaterialTabHost) findViewById(R.id.tabhost);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
//        tabHost.setType(MaterialTabHost.Type.Centered);
//        tabHost.setType(MaterialTabHost.Type.LeftOffset);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabHost.setElevation(0);
        }

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(pagerAdapter.getPageTitle(i));
        }

    }

    private void initViews() {
        initTopBarForLeft("我的活动");

       /* mFavoriteFragment = new FavoriteFragment();
        mEnrollFragment = new EnrollFragment();
        mLaunchFragment = new LaunchFragment();
        mFragments = new Fragment[]{mFavoriteFragment, mEnrollFragment, mLaunchFragment};*/

        pos = getIntent().getIntExtra("pos", -1);
        System.out.println("pos:" + pos);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(tabHost);
        viewPager.setCurrentItem(pos);
        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Bundle bundle = new Bundle();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            /*Fragment fragment = mFragments[position];
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            return fragment;*/

            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = mFavoriteFragment;
                    break;
                case 1:
                    fragment = mEnrollFragment;
                    break;
                case 2:
                    fragment = mLaunchFragment;
                    break;
                default:
                    return null;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.txt_favorite).toUpperCase(l);
                case 1:
                    return getString(R.string.txt_enroll).toUpperCase(l);
                case 2:
                    return getString(R.string.txt_launch).toUpperCase(l);
            }
            return null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //空操作解决Fragment重叠问题
    }

}
