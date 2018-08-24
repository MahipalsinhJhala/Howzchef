package com.houz.chef.fragment;

import android.view.View;

import com.houz.chef.R;
import com.houz.chef.activity.DrawerActivity;
import com.houz.chef.databinding.ChatFragmentBinding;

/**
 *
 */
public class ChatListFragment extends BaseFragment {

    ChatFragmentBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.chat_fragment;
    }

    @Override
    public void init() {
        binding = (ChatFragmentBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) context).ToggleDrawer();
            }
        });

    }
}
