package com.siti.renrenlai.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.User;
import com.siti.renrenlai.db.DbTempActivity;
import com.siti.renrenlai.dialog.HobbyDialog;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.ImageHelper;
import com.siti.renrenlai.util.PhotoUtil;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rebus.bottomdialog.BottomDialog;

/**
 * Created by Dong on 2016/3/22.
 */
public class MyProfileActivity extends BaseActivity implements OnClickListener {

    @Bind(R.id.img_photo)
    CircleImageView img_photo;
    @Bind(R.id.layout_name)
    RelativeLayout layout_name;
    @Bind(R.id.layout_nickname)
    RelativeLayout layout_nickname;
    @Bind(R.id.tv_nickName)
    TextView tv_nickName;
    @Bind(R.id.layout_community)
    RelativeLayout layout_community;
    @Bind(R.id.layout_gender)
    RelativeLayout layout_gender;
    @Bind(R.id.layout_hobby)
    RelativeLayout layout_hobby;
    @Bind(R.id.layout_introduction)
    RelativeLayout layout_introduction;
    @Bind(R.id.tv_gender)
    TextView tv_gender;

    static TextView tv_hobby;
    @Bind(R.id.tv_introduction)
    TextView tv_introduction;
    @Bind(R.id.tv_community)
    TextView tvCommunity;
    private Bitmap bitmap;
    private String imgName, nickName, groupName, gender, hobby, intro;
    private static final int SELECT_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static int MODIFY_NAME = 2;         //修改昵称
    private static int MODIFY_INTRO = 3;        //修改个人简介
    private static int CHOOSE_COMMUNITY = 4;        //选择我的小区
    private String userHeadImagePath, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tv_hobby = (TextView) findViewById(R.id.tv_hobby);
        ButterKnife.bind(this);
        initTopBarForLeft("我的资料");

