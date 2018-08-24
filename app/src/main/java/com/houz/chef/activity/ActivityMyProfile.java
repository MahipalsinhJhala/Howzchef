package com.houz.chef.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ActivityMyProfileBinding;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.ProfileBean;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Preferences;
import com.squareup.picasso.Picasso;

import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m.j on 7/20/2018.
 */

public class ActivityMyProfile extends BaseActivity implements View.OnClickListener {

    ActivityMyProfileBinding binding;
    private ImageView ivBack, ivEdit;
    private TextView tvLeftTitle, tvTitle;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_profile;
    }

    @Override
    public void init() {
        binding = (ActivityMyProfileBinding) getBindingObj();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivEdit = (ImageView) findViewById(R.id.iv_tool_right);
        tvLeftTitle = (TextView) findViewById(R.id.tv_left_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        tvTitle.setVisibility(View.GONE);
        tvLeftTitle.setVisibility(View.VISIBLE);
        ivEdit.setImageResource(R.drawable.white_edit);
        tvTitle.setText(getString(R.string.my_profile));
        ivEdit.setVisibility(View.VISIBLE);
        setListener();
        if (!CommonUtils.isInternetOn(getApplicationContext())) {
            CommonUtils.toast(getApplicationContext(), getString(R.string.snack_bar_no_internet));
            return;
        }
        binding.progress.setVisibility(View.VISIBLE);
        AboutMe aboutMe = new Preferences(this).getUserDataPref();
        FetchServiceBase.getFetcherServiceWithToken(context)
                .callFetchProfile(aboutMe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProfileBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ProfileBean profileBean) {
                        binding.progress.setVisibility(View.GONE);
                        try {
                            Picasso.with(context).load(profileBean.getUserBean().getUser().getProfile()).error(R.drawable.user_placeholder).placeholder(R.drawable.user_placeholder).into(binding.ivUser);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        binding.tvName.setText(profileBean.getUserBean().getUser().getName());
                        binding.tvEmail.setText(profileBean.getUserBean().getUser().getEmail());
                        binding.tvPhone.setText(profileBean.getUserBean().getUser().getPhone());
                    }
                });
    }


    private void setListener() {
        ivBack.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        binding.llChangePwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_change_pwd:
                intent = new Intent(this, ActivityChangePassword.class);
                startActivity(intent);
                break;

            case R.id.iv_tool_right:
                intent = new Intent(this, ActivityEditProfile.class);
                startActivity(intent);
                break;
        }

    }
}
