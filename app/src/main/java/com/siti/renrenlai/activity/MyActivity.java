package com.siti.renrenlai.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
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
    private Fragment[] mFragments;
    private static FavoriteFragment mFavoriteFragment;
    private EnrollFragment mEnrollFragment;
    private LaunchFragment mLaunchFragment;
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

        mFavoriteFragment = new FavoriteFragment();
        mEnrollFragment = new EnrollFragment();
        mLaunchFragment = new LaunchFragment();
        mFragments = new Fragment[]{mFavoriteFragment, mEnrollFragment, mLaunchFragment};

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

            return mFragments[position];

            /*switch (position) {
                case 0:
                    return mFavoriteFragment;
                case 1:
                    return mEnrollFragment;
                case 2:
                    return mLaunchFragment;
                default:
                    return null;
            }*/

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

}
