package com.siti.renrenlai.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.FragmentBase;

/**
 * Created by Dong on 2016/4/1.
 */
public class FavoriteFragment extends FragmentBase {
    private View view;
    private ScrollView mScrollView;
    private Button btn_to_top;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){
        mScrollView= (ScrollView) findViewById(R.id.mScrollView);
        btn_to_top= (Button) findViewById(R.id.btn_to_top);

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(mScrollView.getScrollY() > 500){
                    btn_to_top.setVisibility(View.VISIBLE);
                }else{
                    btn_to_top.setVisibility(View.INVISIBLE);
                }
            }
        });

        btn_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator.ofInt(mScrollView, "scrollY", 0).setDuration(1000).start();
            }
        });
    }

}
