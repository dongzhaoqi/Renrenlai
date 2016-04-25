package com.siti.renrenlai.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.ImageHelper;
import com.siti.renrenlai.util.PhotoUtil;

import java.io.File;
import java.io.FileNotFoundException;
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
    RelativeLayout layoutName;
    @Bind(R.id.layout_nickname)
    RelativeLayout layoutNickname;
    @Bind(R.id.layout_realname)
    RelativeLayout layoutRealname;
    @Bind(R.id.layout_gender)
    RelativeLayout layoutGender;
    @Bind(R.id.layout_tel)
    RelativeLayout layoutTel;
    @Bind(R.id.layout_introduction)
    RelativeLayout layoutIntroduction;
    @Bind(R.id.layout_password)
    RelativeLayout layout_password;
    private static final int SELECT_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private Bitmap bitmap;
    private String imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initTopBarForLeft("我的资料");
    }

    @OnClick({R.id.img_photo, R.id.layout_name, R.id.layout_nickname, R.id.layout_realname,
            R.id.layout_gender, R.id.layout_tel, R.id.layout_introduction, R.id.layout_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                showPicDialog();
                break;
            case R.id.layout_name:
                break;
            case R.id.layout_nickname:
                break;
            case R.id.layout_realname:
                break;
            case R.id.layout_gender:
                break;
            case R.id.layout_tel:
                break;
            case R.id.layout_introduction:
                break;
            case R.id.layout_password:
                startAnimActivity(ModifyPasswordActivity.class);
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
                img_photo.setImageBitmap(photo);
            }
        } else if (requestCode == TAKE_PICTURE) {

            File imgFolder = new File(Environment.getExternalStorageDirectory(), "AAA");
            imgName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg";
            File img = new File(imgFolder, imgName);
            bitmap = PhotoUtil.getImageThumbnail(img.getAbsolutePath(), 180, 180);
            bitmap = PhotoUtil.rotaingImageView(90, bitmap);
            //bitmap = ImageHelper.getRoundedCornerBitmap(bitmap, 130);
            img_photo.setImageBitmap(bitmap);
        }
    }

}
