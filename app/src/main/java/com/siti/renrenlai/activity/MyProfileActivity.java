package com.siti.renrenlai.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.siti.renrenlai.R;
import com.siti.renrenlai.util.ImageHelper;
import com.siti.renrenlai.util.PhotoUtil;
import com.siti.renrenlai.util.SharedPreferencesUtil;

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
    RelativeLayout layout_nickname;
    @Bind(R.id.tv_nickName) TextView tv_nickName;
    @Bind(R.id.layout_community)
    RelativeLayout layout_community;
    @Bind(R.id.layout_gender)
    RelativeLayout layout_gender;
    @Bind(R.id.layout_hobby)
    RelativeLayout layout_hobby;
    @Bind(R.id.layout_introduction)
    RelativeLayout layout_introduction;
    @Bind(R.id.layout_password)
    RelativeLayout layout_password;
    @Bind(R.id.tv_gender)
    TextView tv_gender;
    @Bind(R.id.tv_hobby)
    TextView tv_hobby;
    private static final int SELECT_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private Bitmap bitmap;
    private String imgName;
    private static int MODIFY_NAME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initTopBarForLeft("我的资料");
        String nickName = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "login"), "nickName");
        tv_nickName.setText(nickName);
    }

    @OnClick({R.id.img_photo, R.id.layout_name, R.id.layout_nickname, R.id.layout_community,
            R.id.layout_gender, R.id.layout_hobby, R.id.layout_introduction, R.id.layout_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                showPicDialog();
                break;
            case R.id.layout_name:
                break;
            case R.id.layout_nickname:
                Intent intent = new Intent();
                intent.setClass(MyProfileActivity.this, EditNameActivity.class);
                startActivityForResult(intent, MODIFY_NAME);
                break;
            case R.id.layout_community:
                break;
            case R.id.layout_gender:
                showGenderDialog();
                break;
            case R.id.layout_hobby:
                showHobbyDialog();
                break;
            case R.id.layout_introduction:
                startAnimActivity(IntroductionActivity.class);
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

    public void showGenderDialog(){
        new MaterialDialog.Builder(this)
                .title(R.string.str_select)
                .items(R.array.gender)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tv_gender.setText(text);
                        return true;    // allow selection
                    }
                })
                .positiveText(R.string.okBtn)
                .show();
    }

    public void showHobbyDialog(){
        final StringBuilder[] str = new StringBuilder[1];
        new MaterialDialog.Builder(this)
                .title(R.string.str_select)
                .items(R.array.hobby)
                .itemsCallbackMultiChoice(new Integer[]{1}, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        boolean allowSelection = which.length <= 5; // limit selection to 2, the new selection is included in the which array
                        str[0] = new StringBuilder();
                        if (!allowSelection) {
                            showToast(R.string.selection_limit_reached);
                        }
                        for (int i = 0; i < which.length; i++) {
                            str[0].append(text[i]);
                            str[0].append(" ");
                        }
                        return allowSelection;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        tv_hobby.setText(str[0]);
                    }
                })
                .positiveText(R.string.okBtn)
                .alwaysCallMultiChoiceCallback() // the callback will always be called, to check if selection is still allowed
                .show();
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
        }else if(requestCode == MODIFY_NAME){
            String nickName = data.getStringExtra("nickName");
            modifyName(nickName);
        }
    }

    public void modifyName(String userName) {
        tv_nickName.setText(userName);
    }

}
