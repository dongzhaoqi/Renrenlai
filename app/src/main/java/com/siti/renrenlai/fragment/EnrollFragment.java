package com.siti.renrenlai.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.FragmentBase;

/**
 * Created by Dong on 2016/4/1.
 */
public class EnrollFragment extends FragmentBase {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_enroll,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
