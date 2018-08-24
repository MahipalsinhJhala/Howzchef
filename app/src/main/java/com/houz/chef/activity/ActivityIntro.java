package com.houz.chef.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;

import com.houz.chef.R;
import com.houz.chef.databinding.IntroActivityBinding;

/**
 *
 */
public class ActivityIntro extends BaseActivity {

    IntroActivityBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.intro_activity;
    }

    @Override
    public void init() {
        binding = (IntroActivityBinding) getBindingObj();

        binding.txtSignUpDetail.setText(Html.fromHtml("Don’t have account? <font color='red'>SIGN UP</font>"));

        binding.txtSignUpDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityIntro.this, ActivitySignup.class);
                startActivity(intent1);
            }
        });

        binding.btnSginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityIntro.this, ActivityLogin.class);
                startActivity(intent1);
            }
        });
    }
}
