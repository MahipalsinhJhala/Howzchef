package com.houz.chef.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.houz.chef.utils.Preferences;

import rx.Subscription;

public abstract class BaseFragment extends Fragment {
    private ViewDataBinding viewDataBinding;
    public Subscription subscription;
    public Preferences preferences;
    public Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new Preferences(view.getContext());
        context = getActivity();
        init();
    }

    public ViewDataBinding getBindingObj() {
        return viewDataBinding;
    }

    public abstract int getLayoutResId();

    public abstract void init();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Runtime.getRuntime().gc();
            System.gc();
        } catch(Exception ignored){
        }
    }
}
