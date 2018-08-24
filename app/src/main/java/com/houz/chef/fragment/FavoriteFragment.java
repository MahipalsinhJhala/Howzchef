package com.houz.chef.fragment;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.activity.DrawerActivity;
import com.houz.chef.adapter.Fragment2PagerAdapter;
import com.houz.chef.databinding.FavoriteFragmentBinding;

/**
 *
 */
public class FavoriteFragment extends BaseFragment {

    FavoriteFragmentBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.favorite_fragment;
    }

    @Override
    public void init() {
        binding = (FavoriteFragmentBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) context).ToggleDrawer();
            }
        });

        binding.viewPager.setAdapter(new Fragment2PagerAdapter(getFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#f04a38"));
        binding.tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        binding.tabLayout.setTabTextColors(Color.parseColor("#c9c9c9"), Color.parseColor("#000000"));
        setupTabLayout();
    }

    private void setupTabLayout() {

        try {

            TabLayout.Tab tab = binding.tabLayout.getTabAt(0);
            RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_tab, null);
            TextView chefText = (TextView) tabOne.findViewById(R.id.txt_tab);
            chefText.setText("Chef");
            tab.setCustomView(tabOne);

            TabLayout.Tab tab1 = binding.tabLayout.getTabAt(1);
            RelativeLayout tabTwo = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_tab, null);
            TextView recipeText = (TextView) tabTwo.findViewById(R.id.txt_tab);
            recipeText.setText("Recipe");
            tab1.setCustomView(tabTwo);

        }catch (Exception e)
        {
            Log.e("setupTabLayout ",e.getMessage());
        }

    }
}
