package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.User;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import java.lang.ref.WeakReference;


public class SplashActivity extends BaseActivity {

    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;
    private static final int WAITE = 300;
    public static final String TAG = "SplashActivity";
    private RelativeLayout relative;
    private static User user;
    private MyHandler handler = new MyHandler(this);
    static String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        relative = (RelativeLayout) findViewById(R.id.relative);
        startAnimation(relative);
        //SharedPreferencesUtil.writeString(getSharedPreferences("login", Context.MODE_PRIVATE),"userName","0");

    }

    private void startAnimation(RelativeLayout relative) {
        AnimationSet set = new AnimationSet(false);

        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);
        alpha.setFillAfter(true);

        set.addAnimation(alpha);

        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(1, 1000);
            }

        });
        relative.startAnimation(set);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<SplashActivity> mActivity;

        public MyHandler(SplashActivity splashActivity) {
            mActivity = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(activity, "login"), "userName");
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                        /*if (!userName.equals("0")) {
                            ((CustomApplication) activity.getApplication()).setUser(user);
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        } else {
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                            activity.finish();
                        }*/
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
