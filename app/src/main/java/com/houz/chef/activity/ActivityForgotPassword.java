package com.houz.chef.activity;

import android.view.View;

import com.houz.chef.R;
import com.houz.chef.databinding.ForgotPasswordActivityBinding;
import com.houz.chef.utils.CommonUtils;

/**
 *
 */
public class ActivityForgotPassword extends BaseActivity {

    ForgotPasswordActivityBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.forgot_password_activity;
    }

    @Override
    public void init() {
        binding = (ForgotPasswordActivityBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtEmail.getText().toString().isEmpty())
                    CommonUtils.toast(context,"Please enter Email.");
                else
                {

                }
            }
        });

    }
}
