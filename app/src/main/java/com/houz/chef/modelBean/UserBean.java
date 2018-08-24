package com.houz.chef.modelBean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 8/3/2018.
 */

public class UserBean {
    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
