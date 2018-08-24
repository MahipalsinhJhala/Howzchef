package com.houz.chef.fragment;

import android.content.Intent;
import android.view.View;

import com.houz.chef.R;
import com.houz.chef.activity.ActivityAboutUs;
import com.houz.chef.activity.ActivityInviteFriends;
import com.houz.chef.activity.ActivityMyAddress;
import com.houz.chef.activity.ActivityMyProfile;
import com.houz.chef.activity.ActivitySettingNotification;
import com.houz.chef.activity.DrawerActivity;
import com.houz.chef.activity.TermsConditionActivity;
import com.houz.chef.databinding.SettingFragmentBinding;
import com.houz.chef.modelBean.AboutMe;
import com.squareup.picasso.Picasso;

/**
 * Created by m.j on 6/13/2018.
 */

public class FrSetting extends BaseFragment implements View.OnClickListener {

    SettingFragmentBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.setting_fragment;
    }

    @Override
    public void init() {
        binding = (SettingFragmentBinding) getBindingObj();
        setListener();
        setData();
    }

    private void setListener() {
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) context).ToggleDrawer();
            }
        });
        binding.rlUserImage.setOnClickListener(this);
        binding.rlNotification.setOnClickListener(this);
        binding.rlAboutUs.setOnClickListener(this);
        binding.rlContactUs.setOnClickListener(this);
        binding.rlInviteFriend.setOnClickListener(this);
        binding.rlMyAddress.setOnClickListener(this);
        binding.rlTermCondition.setOnClickListener(this);
    }

    private void setData(){
        AboutMe aboutMe=preferences.getUserDataPref();
        binding.tvUserName.setText(aboutMe.getFirst_name()+" "+aboutMe.getLast_name());
        binding.tvEmail.setText(aboutMe.getEmail());
        if(aboutMe.getFcm_token()!=null && !aboutMe.getProfile().isEmpty()){
            Picasso.with(getContext())
                    .load(aboutMe.getProfile())
                    .placeholder(R.drawable.user_placeholder)
                    .into(binding.ivUser);
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            case R.id.rl_user_image:
                intent = new Intent(getContext(), ActivityMyProfile.class);
                startActivity(intent);
                break;

            case R.id.rl_notification:
                intent = new Intent(getContext(), ActivitySettingNotification.class);
                startActivity(intent);
                break;

            case R.id.rl_about_us:
                intent = new Intent(getContext(), ActivityAboutUs.class);
                startActivity(intent);
                break;

            case R.id.rl_contact_us:

                break;

            case R.id.rl_invite_friend:
                intent = new Intent(getContext(), ActivityInviteFriends.class);
                startActivity(intent);
                break;

            case R.id.rl_my_address:
                intent = new Intent(getContext(), ActivityMyAddress.class);
                intent.putExtra("hidePayment",true);
                startActivity(intent);
                break;

            case R.id.rl_term_condition:
                intent = new Intent(getContext(), TermsConditionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
