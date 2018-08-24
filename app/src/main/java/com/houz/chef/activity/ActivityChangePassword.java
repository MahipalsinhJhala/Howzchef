package com.houz.chef.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ActivityChangePwdBinding;
import com.houz.chef.databinding.ActivityNotificationBinding;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.LoginBean;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Preferences;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m.j on 7/22/2018.
 */

public class ActivityChangePassword extends BaseActivity implements View.OnClickListener {

    ActivityChangePwdBinding binding;
    private ImageView ivBack;
    private TextView tvTitle, tvTitleLeft;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public void init() {
        binding = (ActivityChangePwdBinding) getBindingObj();
        tvTitle = findViewById(R.id.tv_title);
        tvTitleLeft = findViewById(R.id.tv_left_title);
        ivBack = findViewById(R.id.iv_back);
        tvTitle.setText(getString(R.string.change_password));
        setListener();
    }

    public void onChangePassword(final View view) {
        CommonUtils.hideKeyboard(view);
        String currentPassword = binding.edtCurrentPwd.getText().toString().trim();
        String newPassword = binding.edtNewPwd.getText().toString().trim();
        String confirmPassword = binding.edtConfNewPwd.getText().toString().trim();
        if (TextUtils.isEmpty(currentPassword)) {
            CommonUtils.toast(context, getString(R.string.pls_enter_current_password));
            return;
        } else if (TextUtils.isEmpty(newPassword)) {
            CommonUtils.toast(context, getString(R.string.pls_enter_new_password));
            return;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            CommonUtils.toast(context, getString(R.string.pls_enter_confirm_password));
            return;
        } else if (!newPassword.equals(confirmPassword)) {
            CommonUtils.toast(context, getString(R.string.both_password_not_same));
            return;
        }
        if(!CommonUtils.isInternetOn(getApplicationContext())){
            CommonUtils.toast(getApplicationContext(),getString(R.string.snack_bar_no_internet));
            return;
        }
        Map map = new HashMap<>();
        map.put("old_password", currentPassword);
        map.put("new_password", newPassword);
        map.put("conf_password", confirmPassword);
        AboutMe aboutMe = new Preferences(view.getContext()).getUserDataPref();
        binding.progress.setVisibility(View.VISIBLE);
        FetchServiceBase.getFetcherService(context)
                .callChangePassword(aboutMe.getId(),CommonUtils.converRequestBodyFromMap(map))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.progress.setVisibility(View.GONE);
                        e.printStackTrace();
                        CommonUtils.toast(view.getContext(), getString(R.string.psw_not_change));
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        binding.progress.setVisibility(View.GONE);
                        if (loginBean.getStatus().equalsIgnoreCase("true")) {
                            finish();
                        } else {
                            CommonUtils.toast(getApplicationContext(), loginBean.getError());
                        }
                    }
                });
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
