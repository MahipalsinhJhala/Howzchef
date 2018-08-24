package com.houz.chef.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.houz.chef.R;
import com.houz.chef.databinding.SplashActivityBinding;

import java.security.MessageDigest;

/**
 *
 */
public class ActivitySplash extends BaseActivity{

    private SplashActivityBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.splash_activity;
    }

    @Override
    public void init() {

        facebookHashKey(this);
        binding = (SplashActivityBinding) getBindingObj();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        startCountDownTimer();

    }

    public void facebookHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(ActivitySplash.class.getName(), "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception ignored) {
        }
    }

    private void startCountDownTimer() {

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 2500) {
                        sleep(100);
                        waited += 100;
                    }
                    if (FirebaseInstanceId.getInstance().getToken() != null) {
                        preferences.setPushToken(FirebaseInstanceId.getInstance().getToken());
                    }
                    startNextActivity();
                    ActivitySplash.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ActivitySplash.this.finish();
                }
            }
        };
        splashTread.start();
    }

    private void startNextActivity() {
//        startActivity(new Intent(ActivitySplash.this, ActivityIntro.class));
        if (preferences.getSessionToken() != null) {
            Intent intent1 = new Intent(ActivitySplash.this, DrawerActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent1);
            finishAffinity();
        } else {
            Intent intent;
            intent = new Intent(ActivitySplash.this, ActivityIntro.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finishAffinity();
        }
    }
}
