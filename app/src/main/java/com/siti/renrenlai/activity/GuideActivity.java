package com.siti.renrenlai.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dev.sacot41.scviewpager.DotsView;
import com.dev.sacot41.scviewpager.SCPositionAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimationUtil;
import com.dev.sacot41.scviewpager.SCViewPager;
import com.dev.sacot41.scviewpager.SCViewPagerAdapter;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.SharedPreferencesUtil;

/**
 * Created by Dong on 4/17/2016.
 */
public class GuideActivity extends FragmentActivity {

    private static final int NUM_PAGES = 5;

    private SCViewPager mViewPager;
    private SCViewPagerAdapter mPageAdapter;
    private DotsView mDotsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);

        isFirst();

        mViewPager = (SCViewPager) findViewById(R.id.viewpager_main_activity);
        mDotsView = (DotsView) findViewById(R.id.dotsview_main);
        mDotsView.setDotRessource(R.drawable.dot_selected, R.drawable.dot_unselected);
        mDotsView.setNumberOfPage(NUM_PAGES);

        mPageAdapter = new SCViewPagerAdapter(getSupportFragmentManager());
        mPageAdapter.setNumberOfPage(NUM_PAGES);
        mPageAdapter.setFragmentBackgroundColor(R.color.theme_100);
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mDotsView.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final Point size = SCViewAnimationUtil.getDisplaySize(this);

        View nameTag = findViewById(R.id.imageview_main_activity_name_tag);
        SCViewAnimation nameTagAnimation = new SCViewAnimation(nameTag);
        nameTagAnimation.addPageAnimation(new SCPositionAnimation(this, 0, 0, -size.y / 2));
        mViewPager.addAnimation(nameTagAnimation);

        View currentlyWork = findViewById(R.id.imageview_main_activity_currently_work);
        SCViewAnimation currentlyWorkAnimation = new SCViewAnimation(currentlyWork);
        currentlyWorkAnimation.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        mViewPager.addAnimation(currentlyWorkAnimation);

        View atSkex = findViewById(R.id.imageview_main_activity_at_skex);
        SCViewAnimationUtil.prepareViewToGetSize(atSkex);
        SCViewAnimation atSkexAnimation = new SCViewAnimation(atSkex);
        atSkexAnimation.addPageAnimation(new SCPositionAnimation(getApplicationContext(), 0, 0, -( size.y - atSkex.getHeight() )));
        atSkexAnimation.addPageAnimation(new SCPositionAnimation(getApplicationContext(), 1, -size.x, 0));
        mViewPager.addAnimation(atSkexAnimation);

        View mobileView = findViewById(R.id.imageview_main_activity_mobile);
        SCViewAnimation mobileAnimation = new SCViewAnimation(mobileView);
        mobileAnimation.startToPosition((int)(size.x*1.5), null);
        mobileAnimation.addPageAnimation(new SCPositionAnimation(this, 0, -(int)(size.x*1.5), 0));
        mobileAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -(int)(size.x*1.5), 0));
        mViewPager.addAnimation(mobileAnimation);

        View djangoView = findViewById(R.id.imageview_main_activity_django_python);
        SCViewAnimation djangoAnimation = new SCViewAnimation(djangoView);
        djangoAnimation.startToPosition(null, -size.y);
        djangoAnimation.addPageAnimation(new SCPositionAnimation(this, 0, 0, size.y));
        djangoAnimation.addPageAnimation(new SCPositionAnimation(this, 1, 0, size.y));
        mViewPager.addAnimation(djangoAnimation);

        View commonlyView = findViewById(R.id.imageview_main_activity_commonly);
        SCViewAnimation commonlyAnimation = new SCViewAnimation(commonlyView);
        commonlyAnimation.startToPosition(size.x, null);
        commonlyAnimation.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        commonlyAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        mViewPager.addAnimation(commonlyAnimation);

        View butView = findViewById(R.id.imageview_main_activity_but);
        SCViewAnimation butAnimation = new SCViewAnimation(butView);
        butAnimation.startToPosition(size.x, null);
        butAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x,0));
        butAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x,0));
        mViewPager.addAnimation(butAnimation);

        View diplomeView = findViewById(R.id.imageview_main_activity_diploma);
        SCViewAnimation diplomeAnimation = new SCViewAnimation(diplomeView);
        diplomeAnimation.startToPosition((size.x *2), null);
        diplomeAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x*2,0));
        diplomeAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x*2 ,0));
        mViewPager.addAnimation(diplomeAnimation);

        View whyView = findViewById(R.id.imageview_main_activity_why);
        SCViewAnimation whyAnimation = new SCViewAnimation(whyView);
        whyAnimation.startToPosition(size.x, null);
        whyAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        whyAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        mViewPager.addAnimation(whyAnimation);

        View futureView = findViewById(R.id.imageview_main_future);
        SCViewAnimation futureAnimation = new SCViewAnimation(futureView);
        futureAnimation.startToPosition(null, -size.y);
        futureAnimation.addPageAnimation(new SCPositionAnimation(this, 2, 0, size.y));
        futureAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(futureAnimation);

        View arduinoView = findViewById(R.id.imageview_main_arduino);
        SCViewAnimation arduinoAnimation = new SCViewAnimation(arduinoView);
        arduinoAnimation.startToPosition(size.x * 2, null);
        arduinoAnimation.addPageAnimation(new SCPositionAnimation(this, 2, - size.x *2, 0));
        arduinoAnimation.addPageAnimation(new SCPositionAnimation(this, 3, - size.x, 0));
        mViewPager.addAnimation(arduinoAnimation);

        View raspberryView = findViewById(R.id.imageview_main_raspberry_pi);
        SCViewAnimation raspberryAnimation = new SCViewAnimation(raspberryView);
        raspberryAnimation.startToPosition(-size.x, null);
        raspberryAnimation.addPageAnimation(new SCPositionAnimation(this, 2, size.x, 0));
        raspberryAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(raspberryAnimation);

        View connectedDeviceView = findViewById(R.id.imageview_main_connected_device);
        SCViewAnimation connectedDeviceAnimation = new SCViewAnimation(connectedDeviceView);
        connectedDeviceAnimation.startToPosition((int)(size.x *1.5), null);
        connectedDeviceAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -(int) (size.x * 1.5), 0));
        connectedDeviceAnimation.addPageAnimation(new SCPositionAnimation(this, 3,  - size.x, 0));
        mViewPager.addAnimation(connectedDeviceAnimation);

        View checkOutView = findViewById(R.id.imageview_main_check_out);
        SCViewAnimation checkOutAnimation = new SCViewAnimation(checkOutView);
        checkOutAnimation.startToPosition(size.x, null);
        checkOutAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(checkOutAnimation);

        View startView = findViewById(R.id.btn_start);
        SCViewAnimation startAnimation = new SCViewAnimation(startView);
        startAnimation.startToPosition(size.x, null);
        startAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(startAnimation);

        startView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    private void isFirst(){

        int count = SharedPreferencesUtil.readInt(
                SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "count"), "count");
        Log.d("count", "count:"+count);
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0) {
            SharedPreferencesUtil.writeInt(getSharedPreferences("count", Context.MODE_PRIVATE), "count", ++count);
        }else{
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }

    }
}