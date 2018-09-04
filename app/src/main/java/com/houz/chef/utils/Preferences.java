package com.houz.chef.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.houz.chef.R;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.LoginBean;
import com.houz.chef.modelBean.PromoDataBean;


public class Preferences {

    private Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public void setFirstTimeDialog(String token) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firstTimeDialog", token);
        editor.apply();
    }

    public String getFirstTimeDialog() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("firstTimeDialog", null);
    }

    public void setFirstTime(boolean isFirstTime) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_time_install", isFirstTime);
        editor.apply();
    }

    public boolean getFirstTime() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getBoolean("first_time_install", true);
    }

    public void setPushToken(String token) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pushtoken", token);
        editor.apply();
    }

    public String getPushToken() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("pushtoken", "");
    }

    public void setSessionToken(String uid) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sessionToken", uid);
        editor.apply();
    }

    public String getSessionToken() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("sessionToken", null);
    }

    public void setFilterTypeData(String filterTypeData) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("filterTypeData", filterTypeData);
        editor.apply();
    }

    public String getFilterTypeData() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("filterTypeData", "");
    }

    public void setRecipeAdded(int recipeAdded) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("recipeAdded", recipeAdded);
        editor.apply();
    }

    public int getRecipeAdded() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getInt("recipeAdded", 0);
    }

    public void setRecipeChanged(int recipeChanged) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("recipeChanged", recipeChanged);
        editor.apply();
    }

    public int getRecipeChanged() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getInt("recipeChanged", 0);
    }

    public void setFilterCategoryData(String filterCategoryData) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("filterCategoryData", filterCategoryData);
        editor.apply();
    }

    public String getFilterCategoryData() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("filterCategoryData", "");
    }

    public void setSortingData(String sortingData) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sortingData", sortingData);
        editor.apply();
    }

    public String getSortingData() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("sortingData", "");
    }

    public void saveUserData(AboutMe userData) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(userData);
        editor.putString("user_data_obj", json);
        editor.apply();
    }

    public AboutMe getUserDataPref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("user_data_obj", null);
        return gson.fromJson(json, AboutMe.class);
    }

    public void savePromoCode(PromoDataBean promoDataBean) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(promoDataBean);
        editor.putString("promo_code", json);
        editor.apply();
    }

    public PromoDataBean getPromoCodePref() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("promo_code", null);
        return gson.fromJson(json, PromoDataBean.class);
    }


    public void cleanAlltoken() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sessionToken", null);
        editor.apply();
    }

    public void setProfileImage(String profileImage) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profileImage", profileImage);
        editor.apply();
    }

    public String getProfileImage() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_key), Context.MODE_PRIVATE);
        return preferences.getString("profileImage", "");
    }
}
