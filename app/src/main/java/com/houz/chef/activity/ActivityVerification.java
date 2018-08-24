package com.houz.chef.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.VerificationActivityBinding;
import com.houz.chef.modelBean.LoginBean;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.MyApplication;
import com.houz.chef.utils.StaticField;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ActivityVerification extends BaseActivity implements View.OnClickListener {

    VerificationActivityBinding binding;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mobileNumber = "";
    private String email = "";
    private String varificationId;
    private String otp;
    HashMap<String, String> map;

    @Override
    public int getLayoutResId() {
        return R.layout.verification_activity;
    }

    @Override
    public void init() {
        binding = (VerificationActivityBinding) getBindingObj();

        mAuth = ((MyApplication) getApplication()).mAuth;
        mCallbacks = ((MyApplication) getApplication()).getmCallbacks();
        mResendToken = ((MyApplication) getApplication()).getToken();
        varificationId = ((MyApplication) getApplication()).getVerificationId();
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(StaticField.ARG_MOBILE_NUMBER))
                mobileNumber = "+91"+getIntent().getExtras().getString(StaticField.ARG_MOBILE_NUMBER);
            if (getIntent().getExtras().containsKey(StaticField.ARG_EMAIL))
                email = getIntent().getExtras().getString(StaticField.ARG_EMAIL);
        }

        setListner();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_side_bar :
                finish();
                break;

            case R.id.txt_resend :
                resendVerificationCode(mobileNumber, mResendToken);
                break;

            case R.id.txt_close :
                binding.edtFirstNum.requestFocus();
                binding.edtFirstNum.setText("");
                binding.edtSecondNum.setText("");
                binding.edtThiredNum.setText("");
                binding.edtFourthNum.setText("");
                binding.edtFifthNum.setText("");
                binding.edtSixthNum.setText("");
                break;

            case R.id.img_clear :

                if(binding.edtSixthNum.length()==1) {
                    binding.edtSixthNum.setText("");

                }else if(binding.edtFifthNum.length()==1) {
                    binding.edtFifthNum.setText("");
                }else if(binding.edtFourthNum.length()==1) {
                    binding.edtFourthNum.setText("");
                }else if(binding.edtThiredNum.length()==1) {
                    binding.edtThiredNum.setText("");
                }else if(binding.edtSecondNum.length()==1) {
                    binding.edtSecondNum.setText("");
                }else if(binding.edtFirstNum.length()==1) {
                    binding.edtFirstNum.setText("");
                }
                break;

            case R.id.txt_zero :
                setNumberText(binding.txtZero.getText().toString());
                break;

            case R.id.txt_one :
                setNumberText(binding.txtOne.getText().toString());
                break;

            case R.id.txt_two :
                setNumberText(binding.txtTwo.getText().toString());
                break;

            case R.id.txt_three :
                setNumberText(binding.txtThree.getText().toString());
                break;

            case R.id.txt_four :
                setNumberText(binding.txtFour.getText().toString());
                break;

            case R.id.txt_five :
                setNumberText(binding.txtFive.getText().toString());
                break;

            case R.id.txt_six :
                setNumberText(binding.txtSix.getText().toString());
                break;

            case R.id.txt_seven :
                setNumberText(binding.txtSeven.getText().toString());
                break;

            case R.id.txt_eight :
                setNumberText(binding.txtEight.getText().toString());
                break;

            case R.id.txt_nine :
                setNumberText(binding.txtNine.getText().toString());
                break;
        }
    }

    private void setListner() {

        binding.imgSideBar.setOnClickListener(this);
        binding.txtClose.setOnClickListener(this);
        binding.imgClear.setOnClickListener(this);
        binding.txtResend.setOnClickListener(this);
        binding.txtZero.setOnClickListener(this);
        binding.txtOne.setOnClickListener(this);
        binding.txtTwo.setOnClickListener(this);
        binding.txtThree.setOnClickListener(this);
        binding.txtFour.setOnClickListener(this);
        binding.txtFive.setOnClickListener(this);
        binding.txtSix.setOnClickListener(this);
        binding.txtSeven.setOnClickListener(this);
        binding.txtEight.setOnClickListener(this);
        binding.txtNine.setOnClickListener(this);


   }

    private void setNumberText(String num)
    {
        if(binding.edtFirstNum.length()!=1)
            binding.edtFirstNum.setText(num);
        else if(binding.edtSecondNum.length()!=1)
            binding.edtSecondNum.setText(num);
        else if(binding.edtThiredNum.length()!=1)
            binding.edtThiredNum.setText(num);
        else if(binding.edtFourthNum.length()!=1)
            binding.edtFourthNum.setText(num);
        else if(binding.edtFifthNum.length()!=1)
            binding.edtFifthNum.setText(num);
        else if(binding.edtSixthNum.length()!=1) {
            binding.edtSixthNum.setText(num);
            binding.llProgress.setVisibility(View.VISIBLE);
            otp = binding.edtFirstNum.getText().toString() + binding.edtSecondNum.getText().toString() +
                    binding.edtThiredNum.getText().toString() + binding.edtFourthNum.getText().toString() +
                binding.edtFifthNum.getText().toString() + binding.edtSixthNum.getText().toString();

            verifyPhoneNumberWithCode(varificationId, otp);

        }
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks, token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callSignUpApi();
                        } else {
                            binding.llProgress.setVisibility(View.GONE);

                            if(task.getException() != null)
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void callSignUpApi() {

        if (CommonUtils.isInternetOn(context)) {
            map = new HashMap<>();
            map.put("email", email);
            map.put("is_verified", "1");

            binding.llProgress.setVisibility(View.VISIBLE);
            Observable<LoginBean> signupusers = FetchServiceBase.getFetcherService(context)
                    .verified(CommonUtils.converRequestBodyFromMap(map));
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
                            Log.e("Verifiy error ",""+e);
                            binding.llProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(LoginBean loginBean) {
                            binding.llProgress.setVisibility(View.GONE);
                            if (loginBean.getStatus().equalsIgnoreCase("true")) {
                                if(loginBean != null) {
                                    preferences.saveUserData(loginBean.getData());
                                    if(loginBean.getData().getFcm_token() != null)
                                        preferences.setSessionToken(loginBean.getData().getFcm_token());
                                    CommonUtils.toast(context, loginBean.getMessage());
                                    Intent intent = new Intent(ActivityVerification.this,DrawerActivity.class);
                                    startActivity(intent);
                                }
                            } else
                                CommonUtils.toast(context, loginBean.getError());
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
