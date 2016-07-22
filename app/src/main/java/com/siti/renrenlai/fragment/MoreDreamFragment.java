package com.siti.renrenlai.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siti.renrenlai.R;
import com.siti.renrenlai.util.SharedPreferencesUtil;

/**
 * Created by Dong on 2016/7/22.
 */
public class MoreDreamFragment extends BaseFragment {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_home,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userName");

    }
}
