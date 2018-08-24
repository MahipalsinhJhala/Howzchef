package com.houz.chef.activity;

import com.houz.chef.R;
import com.houz.chef.databinding.ChatMsgActivityBinding;

/**
 *
 */
public class ActivityChatMsg extends BaseActivity {

    ChatMsgActivityBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.chat_msg_activity;
    }

    @Override
    public void init() {
        binding = (ChatMsgActivityBinding)getBindingObj();

    }
}
