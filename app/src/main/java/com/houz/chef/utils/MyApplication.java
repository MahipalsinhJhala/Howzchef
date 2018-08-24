package com.houz.chef.utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class MyApplication extends MultiDexApplication {
    public PhoneAuthProvider.ForceResendingToken token;
    public String verificationId;

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.CRASHLYTICS_ENABLED).build();
//        //Fabric.with(this, new Crashlytics.Builder().core(core).build());
//        Fabric.with(this, new Crashlytics());
//
//        PaystackSdk.initialize(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
//        EmojiManager.install(new IosEmojiProvider());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public PhoneAuthProvider.ForceResendingToken getToken() {
        return token;
    }

    public void setToken(PhoneAuthProvider.ForceResendingToken token) {
        this.token = token;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks getmCallbacks() {
        return mCallbacks;
    }

    public void setmCallbacks(PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        this.mCallbacks = mCallbacks;
    }
}
