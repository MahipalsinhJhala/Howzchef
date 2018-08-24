package com.houz.chef.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.databinding.ActivityAboutUsBinding;
import com.houz.chef.databinding.ActivityInviteFriendsBinding;

/**
 * Created by m.j on 7/22/2018.
 */

public class ActivityAboutUs extends BaseActivity implements View.OnClickListener{
    ActivityAboutUsBinding binding;
    private ImageView ivBack;
    private TextView tvTitle,tvTitleLeft;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void init() {
        binding=(ActivityAboutUsBinding)getBindingObj();
        tvTitle=findViewById(R.id.tv_title);
        tvTitleLeft=findViewById(R.id.tv_left_title);
        ivBack=findViewById(R.id.iv_back);
        tvTitle.setText(getString(R.string.about_us));
        setListener();
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

