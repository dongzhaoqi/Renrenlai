package com.siti.renrenlai.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.adapter.AbImageShowAdapter;
import com.afollestad.materialdialogs.MaterialDialog;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.ImageHelper;
import com.siti.renrenlai.util.PhotoUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import rebus.bottomdialog.BottomDialog;

/**
 * Created by Dong on 2016/3/29.
 */

public class LaunchActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.layout_type) RelativeLayout layoutType;
    @Bind(R.id.et_subject) EditText et_subject;
    @Bind(R.id.tv_category) TextView tv_category;
    @Bind(R.id.et_time) TextView et_time;
    @Bind(R.id.tv_end) TextView et_end;
    @Bind(R.id.et_place) EditText et_place;
    @Bind(R.id.et_epople) EditText etEpople;
    @Bind(R.id.iv_cover) ImageView iv_cover;
    @Bind(R.id.layout_cover) RelativeLayout layout_cover;
    @Bind(R.id.et_detail) EditText et_detail;
    @Bind(R.id.btn_preview) Button btn_preview;
    @Bind(R.id.btn_publish) Button btn_publish;

    private Bitmap bitmap;
    private String imgName;
    private int camIndex = 0;
    private int selectIndex = 0;
    private static final int SELECT_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int CAMERA_CROP_DATA = 2;
    private ArrayList<String> mPhotoList = new ArrayList<String>();
    private GridView mGridView = null;
    private AbImageShowAdapter mImagePathAdapter = null;
    // 照相机拍照得到的图片
    private File mCurrentPhotoFile;
    /* 拍照的照片存储位置 */
    private File PHOTO_DIR = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        initViews();

        initEvent();
    }

    private void initViews() {
        initTopBarForLeft("发起活动");

        /*mPhotoList.add(String.valueOf(R.drawable.cam_photo));
        mGridView = (GridView) findViewById(R.id.myGrid);
        mImagePathAdapter = new AbImageShowAdapter(this, mPhotoList,116,116);
        mGridView.setAdapter(mImagePathAdapter);*/

    }

    private void initEvent() {

        /*mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIndex = position;
                if (selectIndex == camIndex) {
                    showPicDialog();
                }
            }
        });*/
    }

    public void showTypeDialog(){
        new MaterialDialog.Builder(this)
                .items(R.array.activity_type)
                .title(R.string.txt_category)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tv_category.setText(text);
                    }
                })
                .show();
    }

    public void showTimeDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(LaunchActivity.this).create();
        dialog.show();
        DatePicker picker = new DatePicker(LaunchActivity.this);
        picker.setDate(2015, 10);
        picker.setTodayDisplay(true);
        picker.setHolidayDisplay(false);
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                et_time.setText(date);
                dialog.dismiss();
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(picker, params);
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    public void showPicDialog() {
        BottomDialog dialog = new BottomDialog(LaunchActivity.this);
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
                        File imgFolder = new File(Environment.getExternalStorageDirectory(), "AAA");
                        imgFolder.mkdirs();
                        File img = new File(imgFolder, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg");
                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
                        Uri imgUri = Uri.fromFile(img);
                        camera.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                        startActivityForResult(camera, TAKE_PICTURE);
                        return true;
                    default:
                        return false;
                }

            }

        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == SELECT_PICTURE) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            // 使用ContentProvider通过URI获取原始图片
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
                iv_cover.setImageBitmap(photo);
            }
        } else if (requestCode == TAKE_PICTURE) {

            File imgFolder = new File(Environment.getExternalStorageDirectory(), "AAA");
            imgName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg";
            File img = new File(imgFolder, imgName);
            bitmap = PhotoUtil.getImageThumbnail(img.getAbsolutePath(), 180, 180);
            bitmap = PhotoUtil.rotaingImageView(90, bitmap);
            //bitmap = ImageHelper.getRoundedCornerBitmap(bitmap, 130);
            iv_cover.setImageBitmap(bitmap);
        }
    }


    @OnClick({R.id.layout_type, R.id.et_time, R.id.tv_end, R.id.layout_cover, R.id.btn_preview, R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_type:
                showTypeDialog();
                break;
            case R.id.et_time:
                showTimeDialog();
                break;
            case R.id.tv_end:
                showTimeDialog();
                break;
            case R.id.layout_cover:
                showPicDialog();
                break;
            case R.id.btn_preview:
                break;
            case R.id.btn_publish:
                break;
        }
    }
}