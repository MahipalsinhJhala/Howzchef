package com.houz.chef.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.houz.chef.fragment.ChefListFragment;
import com.houz.chef.fragment.RecipeListFragment;

import java.util.ArrayList;
import java.util.List;

public class Fragment2PagerMyOrderAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public Fragment2PagerMyOrderAdapter(FragmentManager fm) {
        super(fm);

        mFragmentList.add(new ChefListFragment());
        mFragmentList.add(new RecipeListFragment());
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }



    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        // Generate title based on item position
//        return tabTitles[position];
//    }
}
