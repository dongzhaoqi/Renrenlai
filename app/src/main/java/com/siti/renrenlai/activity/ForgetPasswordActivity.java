package com.siti.renrenlai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Dong on 2016/3/22.
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Bind(R.id.btn_sign_up) Button btnSignUp;
    @Bind(R.id.btn_verify) Button btnVerify;
    @Bind(R.id.et_phone) EditText et_phone;
    @Bind(R.id.et_smscode) EditText et_smscode;

    private static final int GET_VERIFY_CODE = 1;
    private static final int REGET_VERIFY_CODE = 2;
    private static final String TAG = "ForgetPasswordActivity";
    int i = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);
        initTopBarForLeft("找回密码");
        initSMSSDK();

    }

    private void initSMSSDK(){
        // 初始化短信SDK
        SMSSDK.initSDK(this, ConstantValue.SMS_APPKEY, ConstantValue.SMS_APPSECRET, false);
        EventHandler eventHandler = new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                Message message = new Message();
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                handler.sendMessage(message);
            }
        };

        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_VERIFY_CODE:
                    btnVerify.setText("获取验证码");
                    btnVerify.setClickable(true);
                    btnVerify.setBackgroundColor(Color.parseColor("#F5A623"));
                    i = 60;
                    break;
                case REGET_VERIFY_CODE:
                    btnVerify.setText("重新发送(" + i + ")");
                    break;
                default:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e("event", "event=" + event + " data="+data + " result="+result);
                    if (result == SMSSDK.RESULT_COMPLETE) {

                        if(data.toString().equals("true")){
                            Toast.makeText(ForgetPasswordActivity.this, "已验证成功，前去修改密码", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetPasswordActivity.this,
                                    ModifyPasswordActivity.class);
                            intent.putExtra("tel", et_phone.getText().toString());
                            startActivity(intent);
                        }else {

                            // 短信注册成功后，返回MainActivity,然后提示新好友
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                                Toast.makeText(getApplicationContext(), "提交验证码成功",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetPasswordActivity.this,
                                        ModifyPasswordActivity.class);
                                intent.putExtra("tel", et_phone.getText().toString());
                                startActivity(intent);
                            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                /*Toast.makeText(getApplicationContext(), "验证码已经发送",
                                        Toast.LENGTH_SHORT).show();*/
                            } else {
                                ((Throwable) data).printStackTrace();
                            }
                        }
                    }
            }
        }
    };

    @OnClick({R.id.btn_verify, R.id.btn_sign_up})
    public void onClick(View view) {
        String phoneNums = et_phone.getText().toString();
        switch (view.getId()) {
            case R.id.btn_verify:
                SMSSDK.getVerificationCode("86", phoneNums);
                Log.d(TAG, "onClick: " + phoneNums);
                if(!isValidPhone(phoneNums)) return;
                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                btnVerify.setClickable(false);
                btnVerify.setBackgroundColor(Color.parseColor("#66F5A623"));
                btnVerify.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(REGET_VERIFY_CODE);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(GET_VERIFY_CODE);
                    }
                }).start();
                break;
            case R.id.btn_sign_up:
                SMSSDK.submitVerificationCode("86", phoneNums, et_smscode.getText().toString());
                break;
        }
    }

    private boolean isValidPhone(String phone){
        if(TextUtils.isEmpty(phone)){
            showToast(R.string.hint_empty_phone);
            return false;
        }else if(!CommonUtils.isMobilePhone(phone)){
            showToast(R.string.hint_invalid_phone);
            return false;
        }
        return true;
    }

    protected void onDestroy() {
        // 销毁回调监听接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
