package com.siti.renrenlai.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.MyPageAdapter;
import com.siti.renrenlai.util.BitmapUtils;
import com.siti.renrenlai.view.ViewPagerFixed;
import com.siti.renrenlai.view.ZoomableImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EasyShare005 on 2015/7/1.
 */
public class GalleryImageActivity extends Activity {

    @Bind(R.id.gallery_back)
    Button back_bt;
    @Bind(R.id.tv_pos)
    TextView positionTextView;      //顶部显示预览图片位置的textview
    @Bind(R.id.gallery_del)
    Button gallery_del;
    @Bind(R.id.bottom_layout)
    RelativeLayout rlBottom;
    @Bind(R.id.gallery01)
    ViewPagerFixed pager;

    private Intent intent;
    //当前的位置
    int location = 0;
    int imageSize;

    ArrayList<View> listViews = null;
    ArrayList<String> imagePath = new ArrayList<>();
    MyPageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        ButterKnife.bind(this);

        intent = getIntent();
        location = intent.getIntExtra("ID", 0);
        imagePath = intent.getStringArrayListExtra("imagePath");
        imageSize = imagePath.size();

        positionTextView.setText((location + 1) + "/" + imageSize);

        rlBottom.setVisibility(View.GONE);
        gallery_del.setVisibility(View.GONE);

        pager.setOnPageChangeListener(pageChangeListener);

        /*for (int i = 0; i < imageSize; i++) {
            initListViews(BitmapUtils.loadBitmap(imagePath.get(i)));
        }*/
        runOnUiThread(new Runnable() {
            public void run() {
                for (int i = 0; i < imageSize; i++) {
                    initListViews(BitmapUtils.loadBitmap(imagePath.get(i)));
                }
            }
        });

        adapter = new MyPageAdapter(listViews, this);
        pager.setAdapter(adapter);
        pager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.padding_10));
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }


    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
            positionTextView.setText((location + 1) + "/" + imageSize);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @OnClick(R.id.gallery_back)
    public void onClick() {
        finish();
    }

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        ZoomableImageView img = new ZoomableImageView(this);
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    /**
     * 监听返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}