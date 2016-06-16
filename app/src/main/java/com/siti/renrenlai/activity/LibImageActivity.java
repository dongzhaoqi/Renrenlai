package com.siti.renrenlai.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.AlbumGridViewAdapter;
import com.siti.renrenlai.bean.ActivityImagePre;
import com.siti.renrenlai.util.AlbumHelper;
import com.siti.renrenlai.util.ImageBucket;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/6/16.
 */
public class LibImageActivity extends Activity {

    @Bind(R.id.cancel)
    Button cancel;
    @Bind(R.id.ok_button)
    Button okButton;
    @Bind(R.id.grid_lib)
    GridView gridLib;
    private List<ActivityImagePre> libImageList;
    //gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_image);
        ButterKnife.bind(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plugin_camera_no_pictures);
        libImageList = (List<ActivityImagePre>) getIntent().getSerializableExtra("libImageList");

        init();
    }

    private void init(){
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        contentList = helper.getImagesBucketList(false);

    }

    @OnClick({R.id.cancel, R.id.ok_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                break;
            case R.id.ok_button:
                break;
        }
    }
}
