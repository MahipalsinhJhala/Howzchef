package com.houz.chef.interfaces;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

public interface FacebookInterface {
    void success(Map<String, String> map);

    @Nullable
    void onFbFrdFetch(ArrayList<Object> list);
}