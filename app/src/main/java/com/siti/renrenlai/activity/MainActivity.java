package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.fragment.FindFragment;
import com.siti.renrenlai.fragment.MeFragment;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.CustomApplication;
import com.software.shell.fab.ActionButton;

import java.util.LinkedHashSet;
import java.util.Set;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Button[] mTabs;
    private ActionButton btn_activity;
    private Fragment[] mFragments;
    private FindFragment mFindFragment;
    private MeFragment mMeFragment;
    private int currentTabIndex = 0;
    private int index;
    private long firstTime;     //记录初次按下后退键的时间
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initTab();

        setAlias();		//为设备设置别名,以便可以定向推送
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

    private void setAlias(){
        String alias = CustomApplication.getInstance().getUser().getUserName();
        Log.d(TAG, "setAlias() returned: " + alias);
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (!CommonUtils.isValidTagAndAlias(alias)) {
            Toast.makeText(MainActivity.this,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }


    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    //Log.i(TAG, logs);
                    
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (CommonUtils.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
            CommonUtils.showToast(logs, getApplicationContext());
        }

    };
}
