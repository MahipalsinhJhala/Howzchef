package com.houz.chef.adapter;


import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.houz.chef.fragment.ChatListFragment;
import com.houz.chef.fragment.ChefListFragment;
import com.houz.chef.fragment.HomeFragment;
import com.houz.chef.fragment.RecipeListFragment;

import java.util.ArrayList;
import java.util.List;

public class Fragment2PagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public Fragment2PagerAdapter(FragmentManager fm) {
        super(fm);

        mFragmentList.add(new ChefListFragment());
        mFragmentList.add(new RecipeListFragment());
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
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
