package com.houz.chef.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.LoginActivityBinding;
import com.houz.chef.interfaces.FacebookInterface;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.LoginBean;
import com.houz.chef.modelBean.User;
import com.houz.chef.socialSignup.FaceBookLogin;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.MyApplication;
import com.houz.chef.utils.StaticField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityLogin extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, FacebookInterface {
    private FirebaseAuth mAuth;
    LoginActivityBinding binding;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    HashMap<String, String> map;
    private static final int RC_SIGN_IN = 9001;
    private static final int FACEBOOK_REQUEST_CODE = 64206;
    private FaceBookLogin faceBookLogin;
    private GoogleApiClient mGoogleApiClient;
    private String socialId = "", login_by = "",fname = "", lname = "", email = "";
    private boolean isSocial = false;

    @Override
    public int getLayoutResId() {
        return R.layout.login_activity;
    }

    private void setEnableView(boolean isEnable) {
        binding.btnFb.setEnabled(isEnable);
        binding.btnGoogle.setEnabled(isEnable);
        binding.btnSginIn.setEnabled(isEnable);
        binding.txtSignInDetail.setEnabled(isEnable);
    }

    @Override
    public void init() {
        LoginManager.getInstance().logOut();
        mAuth = ((MyApplication) getApplication()).mAuth;
        faceBookLogin = new FaceBookLogin(context, this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        binding = (LoginActivityBinding) getBindingObj();

        binding.txtSignInDetail.setText(Html.fromHtml("Don’t have account? <font color='red'>SIGN UP</font>"));

        binding.txtSignInDetail.setOnClickListener(this);
        binding.btnFb.setOnClickListener(this);
        binding.btnGoogle.setOnClickListener(this);
        binding.btnSginIn.setOnClickListener(this);
        binding.imgSideBar.setOnClickListener(this);
        binding.txtForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txt_forgot_password :
                Intent intent = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
                startActivity(intent);
                break;

            case R.id.txt_sign_in_detail :
                Intent intent1 = new Intent(ActivityLogin.this, ActivitySignup.class);
                startActivity(intent1);
                break;

            case R.id.btn_sgin_in :
                if (binding.edtEmail.getText().toString().isEmpty()) {
                    CommonUtils.toast(context, "Please enter Email");
                } else if (binding.edtPassword.getText().toString().isEmpty()){
                    CommonUtils.toast(context, "Please enter Password");
                }else if (CommonUtils.checkEmail(binding.edtEmail.getText().toString())) {
                    CommonUtils.toast(context, "Please enter valid email");
                } else {
                    callSignInApi();
                }
                break;

            case R.id.btn_fb :
                clickFb();
                break;

            case R.id.btn_google :
                clickGoogle();
                break;

            case R.id.img_side_bar :
                finish();
                break;
        }
    }

    private void callSignInApi() {
        if (CommonUtils.isInternetOn(context)) {

            map = new HashMap<>();
            map.put("email", binding.edtEmail.getText().toString());
            map.put("password", binding.edtPassword.getText().toString());

            binding.progress.setVisibility(View.VISIBLE);
            Observable<LoginBean> signupusers = FetchServiceBase.getFetcherService(context)
                    .customeSignIn(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LoginBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
//                            CommonUtils.toast(context, e.getMessage());
                            Log.e("SignIn error ",""+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(LoginBean loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus().equalsIgnoreCase("true")) {
                                if(loginBean != null) {
                                    preferences.saveUserData(loginBean.getUser());
                                    if(loginBean.getUser().getFcm_token() != null)
                                        preferences.setSessionToken(loginBean.getUser().getFcm_token());
                                    Intent intent = new Intent(ActivityLogin.this,DrawerActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {

                                if(loginBean.getStatus_code().equals("1013")){
                                    numberVerify(loginBean.getUser());
                                }else {

                                    CommonUtils.toast(context, loginBean.getError());

                                }

                            }
                        }
                    });

        } else {

            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void numberVerify(final AboutMe aboutMe) {
        final boolean[] isSendFirstTime = {true};
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                binding.progress.setVisibility(View.GONE);
                Intent intent = new Intent(ActivityLogin.this,DrawerActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                ((MyApplication) getApplication()).setmCallbacks(mCallbacks);
                ((MyApplication) getApplication()).setVerificationId(verificationId);
                ((MyApplication) getApplication()).setToken(token);

//                if (isSendFirstTime[0]) {
                binding.progress.setVisibility(View.GONE);
//                    isSendFirstTime[0] = false;
                Intent intent = new Intent(context, ActivityVerification.class);
                intent.putExtra(StaticField.ARG_MOBILE_NUMBER, "+91"+"9737724326");
                intent.putExtra(StaticField.ARG_EMAIL, binding.edtEmail.getText().toString());
                startActivity(intent);
                finish();
//                }
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+"9737724326",        // Phone number to verify
                60, TimeUnit.SECONDS, this, mCallbacks);
    }


    private void clickGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void clickFb() {
        if (CommonUtils.isInternetOn(context)) {
            if(faceBookLogin != null)
                faceBookLogin.faceBookManager(context);
        } else
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FACEBOOK_REQUEST_CODE:
                    if(faceBookLogin != null)
                        faceBookLogin.onResult(requestCode, resultCode, data);
                    else
                        setEnableView(true);
                    break;

                case RC_SIGN_IN:
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    if (result.isSuccess()) {
                        GoogleSignInAccount account = result.getSignInAccount();
                        firebaseAuthWithGoogle(account);
                    } else
                        setEnableView(true);
                    break;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setEnableView(true);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        if(mAuth != null) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                setApiValueForGoogleSignup(mAuth.getCurrentUser());
                            } else if (task.getException() != null) {
                                setEnableView(true);
                                CommonUtils.toast(context, task.getException().getMessage());
                            }
                        }
                    });
        } else
            setEnableView(true);
    }

    private void setApiValueForGoogleSignup(FirebaseUser user) {
        isSocial = true;
        if (user.getEmail() != null)
            email = user.getEmail();
        socialId = user.getUid();
        login_by = "google";
        if(user.getDisplayName() != null) {
            String[] separated = user.getDisplayName().split(" ");

            if (separated.length > 0)
                fname = separated[0];
            if (separated.length > 1)
                lname = separated[1];

        }

        callSocialSignUpApi();
    }

    @Override
    public void success(Map<String, String> map1) {
        Log.e("FB account details "," "+map1.toString());

        isSocial = true;
        if (map1.containsKey("firstName") && map1.get("firstName") != null)
            fname = map1.get("firstName");
        if (map1.containsKey("lastName") && map1.get("lastName") != null)
            lname = map1.get("lastName");
        if (map1.containsKey("email") && map1.get("email") != null)
            email = map1.get("email");

        socialId = map1.get("socialId");
        login_by = "facebook";

        callSocialSignUpApi();
    }

    @Nullable
    @Override
    public void onFbFrdFetch(ArrayList<Object> list) {
    }

    private void callSocialSignUpApi() {
        if (CommonUtils.isInternetOn(context)) {

            String isChecked = binding.chkCustomer.isChecked() ? "false" : "true";

            map = new HashMap<>();
            map.put("first_name", fname);
            map.put("last_name", lname);
            map.put("phone", "");
            map.put("email", email);
            map.put("is_chef", isChecked);
            map.put("device_type", "android");
            map.put("login_by", login_by);
            map.put("social_id", socialId);

            if (preferences.getPushToken() != null) {
                map.put("fcm_token", preferences.getPushToken());
            } else {
                map.put("fcm_token", "123456789");
            }

            binding.progress.setVisibility(View.VISIBLE);
            Observable<LoginBean> signupusers = FetchServiceBase.getFetcherService(context)
                    .customeSignUp(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LoginBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Signup error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(LoginBean loginBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (loginBean.getStatus().equalsIgnoreCase("true")) {
                                if (loginBean != null) {
                                    preferences.saveUserData(loginBean.getUser());
                                    if (loginBean.getUser().getFcm_token() != null) {
                                        preferences.setSessionToken(loginBean.getUser().getFcm_token());
                                        Intent intent = new Intent(ActivityLogin.this,DrawerActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            } else
                                CommonUtils.toast(context, loginBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }

    }

    @Override
    protected void onDestroy() {
        if (subscription != null)
            subscription.unsubscribe();
        super.onDestroy();
    }
}
