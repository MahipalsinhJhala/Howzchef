package com.houz.chef.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.houz.chef.fragment.ChefListFragment;
import com.houz.chef.fragment.FrOrderCompleted;
import com.houz.chef.fragment.FrOrderPending;
import com.houz.chef.fragment.RecipeListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.j on 7/8/2018.
 */

public class OrderPagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public OrderPagerAdapter(FragmentManager fm) {
        super(fm);

        mFragmentList.add(new FrOrderCompleted());
        mFragmentList.add(new FrOrderPending());
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
