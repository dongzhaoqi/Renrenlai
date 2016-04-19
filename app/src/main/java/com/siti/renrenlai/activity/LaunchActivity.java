package com.siti.renrenlai.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.adapter.AbImageShowAdapter;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.ImageHelper;
import com.siti.renrenlai.util.PhotoUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import rebus.bottomdialog.BottomDialog;

/**
 * Created by Dong on 2016/3/29.
 */

public class LaunchActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_subject, et_place, et_detail;
    private TextView et_time, et_end;
    private Button btn_preview, btn_publish;
    private RelativeLayout layout_cover;
    private ImageView iv_cover;
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
        initViews();

        initEvent();
    }

    private void initViews() {
        initTopBarForLeft("发起活动");

        et_subject = (EditText) findViewById(R.id.et_subject);
        et_time = (TextView) findViewById(R.id.et_time);
        et_end = (TextView) findViewById(R.id.tv_end);
        et_place = (EditText) findViewById(R.id.et_place);
        et_detail = (EditText) findViewById(R.id.et_detail);

        btn_preview = (Button) findViewById(R.id.btn_preview);
        btn_publish = (Button) findViewById(R.id.btn_publish);

        layout_cover = (RelativeLayout) findViewById(R.id.layout_cover);

        /*mPhotoList.add(String.valueOf(R.drawable.cam_photo));
        mGridView = (GridView) findViewById(R.id.myGrid);
        mImagePathAdapter = new AbImageShowAdapter(this, mPhotoList,116,116);
        mGridView.setAdapter(mImagePathAdapter);*/

        iv_cover = (ImageView) findViewById(R.id.iv_cover);
    }

    private void initEvent() {
        et_time.setOnClickListener(this);
        et_end.setOnClickListener(this);

        layout_cover.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.et_time:
                showTimeDialog();
                break;
            case R.id.tv_end:
                showTimeDialog();
                break;
            case R.id.layout_cover:
                showPicDialog();
                break;
        }

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
                        File imgFolder = new File(Environment.getExternalStorageDirectory(),"AAA");
                        imgFolder.mkdirs();
                        File img = new File(imgFolder,new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg");
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


}