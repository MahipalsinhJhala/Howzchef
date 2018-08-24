package com.houz.chef.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.FacebookSdk;
import com.houz.chef.R;
import com.houz.chef.utils.Preferences;

import rx.Subscription;

public abstract class BaseActivity extends AppCompatActivity {
    private ViewDataBinding bindObject;
    public Subscription subscription;
    public Preferences preferences;
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        bindObject = DataBindingUtil.setContentView(this, getLayoutResId());
        preferences = new Preferences(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }
        context = this;
        init();
    }

    public abstract int getLayoutResId();

    public abstract void init();

    public ViewDataBinding getBindingObj() {
        return bindObject;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Runtime.getRuntime().gc();
            System.gc();
        } catch(Exception ignored){
        }
    }
}

