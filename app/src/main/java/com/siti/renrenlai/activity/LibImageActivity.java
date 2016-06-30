package com.siti.renrenlai.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.AlbumGridViewAdapter;
import com.siti.renrenlai.adapter.MyGridViewAdapter;
import com.siti.renrenlai.bean.ActivityImagePre;
import com.siti.renrenlai.bean.ImageBean;
import com.siti.renrenlai.util.AlbumHelper;
import com.siti.renrenlai.util.Bimp;
import com.siti.renrenlai.util.ImageBucket;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/6/16.
 */
public class LibImageActivity extends Activity{

    @Bind(R.id.cancel)
    Button cancel;
    @Bind(R.id.ok_button)
    Button okButton;
    @Bind(R.id.grid_lib)
    GridView gridLib;
    private List<ActivityImagePre> libImageList;
    private ArrayList<String> imagePathList;
    //gridView的adapter
    private MyGridViewAdapter gridImageAdapter;
    public static Bitmap bitmap;
    int count = 0;              //选中的图片数量
    private static final String TAG = "LibImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_image);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        imagePathList = new ArrayList<>();
        libImageList = (List<ActivityImagePre>) getIntent().getSerializableExtra("libImageList");
        gridImageAdapter = new MyGridViewAdapter(this, libImageList);
        gridLib.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);     //设置多选模式
        gridLib.setAdapter(gridImageAdapter);
        gridImageAdapter.setOnItemClickListener(new MyGridViewAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(ToggleButton toggleButton, int position, boolean isChecked, Button chooseBt) {
                String imagePath = libImageList.get(position).getActivityImagePath();
                if (count >= 9) {
                    toggleButton.setChecked(false);
                    chooseBt.setVisibility(View.GONE);
                    if (!removeOneData(libImageList.get(position).getActivityImagePath())) {
                        Toast.makeText(LibImageActivity.this, "超出可选图片张数", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if(isChecked){
                    count++;
                    imagePathList.add(imagePath);
                    chooseBt.setVisibility(View.VISIBLE);
                    okButton.setText("完成" + "(" + count
                            + "/" + 9 + ")");
                }else{
                    count--;
                    if(imagePathList.contains(imagePath)){
                        imagePathList.remove(imagePath);
                    }
                    chooseBt.setVisibility(View.GONE);
                    okButton.setText("完成" + "(" + count + "/" + 9 + ")");
                }
            }
        });
    }

    private boolean removeOneData(String path) {
        if (imagePathList.contains(path)) {
            imagePathList.remove(path);
            count--;
            okButton.setText("完成" + "(" + count + "/" + 9 + ")");
            return true;
        }
        return false;
    }

    @OnClick({R.id.cancel, R.id.ok_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.ok_button:
                Intent intent = new Intent();
                intent.putStringArrayListExtra("imagePathList", imagePathList);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

}
