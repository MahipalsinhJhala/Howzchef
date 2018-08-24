package com.houz.chef.fragment;



import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.houz.chef.R;

public class FragmentHandler {

    public static void replaceFragment(AppCompatActivity activity, Fragment fragment, Bundle bundle, boolean isAddToBackStack, String tag, ANIMATION_TYPE animationType) {
        if (bundle != null)
            fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, null);
        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(tag);

        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void addFragment(AppCompatActivity activity, Fragment fragment, Fragment fragmentToTarget, Bundle bundle, boolean isAddToBackStack, String tag, int requestcode, ANIMATION_TYPE animationType) {
        if (bundle != null)
            fragment.setArguments(bundle);
        if (fragmentToTarget != null)
            fragment.setTargetFragment(fragmentToTarget, requestcode);

        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(tag);

        fragmentTransaction.commit();
    }

    public static void replaceChildFragment(int containerId, Fragment currentFragment, Fragment fragment, Bundle bundle, boolean isAddToBackStack, String tag, ANIMATION_TYPE animationType) {
        if (bundle != null)
            fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = currentFragment.getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(tag);

        fragmentTransaction.commitAllowingStateLoss();
    }

    public enum ANIMATION_TYPE {
        SLIDE_IN_LEFT, SLIDE_UP, NONE
    }

    public static void cleaerAllFragment(AppCompatActivity activity) {
        activity.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}