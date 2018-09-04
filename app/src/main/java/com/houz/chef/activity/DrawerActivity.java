package com.houz.chef.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.houz.chef.R;
import com.houz.chef.adapter.DrawerAdapter;
import com.houz.chef.databinding.IntroActivityBinding;
import com.houz.chef.fragment.ChatListFragment;
import com.houz.chef.fragment.FavoriteFragment;
import com.houz.chef.fragment.FrSetting;
import com.houz.chef.fragment.FragmentHandler;
import com.houz.chef.fragment.HomeFragment;
import com.houz.chef.fragment.MyOrderFragment;
import com.houz.chef.fragment.NotificationListFragment;
import com.houz.chef.interfaces.DrawerListener;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.DrawerBean;
import com.houz.chef.modelBean.LoginBean;
import com.houz.chef.utils.Preferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DrawerActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mRlyMenuDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private AboutMe saveLogiBean;
    private Preferences preferences;
    private Context context;

    private ImageView imgProfilePic, img_count;
    private TextView txtSettings, txt_home, txt_chat, txt_favorite, txt_my_order, txt_notification;
    private TextView txtLogout;
    private LinearLayout ll_notification;


    public String oldSelected = "", newSelected = "";
    private int strTo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }
        setContentView(R.layout.activity_drawer);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = new Preferences(context);
        if (preferences.getUserDataPref() != null)
            saveLogiBean = preferences.getUserDataPref();
        else
            saveLogiBean = new AboutMe();

        mRlyMenuDrawer = (RelativeLayout) findViewById(R.id.rly_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        imgProfilePic = (ImageView) findViewById(R.id.img_profile);
        try {
            Picasso.with(DrawerActivity.this).load(preferences.getProfileImage()).error(R.drawable.user_placeholder).placeholder(R.drawable.user_placeholder).into(imgProfilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setData();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!newSelected.equalsIgnoreCase(oldSelected)) {
                    oldSelected = newSelected;
                    switch (newSelected) {
                        case "Home":
                            FragmentHandler.replaceFragment(DrawerActivity.this, new HomeFragment(), null,
                                    false, "Home", FragmentHandler.ANIMATION_TYPE.NONE);
                            break;
                        case "Chat":
                            FragmentHandler.replaceFragment(DrawerActivity.this, new ChatListFragment(), null,
                                    false, "Chat", FragmentHandler.ANIMATION_TYPE.NONE);
                            break;
                        case "Favorite":
                            FragmentHandler.replaceFragment(DrawerActivity.this, new FavoriteFragment(), null,
                                    false, "Favorite", FragmentHandler.ANIMATION_TYPE.NONE);
                            break;
                        case "Notification":
                            FragmentHandler.replaceFragment(DrawerActivity.this, new NotificationListFragment(), null,
                                    false, "Notification", FragmentHandler.ANIMATION_TYPE.NONE);
                            break;
                        case "My Order":
                            FragmentHandler.replaceFragment(DrawerActivity.this, new MyOrderFragment(), null,
                                    false, "My Order", FragmentHandler.ANIMATION_TYPE.NONE);
                            break;

                        case "Settings":
                            FragmentHandler.replaceFragment(DrawerActivity.this, new FrSetting(), null,
                                    false, getString(R.string.settings), FragmentHandler.ANIMATION_TYPE.NONE);
                            break;
                    }
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        FragmentHandler.replaceFragment(DrawerActivity.this, new HomeFragment(), null,
                false, "Home", FragmentHandler.ANIMATION_TYPE.NONE);

        txtSettings = (TextView) findViewById(R.id.txt_settings);
        txtLogout = (TextView) findViewById(R.id.txt_logout);
        txt_home = (TextView) findViewById(R.id.txt_home);
        txt_chat = (TextView) findViewById(R.id.txt_chat);
        txt_favorite = (TextView) findViewById(R.id.txt_favorite);
        txt_notification = (TextView) findViewById(R.id.txt_notification);
        txt_my_order = (TextView) findViewById(R.id.txt_my_order);
        img_count = (ImageView) findViewById(R.id.img_count);
        ll_notification = (LinearLayout) findViewById(R.id.ll_notification);

        txtSettings.setOnClickListener(this);
        txtLogout.setOnClickListener(this);
        txt_home.setOnClickListener(this);
        txt_chat.setOnClickListener(this);
        txt_favorite.setOnClickListener(this);
        ll_notification.setOnClickListener(this);
        txt_my_order.setOnClickListener(this);

        setData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_settings:
                newSelected = getString(R.string.settings);
                txtSettings.setTextColor(getResources().getColor(R.color.red));
                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_chat.setTextColor(getResources().getColor(R.color.black));
                txt_favorite.setTextColor(getResources().getColor(R.color.black));
                txt_notification.setTextColor(getResources().getColor(R.color.black));
                txt_my_order.setTextColor(getResources().getColor(R.color.black));
                if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mRlyMenuDrawer))
                    mDrawerLayout.closeDrawer(mRlyMenuDrawer);
                break;

            case R.id.txt_logout:
                Preferences preferences = new Preferences(context);
                preferences.cleanAlltoken();
                preferences.setFirstTime(true);
                Intent intent = new Intent(context, ActivityIntro.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.txt_home:
                newSelected = "Home";
                txt_home.setTextColor(getResources().getColor(R.color.red));
                txtSettings.setTextColor(getResources().getColor(R.color.black));
                txt_chat.setTextColor(getResources().getColor(R.color.black));
                txt_favorite.setTextColor(getResources().getColor(R.color.black));
                txt_notification.setTextColor(getResources().getColor(R.color.black));
                txt_my_order.setTextColor(getResources().getColor(R.color.black));
                if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mRlyMenuDrawer))
                    mDrawerLayout.closeDrawer(mRlyMenuDrawer);
                break;

            case R.id.txt_chat:
                newSelected = "Chat";
                txtSettings.setTextColor(getResources().getColor(R.color.black));
                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_chat.setTextColor(getResources().getColor(R.color.red));
                txt_favorite.setTextColor(getResources().getColor(R.color.black));
                txt_notification.setTextColor(getResources().getColor(R.color.black));
                txt_my_order.setTextColor(getResources().getColor(R.color.black));
                if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mRlyMenuDrawer))
                    mDrawerLayout.closeDrawer(mRlyMenuDrawer);
                break;

            case R.id.txt_favorite:
                newSelected = "Favorite";
                txtSettings.setTextColor(getResources().getColor(R.color.black));
                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_chat.setTextColor(getResources().getColor(R.color.black));
                txt_favorite.setTextColor(getResources().getColor(R.color.red));
                txt_notification.setTextColor(getResources().getColor(R.color.black));
                txt_my_order.setTextColor(getResources().getColor(R.color.black));
                if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mRlyMenuDrawer))
                    mDrawerLayout.closeDrawer(mRlyMenuDrawer);
                break;

            case R.id.ll_notification:

                newSelected = "Notification";
                txtSettings.setTextColor(getResources().getColor(R.color.black));
                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_chat.setTextColor(getResources().getColor(R.color.black));
                txt_favorite.setTextColor(getResources().getColor(R.color.black));
                txt_notification.setTextColor(getResources().getColor(R.color.red));
                txt_my_order.setTextColor(getResources().getColor(R.color.black));
                if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mRlyMenuDrawer))
                    mDrawerLayout.closeDrawer(mRlyMenuDrawer);
                break;

            case R.id.txt_my_order:
                newSelected = "My Order";
                txtSettings.setTextColor(getResources().getColor(R.color.black));
                txt_home.setTextColor(getResources().getColor(R.color.black));
                txt_chat.setTextColor(getResources().getColor(R.color.black));
                txt_favorite.setTextColor(getResources().getColor(R.color.black));
                txt_notification.setTextColor(getResources().getColor(R.color.black));
                txt_my_order.setTextColor(getResources().getColor(R.color.red));
                if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mRlyMenuDrawer))
                    mDrawerLayout.closeDrawer(mRlyMenuDrawer);
                break;
        }
    }

    private void setData() {
        if (preferences.getUserDataPref() != null)
            saveLogiBean = preferences.getUserDataPref();
        else
            saveLogiBean = new AboutMe();

        /*if (!saveLogiBean.getProfile().isEmpty()) {
            Picasso.with(this).load(preferences.getProfileImage()).error(R.drawable.user_placeholder).placeholder(R.drawable.user_placeholder).into(imgProfilePic);
        }*/
    }

    public void ToggleDrawer() {
        if (mDrawerLayout != null && mRlyMenuDrawer != null && mDrawerLayout.isDrawerOpen(mRlyMenuDrawer))
            mDrawerLayout.closeDrawer(mRlyMenuDrawer);
        else if (mDrawerLayout != null && mRlyMenuDrawer != null)
            mDrawerLayout.openDrawer(mRlyMenuDrawer);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mRlyMenuDrawer != null && mDrawerLayout.isDrawerOpen(mRlyMenuDrawer))
            mDrawerLayout.closeDrawer(mRlyMenuDrawer);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item))
            return true;
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Runtime.getRuntime().gc();
            System.gc();
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getFragmentManager().findFragmentById(R.id.container) != null)
            getFragmentManager().findFragmentById(R.id.container).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
