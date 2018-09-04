package com.houz.chef.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ActivityNotificationBinding;
import com.houz.chef.modelBean.GetFavouriteChefList;
import com.houz.chef.modelBean.UpdateSettingResponse;
import com.houz.chef.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m.j on 7/21/2018.
 */

public class ActivitySettingNotification extends BaseActivity implements View.OnClickListener {

    ActivityNotificationBinding binding;
    private ImageView ivBack;
    private TextView tvTitle, tvTitleLeft;
    private boolean isRecipeAdded;
    private boolean isRecipeChanged;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_notification;
    }

    @Override
    public void init() {
        binding = (ActivityNotificationBinding) getBindingObj();
        tvTitle = findViewById(R.id.tv_title);
        tvTitleLeft = findViewById(R.id.tv_left_title);
        ivBack = findViewById(R.id.iv_back);
        tvTitle.setText(getString(R.string.notification));
        isRecipeAdded = preferences.getRecipeAdded() == 1;
        isRecipeChanged = preferences.getRecipeChanged() == 1;
        binding.ivRecipeAdded.setImageResource(isRecipeAdded ? R.drawable.toggle_on : R.drawable.toggle_off);
        binding.ivRecipeChanged.setImageResource(isRecipeChanged ? R.drawable.toggle_on : R.drawable.toggle_off);
        setListener();
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        binding.ivRecipeAdded.setOnClickListener(this);
        binding.ivRecipeChanged.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ivRecipeAdded:
                isRecipeAdded = !isRecipeAdded;
                if (isRecipeAdded) {
                    binding.ivRecipeAdded.setImageResource(R.drawable.toggle_on);
                } else {
                    binding.ivRecipeAdded.setImageResource(R.drawable.toggle_off);
                }
                callRecipeSetting(isRecipeAdded, isRecipeChanged);
                break;
            case R.id.ivRecipeChanged:
                isRecipeChanged = !isRecipeChanged;
                if (isRecipeChanged) {
                    binding.ivRecipeChanged.setImageResource(R.drawable.toggle_on);
                } else {
                    binding.ivRecipeChanged.setImageResource(R.drawable.toggle_off);
                }
                callRecipeSetting(isRecipeAdded, isRecipeChanged);
                break;
        }
    }

    private void callRecipeSetting(final boolean isRecipeAdded, final boolean isRecipeChanged) {
        if (CommonUtils.isInternetOn(context)) {

//            pageProduct++;
            binding.progress.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("is_recipe_added", isRecipeAdded ? String.valueOf(1) : String.valueOf(0));
            map.put("is_recipe_change", isRecipeChanged ? String.valueOf(1) : String.valueOf(0));
            map.put("is_order_update", String.valueOf(0));
            map.put("is_chat_msg", String.valueOf(0));

            Observable<UpdateSettingResponse> signupusers = FetchServiceBase.getFetcherService(context)
                    .updateSettings(preferences.getUserDataPref().getId(), CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UpdateSettingResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("RecipeListFrag error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(UpdateSettingResponse getFavouriteChefList) {
                            binding.progress.setVisibility(View.GONE);
                            preferences.setRecipeAdded(isRecipeAdded ? 1 : 0);
                            preferences.setRecipeChanged(isRecipeChanged ? 1 : 0);
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }
}
