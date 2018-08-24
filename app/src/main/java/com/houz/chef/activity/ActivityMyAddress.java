package com.houz.chef.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.adapter.MyAddressAdapter;
import com.houz.chef.apiBase.APIMethods;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ActivityMyAddressBinding;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.modelBean.AddAddressBean;
import com.houz.chef.modelBean.AddAddressResponse;
import com.houz.chef.modelBean.Address;
import com.houz.chef.modelBean.GetAddressList;
import com.houz.chef.modelBean.GetCart;
import com.houz.chef.modelBean.ModelBean;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m.j on 6/15/2018.
 */

public class ActivityMyAddress extends BaseActivity implements View.OnClickListener, OnItemClickListener {

    ActivityMyAddressBinding binding;
    private MyAddressAdapter addressAdapter;

    private ImageView ivBack;
    private TextView tvTitle;
    private int selectedIndex = -1;
    private float total,discount;
    private String promoCode;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_address;
    }

    @Override
    public void init() {

        binding = (ActivityMyAddressBinding) getBindingObj();

        if(getIntent()!=null){
            if(getIntent().getBooleanExtra("hidePayment",false)){
                binding.btnProceedPay.setVisibility(View.GONE);
            }
            if(getIntent().hasExtra(Const.TOTAL))
                total=getIntent().getFloatExtra(Const.TOTAL,0);

            if(getIntent().hasExtra(Const.PROMO_DISCOUNT))
                discount=getIntent().getFloatExtra(Const.PROMO_DISCOUNT,0);

            if(getIntent().hasExtra(Const.PROMO_CODE))
                promoCode=getIntent().getStringExtra(Const.PROMO_CODE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        if (addressAdapter == null) {
            addressAdapter = new MyAddressAdapter(this, binding.recyclerView);
        }

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);

        binding.recyclerView.setAdapter(addressAdapter);
        addressAdapter.clearAll();
        tvTitle.setText(R.string.my_address);
        callGetAddresApi();
        setListener();
    }

    private void setListener() {

        ivBack.setOnClickListener(this);
        binding.btnAddAddress.setOnClickListener(this);
        binding.btnProceedPay.setOnClickListener(this);
    }


    private void callGetAddresApi() {

        if (CommonUtils.isInternetOn(context)) {

            binding.llProgress.setVisibility(View.VISIBLE);
            Observable<GetAddressList> signupusers = FetchServiceBase.getFetcherService(context)
                    .getAddressList(preferences.getUserDataPref().getId());
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetAddressList>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Cart error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(GetAddressList getAddressList) {
                            binding.llProgress.setVisibility(View.GONE);
                            if (getAddressList.getStatus()) {

                                for (int i = 0; i < getAddressList.getData().getAddressArrayList().size(); i++) {
                                    AddAddressBean addAddressBean = getAddressList.getData().getAddressArrayList().get(i);
                                    if (addAddressBean.getIsDefault() == 1) {
                                        selectedIndex = i;
                                        break;
                                    }
                                }

                                addressAdapter.addAllList(getAddressList.getData().getAddressArrayList());

                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_add_address:
                if (addressAdapter.getItemCount() < 3) { // USER CAN ADD MAX 3 ADDRESS
                    Intent intent = new Intent(this, ActivityAddAddress.class);
                    intent.putExtra(Const.ADDRESS_COUNT, addressAdapter.getItemCount());
                    startActivityForResult(intent, Const.ADD_ADDRESS);
                } else {
                    CommonUtils.toast(this, "You can add maximum 3 addresses.");
                }
                break;

            case R.id.btn_proceed_pay:
                if(addressAdapter.getItemCount()>0) {
                    Intent intent = new Intent(this, ActivityPayment.class);
                    intent.putExtra(Const.TOTAL, total);
                    intent.putExtra(Const.PROMO_CODE, promoCode);
                    intent.putExtra(Const.PROMO_DISCOUNT, discount);
                    intent.putExtra(Const.DELIVERY_ADDRESS, addressAdapter.getItem(selectedIndex).getAddressModel().getTitle());
                    startActivity(intent);
                }
                break;
        }
    }


    private void removeDefaultAddressAPI(final int pos) {
        if (CommonUtils.isInternetOn(this)) {

            AddAddressBean addressBean = addressAdapter.getItem(selectedIndex);
            Address address = addressBean.getAddressModel();
            JSONObject params = new JSONObject();
            JSONObject jsonAddress = new JSONObject();
            try {
                jsonAddress.put("title", address.getTitle());
                jsonAddress.put("state", address.getState());
                jsonAddress.put("city", address.getCity());
                jsonAddress.put("address1", address.getAddress1());
                jsonAddress.put("address2", address.getAddress2());
                jsonAddress.put("country", address.getCountry());
                jsonAddress.put("number", address.getPhone());
                jsonAddress.put("zipcode", address.getPincode());
                params.put("address", jsonAddress);
                params.put("is_default", "0");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("address", params.toString());
            map.put("is_default", "0");
            Map<String, RequestBody> requestBodyHashMap = CommonUtils.converRequestBodyFromMap(map);
            final Observable<AddAddressResponse> addAddress;
            addAddress = FetchServiceBase.getFetcherService(context)
                    .updateAddress(preferences.getUserDataPref().getId(), addressBean.getId(), requestBodyHashMap);

            binding.llProgress.setVisibility(View.VISIBLE);
            subscription = addAddress.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddAddressResponse>() {
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
                        public void onNext(AddAddressResponse addAddressResponse) {
                            //binding.llProgress.setVisibility(View.GONE);
                            if (addAddressResponse.isStatus()) {

                                defaultAddressAPI(addAddressResponse.getAddAddressBean(),pos);
                            }
                        }
                    });
        } else {
            CommonUtils.toast(this, getString(R.string.snack_bar_no_internet));
        }
    }


    private void defaultAddressAPI(final AddAddressBean prevDefaultAddress, final int pos) {
        if (CommonUtils.isInternetOn(this)) {

            AddAddressBean addressBean = addressAdapter.getItem(pos);
            Address address = addressBean.getAddressModel();
            JSONObject params = new JSONObject();
            JSONObject jsonAddress = new JSONObject();
            try {
                jsonAddress.put("title", address.getTitle());
                jsonAddress.put("state", address.getState());
                jsonAddress.put("city", address.getCity());
                jsonAddress.put("address1", address.getAddress1());
                jsonAddress.put("address2", address.getAddress2());
                jsonAddress.put("country", address.getCountry());
                jsonAddress.put("number", address.getPhone());
                jsonAddress.put("zipcode", address.getPincode());
                params.put("address", jsonAddress);


                params.put("is_default", "1");

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            binding.llProgress.setVisibility(View.VISIBLE);

            HashMap<String, String> map = new HashMap<>();
            map.put("address", params.toString());
            map.put("is_default", "1");
            Map<String, RequestBody> requestBodyHashMap = CommonUtils.converRequestBodyFromMap(map);
            final Observable<AddAddressResponse> addAddress;
            addAddress = FetchServiceBase.getFetcherService(context)
                    .updateAddress(preferences.getUserDataPref().getId(), addressBean.getId(), requestBodyHashMap);

            subscription = addAddress.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( new Subscriber<AddAddressResponse>() {
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
                public void onNext(AddAddressResponse addAddressResponse) {
                    binding.llProgress.setVisibility(View.GONE);
                    if (addAddressResponse.isStatus()) {

                        if (selectedIndex != -1) {
                            addressAdapter.put(selectedIndex, prevDefaultAddress);

                        }
                        AddAddressBean addAddressBean = addAddressResponse.getAddAddressBean();
                        addressAdapter.put(pos, addAddressBean);
                        selectedIndex = pos;
                    }
                }
            });
        } else {
            CommonUtils.toast(this, getString(R.string.snack_bar_no_internet));
        }

    }


    private void callDeleteAddressAPI(final int pos) {
        if (CommonUtils.isInternetOn(context)) {

            binding.llProgress.setVisibility(View.VISIBLE);
            final Observable<ModelBean> addAddress;
            addAddress = FetchServiceBase.getFetcherService(context)
                    .deleteAddress(preferences.getUserDataPref().getId(), addressAdapter.getItem(pos).getId());

            subscription = addAddress.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean>() {
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
                        public void onNext(ModelBean modelBean) {
                            binding.llProgress.setVisibility(View.GONE);

                            if (modelBean.getStatus()) {
                                addressAdapter.remove(pos);
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void deleteDialog(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete")
                .setMessage("Would you like to delete this address?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        callDeleteAddressAPI(pos);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true);


        AlertDialog dialog = builder.create();

        dialog.show();
    }


    private void defaultAddressDialog(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Default Address")
                .setMessage("Would you like to make this default address?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        removeDefaultAddressAPI(pos);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true);


        AlertDialog dialog = builder.create();

        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            AddAddressBean address = data.getParcelableExtra(Const.EXTRA_OBJ);
            if (requestCode == Const.ADD_ADDRESS) {

                addressAdapter.add(address);
            } else if (requestCode == Const.UPDATE_ADDRESS) {
                int pos = data.getIntExtra(Const.POSITION, 0);
                addressAdapter.put(pos, address);
            }
        }
    }


    @Override
    public void onItemClick(View view, final int pos) {
        switch (view.getId()) {
            case R.id.rl_address_item:

                defaultAddressDialog(pos);

                break;


            case R.id.iv_edit:
                Intent intent = new Intent(this, ActivityAddAddress.class);
                intent.putExtra(Const.EXTRA_OBJ, addressAdapter.getItem(pos));
                intent.putExtra(Const.POSITION, pos);
                startActivityForResult(intent, Const.UPDATE_ADDRESS);
                break;

            case R.id.iv_remove:
                deleteDialog(pos);
                break;
        }


    }
}
