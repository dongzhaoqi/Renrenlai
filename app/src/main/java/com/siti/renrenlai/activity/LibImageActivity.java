package com.siti.renrenlai.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.AlbumGridViewAdapter;
import com.siti.renrenlai.adapter.MyGridViewAdapter;
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
public class LibImageActivity extends Activity implements AbsListView.MultiChoiceModeListener {

    @Bind(R.id.cancel)
    Button cancel;
    @Bind(R.id.ok_button)
    Button okButton;
    @Bind(R.id.grid_lib)
    GridView gridLib;
    private List<ActivityImagePre> libImageList;
    //gridView的adapter
    private MyGridViewAdapter gridImageAdapter;
    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_image);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        libImageList = (List<ActivityImagePre>) getIntent().getSerializableExtra("libImageList");
        gridImageAdapter = new MyGridViewAdapter(this, libImageList);
        gridLib.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);     //设置多选模式
        gridLib.setAdapter(gridImageAdapter);
        gridLib.setMultiChoiceModeListener(this);
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

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
