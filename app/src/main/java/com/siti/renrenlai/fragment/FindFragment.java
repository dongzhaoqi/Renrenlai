package com.siti.renrenlai.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siti.renrenlai.R;
import com.siti.renrenlai.ui.SearchActivity;
import com.siti.renrenlai.view.FragmentBase;
import com.siti.renrenlai.view.HeaderLayout;

/**
 * Created by Dong on 3/22/2016.
 */
public class FindFragment extends FragmentBase {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_find,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {

        initTopBarForRight("发现", R.drawable.search, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                //startAnimActivity(SearchActivity.class);
                showToast("搜索");
            }
        });

    }
}