        initProfile();
    }


    private void initProfile() {
        userHeadImagePath = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userHeadPicImagePath");

        if(!userHeadImagePath.contains("http")){
            userHeadImagePath = ConstantValue.urlRoot + userHeadImagePath;
        }
        Log.e("userHeadImagePath: " , userHeadImagePath );
        Picasso.with(this).load(userHeadImagePath).placeholder(R.drawable.no_img).into(img_photo);
        nickName = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "login"), "realName");

        userName = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "login"), "userName");

        tv_nickName.setText(nickName);

        gender = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "login"), "gender");

        if ("0".equals(gender)) {
            tv_gender.setText("请选择");
        } else {
            tv_gender.setText(gender);
        }

        hobby = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "login"), "interetsAndHobbies");

        if ("0".equals(hobby)) {
            tv_hobby.setText("请选择");
        } else {
            tv_hobby.setText(hobby);
        }

        groupName = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "login"), "groupName");
        tvCommunity.setText(groupName);

        intro = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "login"), "intro");
        if ("0".equals(intro)) {
            tv_introduction.setText("请填写");
        } else {
            tv_introduction.setText(intro);
        }
    }

    @OnClick({R.id.img_photo, R.id.layout_nickname, R.id.layout_community,
            R.id.layout_gender, R.id.layout_hobby, R.id.layout_introduction})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.img_photo:
                showPicDialog();
                break;
            case R.id.layout_nickname:
                intent.setClass(MyProfileActivity.this, EditNameActivity.class);
                startActivityForResult(intent, MODIFY_NAME);
                break;
            case R.id.layout_community:
                intent.setClass(MyProfileActivity.this, ChooseCommunity.class);
                startActivityForResult(intent, CHOOSE_COMMUNITY);
                break;
            case R.id.layout_gender:
                showGenderDialog();
                break;
            case R.id.layout_hobby:
                showHobbyDialog();
                break;
            case R.id.layout_introduction:
                intent.setClass(MyProfileActivity.this, IntroductionActivity.class);
                if ("0".equals(intro)) {
                    intent.putExtra("intro", "");
                } else {
                    intent.putExtra("intro", intro);
                }
                startActivityForResult(intent, MODIFY_INTRO);
                break;
        }
    }

    public void showPicDialog() {
        BottomDialog dialog = new BottomDialog(MyProfileActivity.this);
        dialog.title(R.string.str_select);
        dialog.canceledOnTouchOutside(true);
        dialog.cancelable(true);
        dialog.inflateMenu(R.menu.menu_dialog);
        dialog.setOnItemSelectedListener(new BottomDialog.OnItemSelectedListener() {
            @Override
            public boolean onItemSelected(int id) {
                switch (id) {
                    case R.id.item_pick_photo:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                        return true;
                    case R.id.item_take_pic:
                        goCamera();
                        return true;
                    default:
                        return false;
                }

            }

        });
        dialog.show();
    }

    private void goCamera() {
        File imgFolder = new File(Environment.getExternalStorageDirectory(), "RenrenLai");
        imgFolder.mkdirs();
        File img = new File(imgFolder, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg");
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        Uri imgUri = Uri.fromFile(img);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(camera, TAKE_PICTURE);
    }


    /**
     *
     * @param userName  用户名
     * @param bitmap    上传的bitmap图片
     * @param imageName 图片名字
     * @return
     */
    private void uploadUserHead(String userName, Bitmap bitmap, String imageName){
        String url = ConstantValue.UPDATE_USER_HEAD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("imageName", imageName);
            jsonObject.put("imageData", Bitmap2StrByBase64(bitmap));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "response:" + response.toString());
                        userHeadImagePath = response.optString("result");
                        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(getApplicationContext(), "login"),
                                "userHeadPicImagePath", userHeadImagePath);
                        showToast("上传成功!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "error.getMessage():" + error.getMessage());
                showToast("出错了!");
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);// 参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);

    }

    /**
     * "选择性别"弹出框
     */
    public void showGenderDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.str_select)
                .items(R.array.gender)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tv_gender.setText(text);
                        updateGender(text.toString());
                        return true;    // allow selection
                    }
                })
                .positiveText(R.string.okBtn)
                .show();
    }


    public void updateGender(final String gender) {
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        String url = null;
        try {
            url = ConstantValue.UPDATE_USER_GENDER + "?userName=" + URLEncoder.encode(userName, "utf-8") + "&userGender=" + URLEncoder.encode(gender, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "response:" + response.toString());
                        showToast("修改成功!");
                        SharedPreferencesUtil.writeString(SharedPreferencesUtil.getSharedPreference(getApplicationContext(), "login"),
                                "gender", gender);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("出错了!");
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    /**
     * "选择兴趣"弹出框
     */
    public void showHobbyDialog() {

        HobbyDialog dialog = new HobbyDialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); // 设置宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;// 设置高度
        lp.dimAmount = 0.5f;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public static void setHobby(String hobby) {
        tv_hobby.setText(hobby);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            System.out.println("result error!");
            return;
        }

        if (requestCode == SELECT_PICTURE) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            // 使用ContentProvider通过URI获取原始图片
            imgName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg";
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (photo != null) {
                // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                photo = ImageHelper.resizeImage(photo, 180, 180);
                //bitmap = ImageHelper.getRoundedCornerBitmap(photo, 130);
                // 释放原始图片占用的内存，防止out of memory异常发生
                //photo.recycle();
                img_photo.setImageBitmap(photo);
                uploadUserHead(userName, photo, imgName);
            }
        } else if (requestCode == TAKE_PICTURE) {

            File imgFolder = new File(Environment.getExternalStorageDirectory(), "AAA");
            imgName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg";
            File img = new File(imgFolder, imgName);
            bitmap = PhotoUtil.getImageThumbnail(img.getAbsolutePath(), 180, 180);
            bitmap = PhotoUtil.rotaingImageView(90, bitmap);
            //bitmap = ImageHelper.getRoundedCornerBitmap(bitmap, 130);
            img_photo.setImageBitmap(bitmap);
            uploadUserHead(userName, bitmap, imgName);
        } else if (requestCode == MODIFY_NAME) {
            nickName = data.getStringExtra("nickName");
            tv_nickName.setText(nickName);
        } else if (requestCode == MODIFY_INTRO) {
            System.out.println("modify intro");
            intro = SharedPreferencesUtil.readString(
                    SharedPreferencesUtil.getSharedPreference(getApplicationContext(), "login"), "intro");
            modifyInfo(tv_introduction, intro);
        } else if (requestCode == CHOOSE_COMMUNITY) {
            groupName = data.getStringExtra("groupName");
            modifyInfo(tvCommunity, groupName);
        }
    }

    public void modifyInfo(View view, String intro) {
        if(view == tv_introduction){
            tv_introduction.setText(intro);
        }else if(view == tvCommunity){
            tvCommunity.setText(intro);
        }
    }

}
