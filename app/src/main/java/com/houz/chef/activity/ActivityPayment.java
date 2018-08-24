package com.houz.chef.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.PaymentActivityBinding;
import com.houz.chef.modelBean.AddAddressBean;
import com.houz.chef.modelBean.AddAddressResponse;
import com.houz.chef.modelBean.PaymentBean;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ActivityPayment extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,TextWatcher {

    private PaymentActivityBinding binding;
    private int mYear,mMonth,mDay;
    private DatePickerDialog dialog;
    private float total,discount;
    private String promoCode,address;
    @Override
    public int getLayoutResId() {
        return R.layout.payment_activity;
    }

    @Override
    public void init() {
        binding = (PaymentActivityBinding) getBindingObj();
        if(getIntent()!=null){
            if(getIntent().hasExtra(Const.TOTAL))
                total=getIntent().getFloatExtra(Const.TOTAL,0);

            if(getIntent().hasExtra(Const.PROMO_DISCOUNT))
                discount=getIntent().getFloatExtra(Const.PROMO_DISCOUNT,0);

            if(getIntent().hasExtra(Const.PROMO_CODE))
                promoCode=getIntent().getStringExtra(Const.PROMO_CODE);

            if(getIntent().hasExtra(Const.DELIVERY_ADDRESS))
                address=getIntent().getStringExtra(Const.DELIVERY_ADDRESS);
        }

        setListener();
        binding.rgMethod.setVisibility(View.GONE);
      //  binding.llCardDetail.setVisibility(View.GONE);
    }

    private void setListener() {

        binding.rgMethod.setOnCheckedChangeListener(this);
        binding.imgSideBar.setOnClickListener(this);
        binding.btnProceed.setOnClickListener(this);
        binding.edtCardNo.addTextChangedListener(this);
        binding.edtExpiration.setOnClickListener(this);
        binding.edtExpiration.setKeyListener(null);
        binding.edtExpiration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePickerDialog();
                }else {
                    hideDialog();
                }
            }
        });
    }

    private void callOrderAPI() {

        String name, card, date,cvv;

        name = binding.edtCardName.getText().toString().trim();
        card = binding.edtCardNo.getText().toString().trim();
        date = binding.edtExpiration.getText().toString().trim();
        cvv = binding.edtCvv.getText().toString().trim();

        if(name.isEmpty()){
            CommonUtils.toast(this,"Name is empty");
            return;
        }else if(card.isEmpty()){
            CommonUtils.toast(this,"Card number is empty");
            return;
        }else if(card.length()<16){
            CommonUtils.toast(this,"Invalid card number");
            return;
        }else if(date.isEmpty()){
            CommonUtils.toast(this,"Date is empty");
            return;
        }else if(cvv.isEmpty()){
            CommonUtils.toast(this,"CVV is empty");
            return;
        }else if(cvv.length()<3){
            CommonUtils.toast(this,"Invalid CVV");
            return;
        }



        if(CommonUtils.isInternetOn(this)){

            HashMap<String,String> params=new HashMap<>();
            params.put("user_id",""+preferences.getUserDataPref().getId());
            params.put("total",""+total);
            params.put("promo_code",""+promoCode);
            params.put("promo_discount",""+discount);
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("title",address);
                params.put("delivery_address",jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put("card_no",""+card.replace(" ",""));
            params.put("exp_month",""+mMonth);
            params.put("exp_year",""+mYear);
            params.put("cvv",""+cvv);
            binding.llProgress.setVisibility(View.VISIBLE);
            final Observable<PaymentBean> payment;
            payment= FetchServiceBase.getFetcherService(context)
                    .stripePayment(CommonUtils.converRequestBodyFromMap(params));

            subscription = payment.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<PaymentBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Cart error ", "" + e);
                            binding.llProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(PaymentBean paymentBean) {
                            binding.llProgress.setVisibility(View.GONE);
                            if (paymentBean.getStatus()) {

                                //CommonUtils.toast(ActivityPayment.this,paymentBean.getMessage());
                                Intent intent=new Intent(ActivityPayment.this,ActivityThankYou.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });


        }else{
            CommonUtils.toast(this,getString(R.string.snack_bar_no_internet));
        }


    }

    private void showDatePickerDialog(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        if(dialog==null) {
            dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mYear=year;
                    mMonth=month;

                    binding.edtExpiration.setText(""+month+"/"+year);
                }
            }, mYear, mMonth, mDay);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());

            try {

                Field[] datePickerDialogFields = dialog.getClass().getDeclaredFields();
                for (Field datePickerDialogField : datePickerDialogFields) {
                    if (datePickerDialogField.getName().equals("mDatePicker")) {
                        datePickerDialogField.setAccessible(true);
                        DatePicker datePicker = (DatePicker) datePickerDialogField
                                .get(dialog);
                        Field datePickerFields[] = datePickerDialogField.getType()
                                .getDeclaredFields();
                        for (Field datePickerField : datePickerFields) {
                            if ("mDayPicker".equals(datePickerField.getName())
                                    || "mDaySpinner".equals(datePickerField
                                    .getName())) {
                                datePickerField.setAccessible(true);
                                Object dayPicker = new Object();
                                dayPicker = datePickerField.get(datePicker);
                                ((View) dayPicker).setVisibility(View.GONE);
                            }
                        }
                    }

                }
            } catch (Exception ex) {
            }
        }
        dialog.show();
    }

    private void hideDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_side_bar:
                finish();
                break;

            case R.id.btn_proceed:
                CommonUtils.hideKeyboard(v);
                callOrderAPI();
                break;
            case R.id.edt_expiration:
                showDatePickerDialog();
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.rb_cod:
                binding.llCardDetail.setVisibility(View.GONE);
                break;

            case R.id.rb_uber:
                binding.llCardDetail.setVisibility(View.GONE);
                break;

            case R.id.rb_card:
                binding.llCardDetail.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String initial = s.toString();
        String processed = initial.replaceAll("\\D", "");
        processed = processed.replaceAll("(\\d{4})(?=\\d)", "$1 ");

        if (!initial.equals(processed)) {
            s.replace(0, initial.length(), processed);
        }
    }
}
