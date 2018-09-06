package com.houz.chef.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.houz.chef.Manifest;
import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.AddAddressActivityBinding;
import com.houz.chef.modelBean.AddAddressBean;
import com.houz.chef.modelBean.AddAddressResponse;
import com.houz.chef.modelBean.Address;
import com.houz.chef.modelBean.GetAddressList;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Const;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Permissions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ActivityAddAddress extends BaseActivity implements View.OnClickListener {

    AddAddressActivityBinding binding;
    private int addressCount;
    private boolean isUpdate;
    private AddAddressBean addAddressBean;
    private int position;
    private FusedLocationProviderClient mFusedLocationClient;
    private RxPermissions rxPermissions;
    private Location location;

    @Override
    public int getLayoutResId() {
        return R.layout.add_address_activity;
    }

    @Override
    public void init() {
        binding = (AddAddressActivityBinding) getBindingObj();
        rxPermissions = new RxPermissions(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (getIntent().hasExtra(Const.ADDRESS_COUNT)) {
            addressCount = getIntent().getIntExtra(Const.ADDRESS_COUNT, 0);
        }

        if (getIntent().hasExtra(Const.POSITION)) {
            position = getIntent().getIntExtra(Const.POSITION, 0);
        }

        if (getIntent().hasExtra(Const.EXTRA_OBJ)) {
            addAddressBean = getIntent().getParcelableExtra(Const.EXTRA_OBJ);
        }

        setListener();

        if (addAddressBean != null) {
            setData();
        } else {
            binding.txtTitle.setText("Add Address");
        }

    }

    private void setListener() {
        binding.imgSideBar.setOnClickListener(this);
        binding.btnAddAddress.setOnClickListener(this);
        binding.btnLocation.setOnClickListener(this);
    }

    private void checkLocationPermission() {
        if (CommonUtils.hasPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION})) {
            getLocation();

        } else {
            RxPermissions rxPermissions1 = new RxPermissions((Activity) context);
            rxPermissions1
                    .requestEach(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new Action1<Permission>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void call(Permission permission) {
                            if (permission.granted) {

                                getLocation();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                CommonUtils.toast(context, getString(R.string.location_permission_txt));
                            }
                        }
                    });

        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        binding.llProgress.setVisibility(View.VISIBLE);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                ActivityAddAddress.this.location = location;

                Geocoder gc = new Geocoder(context);
                if (gc.isPresent()) {
                    List<android.location.Address> list = null;
                    try {
                        if (location == null)
                            return;
                        list = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        android.location.Address address = list.get(0);
                        binding.edtAddress1.setText(address.getPremises());
                        binding.edtAddress2.setText(address.getSubLocality());
                        binding.edtCity.setText(address.getSubAdminArea());
                        binding.edtState.setText(address.getAdminArea());
                        binding.edtCountry.setText(address.getCountryName());
                        binding.edtZip.setText(address.getPostalCode());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                binding.llProgress.setVisibility(View.GONE);
            }

        });
    }


    private void setData() {
        Address address = addAddressBean.getAddressModel();
        binding.edtTitle.setText(address.getTitle());
        binding.edtAddress1.setText(address.getAddress1());
        binding.edtAddress2.setText(address.getAddress2());
        binding.edtCity.setText(address.getCity());
        binding.edtState.setText(address.getState());
        binding.edtZip.setText(address.getPincode());
        binding.edtCountry.setText(address.getCountry());
        binding.edtNumber.setText(address.getPhone());
        binding.btnAddAddress.setText("Update");
        binding.txtTitle.setText("Update Address");

    }

    private boolean checkValidation() {
        String title, address1, address2, city, state, zipcode, country, contactNo;

        title = binding.edtTitle.getText().toString().trim();
        address1 = binding.edtAddress1.getText().toString().trim();
        address2 = binding.edtAddress2.getText().toString().trim();
        city = binding.edtCity.getText().toString().trim();
        state = binding.edtState.getText().toString().trim();
        zipcode = binding.edtZip.getText().toString().trim();
        country = binding.edtCountry.getText().toString().trim();
        contactNo = binding.edtNumber.getText().toString().trim();
        if (title.isEmpty()) {
            CommonUtils.toast(context, "Please enter title");
            binding.edtTitle.setError("Empty");
            return false;
        } else if (address1.isEmpty()) {
            CommonUtils.toast(context, "Please enter address1");
            binding.edtAddress1.setError("Empty");
            return false;
        } else if (city.isEmpty()) {
            CommonUtils.toast(context, "Please enter city");
            binding.edtCity.setError("Empty");
            return false;
        } else if (state.isEmpty()) {
            CommonUtils.toast(context, "Please enter state");
            binding.edtState.setError("Empty");
            return false;
        } else if (country.isEmpty()) {
            CommonUtils.toast(context, "Please enter country");
            binding.edtCountry.setError("Empty");
            return false;
        } else if (zipcode.isEmpty()) {
            CommonUtils.toast(context, "Please enter zip code");
            binding.edtZip.setError("Empty");
            return false;
        } else if (contactNo.isEmpty()) {
            CommonUtils.toast(context, "Please enter phone number");
            binding.edtNumber.setError("Empty");
            return false;
        } else if (contactNo.length() < 10) {
            CommonUtils.toast(context, "Please enter valid phone number");
            binding.edtNumber.setError("Invalid");
            return false;
        }

        return true;
    }

    private void callUpdateAddressAPI() {
        String title, address1, address2, city, state, zipcode, country, contactNo;

        title = binding.edtTitle.getText().toString().trim();
        address1 = binding.edtAddress1.getText().toString().trim();
        address2 = binding.edtAddress2.getText().toString().trim();
        city = binding.edtCity.getText().toString().trim();
        state = binding.edtState.getText().toString().trim();
        zipcode = binding.edtZip.getText().toString().trim();
        country = binding.edtCountry.getText().toString().trim();
        contactNo = binding.edtNumber.getText().toString().trim();

        JSONObject params = new JSONObject();
        try {
            JSONObject jsonAddress = new JSONObject();
            jsonAddress.put("title", title);
            jsonAddress.put("state", state);
            jsonAddress.put("city", city);
            jsonAddress.put("address1", address1);
            jsonAddress.put("address2", address2);
            jsonAddress.put("country", country);
            jsonAddress.put("number", contactNo);
            jsonAddress.put("zipcode", zipcode);
            params.put("address", jsonAddress);
            params.put("is_default", addAddressBean.getIsDefault());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("address", params.toString());
        map.put("is_default", "" + addAddressBean.getIsDefault());
        Map<String, RequestBody> requestBodyHashMap = CommonUtils.converRequestBodyFromMap(map);
        if (CommonUtils.isInternetOn(context)) {

            binding.llProgress.setVisibility(View.VISIBLE);
            final Observable<AddAddressResponse> addAddress;
            addAddress = FetchServiceBase.getFetcherService(context)
                    .updateAddress(preferences.getUserDataPref().getId(), addAddressBean.getId(), requestBodyHashMap);

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
                            binding.llProgress.setVisibility(View.GONE);
                            if (addAddressResponse.isStatus()) {
                                AddAddressBean addAddressBean = addAddressResponse.getAddAddressBean();
                                Intent intent = new Intent();
                                intent.putExtra(Const.POSITION, position);
                                intent.putExtra(Const.EXTRA_OBJ, addAddressBean);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                        }
                    });
        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void callAddAddressAPI() {
        String title, address1, address2, city, state, zipcode, country, contactNo;

        title = binding.edtTitle.getText().toString().trim();
        address1 = binding.edtAddress1.getText().toString().trim();
        address2 = binding.edtAddress2.getText().toString().trim();
        city = binding.edtCity.getText().toString().trim();
        state = binding.edtState.getText().toString().trim();
        zipcode = binding.edtZip.getText().toString().trim();
        country = binding.edtCountry.getText().toString().trim();
        contactNo = binding.edtNumber.getText().toString().trim();

        JSONObject params = new JSONObject();
        try {
            JSONObject jsonAddress = new JSONObject();
            jsonAddress.put("title", title);
            jsonAddress.put("state", state);
            jsonAddress.put("city", city);
            jsonAddress.put("address1", address1);
            jsonAddress.put("address2", address2);
            jsonAddress.put("country", country);
            jsonAddress.put("number", contactNo);
            jsonAddress.put("zipcode", zipcode);
            params.put("address", jsonAddress);
            if (addressCount == 0)
                params.put("is_default", "1");
            else
                params.put("is_default", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("address", params.toString());
        if (addressCount == 0)
            map.put("is_default", "1");
        else
            map.put("is_default", "0");
        Map<String, RequestBody> requestBodyHashMap = CommonUtils.converRequestBodyFromMap(map);
        if (CommonUtils.isInternetOn(context)) {

            binding.llProgress.setVisibility(View.VISIBLE);
            final Observable<AddAddressResponse> addAddress;
            addAddress = FetchServiceBase.getFetcherService(context)
                    .addAddress(preferences.getUserDataPref().getId(), requestBodyHashMap);

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
                            binding.llProgress.setVisibility(View.GONE);
                            if (addAddressResponse.isStatus()) {

                                AddAddressBean addAddressBean = addAddressResponse.getAddAddressBean();

                                Intent intent = new Intent();
                                intent.putExtra(Const.EXTRA_OBJ, addAddressBean);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
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
            case R.id.btn_add_address:
                CommonUtils.hideKeyboard(v);
                if (addAddressBean == null) {

                    if (checkValidation()) {
                        callAddAddressAPI();
                    }
                } else {
                    if (checkValidation()) {
                        callUpdateAddressAPI();
                    }
                }
                break;

            case R.id.img_side_bar:
                finish();
                break;

            case R.id.btn_location:

                if (CommonUtils.canGetLocation(this)) {
                    checkLocationPermission();
                } else {
                    CommonUtils.showSettingsAlert(this);
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.GPS_ENABLE) {
            if (CommonUtils.canGetLocation(this)) {
                checkLocationPermission();
            } else {
                CommonUtils.toast(this, "GPS is not enabled");
            }
        }
    }
}
