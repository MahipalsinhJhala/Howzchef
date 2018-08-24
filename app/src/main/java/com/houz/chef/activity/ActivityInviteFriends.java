package com.houz.chef.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ActivityChangePwdBinding;
import com.houz.chef.databinding.ActivityInviteFriendsBinding;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.ProfileBean;
import com.houz.chef.modelBean.ProfileResponse;
import com.houz.chef.modelBean.ReferralCodeResponse;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Preferences;
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m.j on 7/22/2018.
 */

public class ActivityInviteFriends extends BaseActivity implements View.OnClickListener {
    ActivityInviteFriendsBinding binding;
    private ImageView ivBack;
    private TextView tvTitle, tvTitleLeft;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    public void init() {
        binding = (ActivityInviteFriendsBinding) getBindingObj();
        tvTitle = findViewById(R.id.tv_title);
        tvTitleLeft = findViewById(R.id.tv_left_title);
        ivBack = findViewById(R.id.iv_back);
        tvTitle.setText(getString(R.string.invite_friends));
        setListener();
        showReferralCode();
    }

    private void showReferralCode() {
        binding.progress.setVisibility(View.VISIBLE);
        AboutMe aboutMe = new Preferences(this).getUserDataPref();
        FetchServiceBase.getFetcherServiceWithToken(context)
                .callGetReferralCode(aboutMe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReferralCodeResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ReferralCodeResponse referralCodeResponse) {
                        binding.progress.setVisibility(View.GONE);
                        binding.tvInviteCode.setText("123456"/*+referralCodeResponse.getReferralModel().getReferralCode()*/);
                    }
                });
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void onCopyCode(View view) {
        String data = binding.tvInviteCode.getText().toString().trim();
        if(TextUtils.isEmpty(data))
            return;
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Invitation Code",data);
        clipboard.setPrimaryClip(clip);
        CommonUtils.toast(view.getContext(),getString(R.string.coppied));
    }

    public void onShareViaWhatsapp(View view) {
        String data = binding.tvInviteCode.getText().toString().trim();
        if(TextUtils.isEmpty(data))
            return;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,data);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        startActivity(sendIntent);
    }

    public void onShareViaFacebook(View view) {
        String data = binding.tvInviteCode.getText().toString().trim();
        if(TextUtils.isEmpty(data))
            return;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, data);
        shareIntent.setData(Uri.parse(data));
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
                break;
            }
        }
    }

    public void onShareViaSMS(View view) {
        String data = binding.tvInviteCode.getText().toString().trim();
        if(TextUtils.isEmpty(data))
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, data);

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        }else {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", data);
            sendIntent.setData(Uri.parse("sms:" + data));
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);
        }
    }
}