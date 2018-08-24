package com.houz.chef.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ProductDetailsActivityBinding;
import com.houz.chef.modelBean.GetProductDetailsBean;
import com.houz.chef.modelBean.ModelBean;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.modelBean.SetFavouriteProduct;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.StaticField;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ActivityProductDetail extends BaseActivity implements View.OnClickListener {

    ProductDetailsActivityBinding binding;
    ProductBean productBean;
    private int productId = 0, userId = 0;
    HashMap<String, String> map;

    @Override
    public int getLayoutResId() {
        return R.layout.product_details_activity;
    }

    @Override
    public void init() {
        binding = (ProductDetailsActivityBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(this);
        binding.imgCartBar.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(StaticField.ARG_PRODUCTID))
                productId = intent.getIntExtra(StaticField.ARG_PRODUCTID, 0);
        }
        userId = preferences.getUserDataPref().getId();
        callgetProduct(productId, userId);



        /*binding.rbItem.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                CommonUtils.toast(ratingBar.getContext(),""+rating);
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_side_bar:
                finish();
                break;

            case R.id.btn_place_order:
                Intent intent3 = new Intent(context, ActivityCart.class);
                startActivity(intent3);
                break;

            case R.id.img_favourite_bar:
                callsetFavouriteProduct();
                break;

            case R.id.img_plus:
                callsetAddToCart();
                break;


            case R.id.img_chef:
                Intent intent2 = new Intent(context, ActivityChefDetail.class);
                intent2.putExtra(StaticField.ARG_CHEFID, productBean.getUser().getId());
                startActivity(intent2);
                break;

            case R.id.ll_name_detail:
                Intent intent1 = new Intent(context, ActivityChefDetail.class);
                intent1.putExtra(StaticField.ARG_CHEFID, productBean.getUser().getId());
                startActivity(intent1);
                break;


            case R.id.img_cart_bar:
                Intent intent = new Intent(context, ActivityCart.class);
                startActivity(intent);
                break;

            case R.id.img_call:
                if (productBean != null && productBean.getUser() != null && productBean.getUser().getPhone() != null && !productBean.getUser().getPhone().equalsIgnoreCase("")) {
                    RxPermissions rxPermissions1 = new RxPermissions((Activity) context);
                    rxPermissions1
                            .requestEach(Manifest.permission.CALL_PHONE)
                            .subscribe(new Action1<Permission>() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void call(Permission permission) {
                                    if (permission.granted) {
                                        if (permission.name.equals("android.permission.CALL_PHONE"))
                                            return;
                                        if (CommonUtils.hasPermissions(context, Manifest.permission.CALL_PHONE)) {
                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            callIntent.setData(Uri.parse("tel:" + productBean.getUser().getPhone()));
                                            startActivity(callIntent);
                                        }
                                    } else if (permission.shouldShowRequestPermissionRationale) {
                                        CommonUtils.toast(context, getString(R.string.image_permission_txt));
                                    }
                                }
                            });

                } else
                    CommonUtils.toast(context, "No phone number added by Chef.");
                break;

            case R.id.img_mail:
                if (productBean != null && productBean.getUser() != null && productBean.getUser().getEmail() != null && !productBean.getUser().getEmail().equalsIgnoreCase("")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + productBean.getUser().getEmail()));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } else
                    CommonUtils.toast(context, "No email ID added by Chef.");
                break;
        }
    }

    private void callgetProduct(final int productId, final int userId) {
        if (CommonUtils.isInternetOn(context)) {

            binding.progress.setVisibility(View.VISIBLE);
            Observable<GetProductDetailsBean> signupusers = FetchServiceBase.getFetcherService(context)
                    .getProductDetail(userId, productId);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetProductDetailsBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("chefDetail error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(GetProductDetailsBean getProductDetailsBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (getProductDetailsBean.getStatus()) {
                                productBean = getProductDetailsBean.getData();
                                setDetails();
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void callsetFavouriteProduct() {
        if (CommonUtils.isInternetOn(context)) {

            map = new HashMap<>();
            map.put("user_id", "" + preferences.getUserDataPref().getId());
            map.put("product_id", "" + productBean.getId());

            binding.progress.setVisibility(View.VISIBLE);
            Observable<SetFavouriteProduct> signupusers = FetchServiceBase.getFetcherService(context)
                    .setFavouriteProduct(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SetFavouriteProduct>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Favourite error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(SetFavouriteProduct setFavouriteProduct) {
                            binding.progress.setVisibility(View.GONE);
                            if (setFavouriteProduct.getStatus()) {

                                boolean flag=!productBean.isIs_favourite();
                                productBean.setIs_favourite(flag);
                                if(productBean.isIs_favourite()) {
                                    binding.imgFavouriteBar.setImageResource(R.drawable.favourite_icon_bg);
                                }else{
                                    binding.imgFavouriteBar.setImageResource(R.drawable.favorite_star);
                                }
                                CommonUtils.toast(context, setFavouriteProduct.getMessage());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void callsetAddToCart() {
        if (CommonUtils.isInternetOn(context)) {

            map = new HashMap<>();
            map.put("user_id", "" + preferences.getUserDataPref().getId());
            map.put("product_id", "" + productBean.getId());
            map.put("qty", "1");

            binding.progress.setVisibility(View.VISIBLE);
            Observable<ModelBean> signupusers = FetchServiceBase.getFetcherService(context)
                    .addToCart(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Favourite error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean modelBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (modelBean.getStatus()) {
                                CommonUtils.toast(context, modelBean.getMessage());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void setDetails() {
        if (productBean.getImage() != null && productBean.getImage() != null
                && !TextUtils.isEmpty(productBean.getImage())) {
            Picasso.with(context)
                    .load(productBean.getImage())
                    .into(binding.imgItem);
        }
        if (productBean.getIs_veg() != null && !TextUtils.isEmpty(productBean.getIs_veg())) {
            if (productBean.getIs_veg().equalsIgnoreCase("1"))
                binding.txtItemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.veg_icon, 0);
            else
                binding.txtItemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.nonveg_icon, 0);
        }

        if(productBean.isIs_favourite()){
            binding.imgFavouriteBar.setImageResource(R.drawable.favourite_icon_bg);
        }else{
            binding.imgFavouriteBar.setImageResource(R.drawable.favorite_star);
        }

        if (productBean.getName() != null && !TextUtils.isEmpty(productBean.getName()))
            binding.txtItemName.setText(productBean.getName());
        if (productBean.getMaking_time() != null && !TextUtils.isEmpty(productBean.getMaking_time()))
            binding.txtTime.setText(productBean.getMaking_time() + " Min");
        if (productBean.getPrice() != null && !TextUtils.isEmpty(productBean.getPrice()))
            binding.txtPrice.setText("$" + productBean.getPrice());
        if (productBean.getUser().getName() != null && !TextUtils.isEmpty(productBean.getUser().getName()))
            binding.txtChefName.setText(productBean.getUser().getName());
        if (productBean.getRating() != null && !TextUtils.isEmpty(productBean.getRating()))
            binding.rbItem.setRating(Float.parseFloat(productBean.getRating()));
        if (productBean.getDescription() != null && !TextUtils.isEmpty(productBean.getDescription()))
            binding.txtDescription.setText(productBean.getDescription());
        if (productBean.getDescription() != null && !TextUtils.isEmpty(productBean.getDescription()))
            binding.txtDescription.setText(productBean.getDescription());
        if (productBean.getUser().getProfile() != null && productBean.getUser().getProfile() != null
                && !TextUtils.isEmpty(productBean.getUser().getProfile())) {
            Picasso.with(context)
                    .load(productBean.getUser().getProfile())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder)
                    .into(binding.imgChef);
        }
        if (productBean.getUser().getName() != null && !TextUtils.isEmpty(productBean.getUser().getName()))
            binding.txtChefName.setText(productBean.getUser().getName());
        if (productBean.getIngredients() != null && !TextUtils.isEmpty(productBean.getIngredients()))
            binding.txtIngredients.setText(productBean.getIngredients());
        if (productBean.getDelivery_options() != null && !TextUtils.isEmpty(productBean.getDelivery_options()))
            binding.txtDeliveryOption.setText(productBean.getDelivery_options());
        if (productBean.getSpecifications() != null && !TextUtils.isEmpty(productBean.getSpecifications()))
            binding.txtSpecification.setText(productBean.getSpecifications());
        if (productBean.getCarbs() != null && !TextUtils.isEmpty(productBean.getCarbs()))
            binding.txtCarbsDetail.setText(productBean.getCarbs());
        if (productBean.getCalorie() != null && !TextUtils.isEmpty(productBean.getCalorie()))
            binding.txtCalorieDetail.setText(productBean.getCalorie());
        if (productBean.getProtein() != null && !TextUtils.isEmpty(productBean.getProtein()))
            binding.txtProteinDetail.setText(productBean.getProtein());
        if (productBean.getFiber() != null && !TextUtils.isEmpty(productBean.getFiber()))
            binding.txtFiberDetail.setText(productBean.getFiber());
        if (productBean.getFat() != null && !TextUtils.isEmpty(productBean.getFat()))
            binding.txtFatDetail.setText(productBean.getFat());
        if (productBean.getDiscount() != null && !TextUtils.isEmpty(productBean.getDiscount())) {
            double dDiscount, dPrice, dDiscountPrice;
            dDiscount = Double.parseDouble(productBean.getDiscount());
            dPrice = Double.parseDouble(productBean.getPrice());
            if (dDiscount > 0) {
                binding.txtDiscount.setText(productBean.getDiscount() + "% off");

                dDiscountPrice = (dPrice / 100.0f) * dDiscount;

                binding.txtDiscountPrice.setText("" + dDiscountPrice);

            } else
                binding.llDiscount.setVisibility(View.GONE);
        }



        binding.imgChef.setOnClickListener(this);
        binding.llNameDetail.setOnClickListener(this);
        binding.imgCall.setOnClickListener(this);
        binding.imgMail.setOnClickListener(this);
        binding.imgFavouriteBar.setOnClickListener(this);
        binding.imgPlus.setOnClickListener(this);
        binding.btnPlaceOrder.setOnClickListener(this);
    }
}
