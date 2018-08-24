package com.houz.chef.activity;

import android.content.Intent;
import android.view.View;

import com.houz.chef.R;
import com.houz.chef.databinding.ActivityThankYouBinding;

/**
 * Created by m.j on 7/3/2018.
 */

public class ActivityThankYou extends BaseActivity implements View.OnClickListener{

        private ActivityThankYouBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_thank_you;
    }

    @Override
    public void init() {
        binding=(ActivityThankYouBinding)getBindingObj();

        setListener();
    }

    private void setListener(){
binding.btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_continue:
                Intent i = new Intent(this, DrawerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
