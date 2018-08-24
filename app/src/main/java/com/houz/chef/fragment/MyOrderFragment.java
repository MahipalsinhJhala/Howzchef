package com.houz.chef.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.activity.DrawerActivity;
import com.houz.chef.adapter.Fragment2PagerAdapter;
import com.houz.chef.adapter.Fragment2PagerMyOrderAdapter;
import com.houz.chef.adapter.OrderPagerAdapter;
import com.houz.chef.databinding.MyorderFragmentBinding;

/**
 *
 */
public class MyOrderFragment extends BaseFragment {

    MyorderFragmentBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.myorder_fragment;
    }

    @Override
    public void init() {
        binding = (MyorderFragmentBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) context).ToggleDrawer();
            }
        });


        binding.viewPager.setAdapter(new OrderPagerAdapter(getFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.red));

        binding.tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        binding.tabLayout.setTabTextColors(Color.parseColor("#c9c9c9"), Color.parseColor("#000000"));
        setupTabLayout();

    }

    private void setupTabLayout() {

        try {

            TabLayout.Tab tab = binding.tabLayout.getTabAt(0);
            RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_tab, null);
            TextView chefText = (TextView) tabOne.findViewById(R.id.txt_tab);
            chefText.setText("Past Orders");
            tab.setCustomView(tabOne);

            TabLayout.Tab tab1 = binding.tabLayout.getTabAt(1);
            RelativeLayout tabTwo = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_tab, null);
            TextView recipeText = (TextView) tabTwo.findViewById(R.id.txt_tab);
            recipeText.setText("Current Orders");
            tab1.setCustomView(tabTwo);

        }catch (Exception e)
        {
            Log.e("setupTabLayout ",e.getMessage());
        }
    }
}
