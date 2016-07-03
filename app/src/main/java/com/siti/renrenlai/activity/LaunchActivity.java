package com.siti.renrenlai.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ImageAdapter;
import com.siti.renrenlai.adapter.PictureAdapter;
import com.siti.renrenlai.adapter.SpinnerProjectAdapter;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.bean.ActivityImagePre;
import com.siti.renrenlai.bean.ProjectBaseInfo;
import com.siti.renrenlai.db.DbTempActivity;
import com.siti.renrenlai.util.Bimp;
import com.siti.renrenlai.util.BitmapUtils;
import com.siti.renrenlai.util.CommonUtils;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.DateTimePicker;
import com.siti.renrenlai.util.ImageHelper;
import com.siti.renrenlai.util.PhotoUtil;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.NoScrollGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rebus.bottomdialog.BottomDialog;

/**
 * Created by Dong on 2016/3/29.
 */

public class LaunchActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.layout_type)
    RelativeLayout layoutType;
    @Bind(R.id.tv_activity_type)
    TextView tv_activity_type;
    @Bind(R.id.ll_interest)
    LinearLayout ll_interest;
    @Bind(R.id.ll_help)
    LinearLayout ll_help;
    @Bind(R.id.ll_advice)
    LinearLayout ll_advice;

    @Bind(R.id.et_subject)
    EditText et_subject;
    @Bind(R.id.tv_start_time)
    TextView tv_start_time;
    @Bind(R.id.tv_end_time)
    TextView tv_end_time;
    @Bind(R.id.tv_deadline)
    TextView tv_deadline;
    @Bind(R.id.et_place)
    EditText et_place;
    @Bind(R.id.et_epople)
    EditText et_people;
    @Bind(R.id.layout_projects)
    RelativeLayout layout_projects;
    @Bind(R.id.tv_project_name)
    TextView tv_project_name;
    @Bind(R.id.layout_cover)
    RelativeLayout layout_cover;
    @Bind(R.id.noScrollgridview)
    NoScrollGridView noScrollGridView;
    @Bind(R.id.et_detail)
    EditText et_detail;
    @Bind(R.id.btn_preview)
    Button btn_preview;
    @Bind(R.id.btn_publish)
    Button btn_publish;

    public static Bitmap bitmap;
    @Bind(R.id.iv_activity)
    ImageView ivActivity;
    @Bind(R.id.iv_charity)
    ImageView ivCharity;
    @Bind(R.id.iv_discuss)
    ImageView ivDiscuss;
    private String imgName;
    private int activity_type = 0;
    int projectId;
    private static final int SELECT_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int FROM_IMAGE_LIB = 2;
    private PictureAdapter picAdapter;
    // 照相机拍照得到的图片
    private File mCurrentPhotoFile;
    /* 拍照的照片存储位置 */
    private File PHOTO_DIR = null;
    private static final String TAG = "LaunchActivity";
    private List<ProjectBaseInfo> projectList;
    private List<ActivityImagePre> imagePreList;
    private ArrayList<String> imagePathList;
    private DbManager db;
    private DbTempActivity dbTempActivity;
    private ImageAdapter imageAdapter;
    int flag_gallery;
    int flag_image_lib;
    int width, height;

    ArrayList<String> imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initProjects();
        initViews();

        initData();
    }

    private void initViews() {
        initTopBarForLeft("发起活动");
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());
        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        picAdapter = new PictureAdapter(this);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == Bimp.getTempSelectBitmap().size()) {
                    showPicDialog();
                } else {
                    Intent intent = new Intent(LaunchActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });
        noScrollGridView.setAdapter(picAdapter);
    }

    /**
     * 若数据库中有保存的活动信息, 从数据库中恢复.
     */
    private void initData() {
        try {
            dbTempActivity = db.selector(DbTempActivity.class).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (dbTempActivity != null) {
            activity_type = dbTempActivity.getActivityType();
            et_subject.setText(dbTempActivity.getActivityName());
            tv_start_time.setText(dbTempActivity.getActivityStartTime());
            tv_end_time.setText(dbTempActivity.getActivityEndTime());
            tv_deadline.setText(dbTempActivity.getActivityDeadLineTime());
            et_place.setText(dbTempActivity.getActivityAddress());
            et_people.setText(dbTempActivity.getActivityPeopleNums());
            tv_project_name.setText(dbTempActivity.getProjectName());
            et_detail.setText(dbTempActivity.getActivityDetail());
            if (activity_type == 1) {
                ll_interest.setSelected(true);
            } else if (activity_type == 2) {
                ll_help.setSelected(true);
            } else if (activity_type == 3) {
                ll_advice.setSelected(true);
            }
        }
    }


    /**
     * 获得该用户下的所有项目.
     */
    public void initProjects() {
        String url = ConstantValue.GET_LAUNCHED_PROJECTS;
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getProjects(response);
                        Log.d("response", "jsonobject:" + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    public void getProjects(JSONObject response) {
        JSONArray result = response.optJSONArray("result");
        Log.d("response", "result:" + result);
        if (result == null || result.length() == 0) {
            layout_projects.setVisibility(View.GONE);
        } else {
            projectList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), ProjectBaseInfo.class);
        }
    }

    /**
     * 弹出时间选择对话框
     * 开始时间、结束时间、报名截止
     *
     * @param v
     */
    public void showTimeDialog(View v) {

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final int id = v.getId();
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_OF_DAY);
        picker.setRange(2000, 2030);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                Date date = new Date();
                try {
                    date = sdf.parse(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (id == R.id.tv_start_time) {
                    tv_start_time.setText(sdf.format(date));
                } else if (id == R.id.tv_end_time) {
                    tv_end_time.setText(sdf.format(date));
                    if (CommonUtils.compareDate(tv_end_time.getText().toString(), tv_start_time.getText().toString())) {
                        Toast.makeText(LaunchActivity.this, "结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
                        tv_end_time.setText("");
                        return;
                    }
                } else {
                    tv_deadline.setText(sdf.format(date));
                    if (CommonUtils.compareDate(tv_start_time.getText().toString(), tv_deadline.getText().toString())) {
                        Toast.makeText(LaunchActivity.this, "报名截止时间不能晚于开始时间", Toast.LENGTH_SHORT).show();
                        tv_deadline.setText("");
                        return;
                    }
                }
            }
        });
        picker.show();
    }

    /**
     * 从相册选择或拍照弹出框
     */
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
                        flag_gallery = 3;
                        startAnimActivity(AlbumActivity.class);
                        flag_image_lib = 0;
                        return true;
                    case R.id.item_take_pic:
                        //goCamera();
                        flag_image_lib = 4;
                        getImagesFromLib();
                        flag_gallery = 0;
                        return true;
                    default:
                        return false;
                }

            }

        });
        dialog.show();
    }

    /**
     * 调用拍照
     */
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
     * 从库图片中选择
     */
    private void getImagesFromLib() {
        String url = ConstantValue.GET_IMAGES_FROM_LIB;
        final Intent intent = new Intent(LaunchActivity.this, LibImageActivity.class);
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        imagePreList = com.alibaba.fastjson.JSONArray.parseArray(
                                response.optJSONArray("result").toString(), ActivityImagePre.class);
                        intent.putExtra("libImageList", (Serializable) imagePreList);
                        startActivityForResult(intent, FROM_IMAGE_LIB);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                showToast("出错了!");
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
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
                ////iv_cover.setImageBitmap(photo);
            }
        } else if (requestCode == TAKE_PICTURE) {

            File imgFolder = new File(Environment.getExternalStorageDirectory(), "RenrenLai");
            imgName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg";
            File img = new File(imgFolder, imgName);
            bitmap = PhotoUtil.getImageThumbnail(img.getAbsolutePath(), 180, 180);
            bitmap = PhotoUtil.rotaingImageView(90, bitmap);
        } else if (requestCode == FROM_IMAGE_LIB) {
            imagePathList = data.getStringArrayListExtra("imagePathList");
            for (String str : imagePathList) {
                Log.d(TAG, "==========> " + str);
            }
            imageAdapter = new ImageAdapter(this, imagePathList);
            noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            /*noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(LaunchActivity.this, GalleryActivity.class);
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            });*/
            noScrollGridView.setAdapter(imageAdapter);
        }
    }


    @OnClick({R.id.layout_type, R.id.ll_interest, R.id.ll_help, R.id.ll_advice, R.id.tv_start_time, R.id.tv_end_time,
            R.id.tv_project_name, R.id.layout_cover, R.id.tv_deadline, R.id.btn_preview, R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_interest:
                unselectedAll();
                ll_interest.setSelected(true);
                ivActivity.setSelected(true);
                activity_type = 1;
                break;
            case R.id.ll_help:
                unselectedAll();
                ll_help.setSelected(true);
                ivCharity.setSelected(true);
                activity_type = 2;
                break;
            case R.id.ll_advice:
                unselectedAll();
                ll_advice.setSelected(true);
                ivDiscuss.setSelected(true);
                activity_type = 3;
                break;
            case R.id.tv_start_time:
                showTimeDialog(view);
                break;
            case R.id.tv_end_time:
                showTimeDialog(view);
                break;
            case R.id.tv_deadline:
                showTimeDialog(view);
                break;
            case R.id.tv_project_name:
                showProjectsDialog();
                break;
            case R.id.layout_cover:
                showPicDialog();
                break;
            case R.id.btn_preview:
                Intent previewIntent = new Intent(LaunchActivity.this, PreviewActivity.class);
                imgs = new ArrayList<>();
                if (flag_gallery == 3) {
                    int picCount = picAdapter.getCount();
                    System.out.println("count:" + picCount);
                    for (int i = 0; i < picCount - 1; i++) {
                        System.out.println("path:" + Bimp.getTempSelectBitmap().get(i).getPath());
                        imgs.add(Bimp.getTempSelectBitmap().get(i).getPath());
                    }
                } else if (flag_image_lib == 4) {
                    int picCount = imageAdapter.getCount();
                    for (int i = 0; i < picCount; i++) {
                        imgs.add(imagePathList.get(i));
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", imgs);
                bundle.putSerializable("activity", getActivityInfo());
                previewIntent.putExtras(bundle);
                startActivity(previewIntent);
                break;
            case R.id.btn_publish:
                publishActivity();
                break;
        }
    }

    public void showProjectsDialog() {
        final PopupWindow popupWindow;
        View view = LayoutInflater.from(this).inflate(R.layout.list_project, null);
        ListView listView = (ListView) view.findViewById(R.id.ll_project);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.base_edit_input));
        popupWindow.setOutsideTouchable(true);
        /**
         * 设置弹出框的宽、高
         */
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        width = (int) (display.getWidth() * 0.675); // 设置宽度
        height = LinearLayout.LayoutParams.WRAP_CONTENT;// 设置高度

        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        listView.setAdapter(new SpinnerProjectAdapter(this, projectList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                projectId = projectList.get(position).getProjectId();
                tv_project_name.setText(projectList.get(position).getProjectName());
                popupWindow.dismiss();
            }
        });
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(tv_project_name, 0, 30);
        }
    }

    /**
     * 发布活动
     */
    public void publishActivity() {

        if (activity_type == 0) {
            Toast.makeText(LaunchActivity.this, "请选择活动类型", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(et_subject.getText().toString().trim())) {
            Toast.makeText(LaunchActivity.this, "请填写活动主题", Toast.LENGTH_SHORT).show();
            et_subject.requestFocus();
            return;
        }
        if ("".equals(tv_start_time.getText().toString())) {
            Toast.makeText(LaunchActivity.this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(tv_end_time.getText().toString())) {
            Toast.makeText(LaunchActivity.this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(tv_deadline.getText().toString())) {
            Toast.makeText(LaunchActivity.this, "请选择报名截止时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(et_place.getText().toString().trim())) {
            Toast.makeText(LaunchActivity.this, "请填写活动地点", Toast.LENGTH_SHORT).show();
            et_place.requestFocus();
            return;
        }
        if ("".equals(et_people.getText().toString())) {
            Toast.makeText(LaunchActivity.this, "请填写活动人数", Toast.LENGTH_SHORT).show();
            et_people.requestFocus();
            return;
        }

        if ("".equals(et_detail.getText().toString())) {
            Toast.makeText(LaunchActivity.this, "请填写活动详情", Toast.LENGTH_SHORT).show();
            et_people.requestFocus();
            return;
        }

        showProcessDialog("发布中");
        Log.d(TAG, "publishActivity() returned: ");
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        String api = null;

        JSONObject activityContent = new JSONObject();
        try {
            activityContent.put("userName", userName);
            activityContent.put("activityName", et_subject.getText().toString());
            activityContent.put("beginTimeOfActivity", tv_start_time.getText().toString());
            activityContent.put("endTimeOfActivity", tv_end_time.getText().toString());
            activityContent.put("signUpDeadLine", tv_deadline.getText().toString());
            activityContent.put("activityAddress", et_place.getText().toString());
            activityContent.put("activityDescrip", et_detail.getText().toString());
            activityContent.put("activityTypeId", activity_type);
            activityContent.put("groupId", 2);
            activityContent.put("projectId", projectId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ConstantValue.LAUNCH_ACTIVITY;
        System.out.println("url:" + url);
        JsonObjectRequest req = new JsonObjectRequest(url, activityContent,
                new Response.Listener<JSONObject>() {
                    public String activity_id;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response:" + response.toString());
                        try {
                            activity_id = response.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String path, imageName;
                        if (flag_gallery == 3) {
                            Log.d(TAG, "flag_gallery: " + flag_gallery);
                            for (int i = 0; i < picAdapter.getCount() - 1; i++) {
                                path = Bimp.getTempSelectBitmap().get(i).getPath();
                                imageName = path.substring(path.lastIndexOf("/") + 1);
                                //imageName = UUID.randomUUID().toString().substring(0, 16);
                                File imgFile = new File(path);
                                if (imgFile.exists()) {
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    upload(activity_id, myBitmap, imageName);
                                }
                            }
                        } else if (flag_image_lib == 4) {
                            Log.d(TAG, "flag_image_lib: " + flag_image_lib);
                            for (int i = 0; i < imageAdapter.getCount() - 1; i++) {
                                path = imagePathList.get(i);
                                imageName = path.substring(path.lastIndexOf("/") + 1);
                                Bitmap myBitmap = BitmapUtils.loadBitmap(path);
                                upload(activity_id, myBitmap, imageName);
                            }
                        }
                        dismissProcessDialog();
                        finish();
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
     * 上传图片
     *
     * @param activity_id 活动id(可关联多张照片)
     * @param bitmap      上传的bitmap图片
     * @param imageName   图片名字
     */
    private void upload(String activity_id, Bitmap bitmap, String imageName) {
        String url = ConstantValue.UPLOAD_IMAGES;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("activityId", activity_id);
            jsonObject.put("imageName", imageName);
            jsonObject.put("imageData", Bitmap2StrByBase64(bitmap));
            Log.d(TAG, "upload: jsonObject：" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "response:" + response.toString());
                        if (db != null) {
                            try {
                                db.delete(DbTempActivity.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                        showToast("发布成功!");
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

    public Activity getActivityInfo() {
        Activity activity = new Activity();
        activity.setActivityType(activity_type);
        activity.setActivityName(et_subject.getText().toString());
        activity.setActivityStartTime(tv_start_time.getText().toString());
        activity.setActivityEndTime(tv_end_time.getText().toString());
        activity.setDeadline(tv_deadline.getText().toString());
        activity.setActivityAddress(et_place.getText().toString());
        activity.setParticipateNum(et_people.getText().toString());
        if (projectList.size() > 0) {
            activity.setProjectName(tv_project_name.getText().toString());
        }
        activity.setActivityDetailDescrip(et_detail.getText().toString());

        return activity;
    }

    /**
     * 按后退键时，将信息保存到数据库中.
     */
    private void save() {

        Activity activity = getActivityInfo();
        DbTempActivity dbTempActivity = new DbTempActivity();
        dbTempActivity.setActivityType(activity.getActivityType());
        dbTempActivity.setActivityName(activity.getActivityName());
        dbTempActivity.setActivityStartTime(activity.getActivityStartTime());
        dbTempActivity.setActivityEndTime(activity.getActivityEndTime());
        dbTempActivity.setActivityDeadLineTime(activity.getDeadline());
        dbTempActivity.setActivityAddress(activity.getActivityAddress());
        dbTempActivity.setActivityPeopleNums(activity.getParticipateNum());
        dbTempActivity.setProjectId(projectId);
        dbTempActivity.setProjectName(tv_project_name.getText().toString());
        dbTempActivity.setActivityDetail(activity.getActivityDetailDescrip());
        try {
            db.delete(DbTempActivity.class);
            db.save(dbTempActivity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择活动类型
     */
    public void unselectedAll() {
        ll_interest.setSelected(false);
        ll_help.setSelected(false);
        ll_advice.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        picAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        save();
        Toast.makeText(LaunchActivity.this, "已保存为草稿", Toast.LENGTH_SHORT).show();
    }
}