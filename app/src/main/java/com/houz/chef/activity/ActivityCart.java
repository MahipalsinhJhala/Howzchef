package com.houz.chef.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.houz.chef.R;
import com.houz.chef.adapter.CartAdapter;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.CartActivityBinding;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.CheckPromoBean;
import com.houz.chef.modelBean.Datum;
import com.houz.chef.modelBean.GetCart;
import com.houz.chef.modelBean.ModelBean;
import com.houz.chef.modelBean.PromoDataBean;
import com.houz.chef.modelBean.QuantityUpdateResponse;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Const;
import com.houz.chef.utils.StaticField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ActivityCart extends BaseActivity implements View.OnClickListener, OnItemClickListener {

    CartActivityBinding binding;
    private int pageProduct, chefId = 0;
    private CartAdapter cartAdapter;
    ArrayList<Datum> productBeansArray = new ArrayList<>();
    HashMap<String, String> map;
    private PromoDataBean promoCode = null;
    private boolean isCartItemChange; // true if cart quantity change or item remove, send it to Homefragment to update home screen count.
    private JSONArray jsonRemovedItem = new JSONArray();

    int subTotal = 0, deliveryCharge = 0, grandTotal = 0;

    @Override
    public int getLayoutResId() {
        return R.layout.cart_activity;
    }

    @Override
    public void init() {
        binding = (CartActivityBinding) getBindingObj();
        binding.imgSideBar.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(StaticField.ARG_CHEFID))
                chefId = intent.getIntExtra(StaticField.ARG_CHEFID, 0);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(context, binding.recyclerView);
            cartAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
//                    homeAdapter.addProgress();
//                    callgetAllMenu();
                }
            });
        }
        binding.recyclerView.setAdapter(cartAdapter);
        pageProduct = 0;
        cartAdapter.clearAll();
        callgetCart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_side_bar:
                finishActivity();
                break;


        }
    }

    private void finishActivity() {

        if (isCartItemChange) {
            try {
                JSONArray jsonArray = new JSONArray();

                for (Datum datum : productBeansArray) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", datum.getProduct().getId() + "");
                    jsonObject.put("qty", datum.getQty());
                    jsonArray.put(jsonObject);
                }

                Intent intent = new Intent();
                intent.putExtra(Const.IS_UPDATE, isCartItemChange);
                intent.putExtra(Const.EXTRA_OBJ, jsonArray.toString());
                intent.putExtra(Const.REMOVED_ITEM_IDS, jsonRemovedItem.toString());
                setResult(Activity.RESULT_OK, intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        finish();

    }

    private void callgetCart() {
        if (CommonUtils.isInternetOn(context)) {

            map = new HashMap<>();
            map.put("user_id", "" + preferences.getUserDataPref().getId());

            pageProduct++;
            binding.progress.setVisibility(View.VISIBLE);
            Observable<GetCart> signupusers = FetchServiceBase.getFetcherService(context)
                    .getCart(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetCart>() {
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
                        public void onNext(GetCart getCart) {
                            binding.progress.setVisibility(View.GONE);
                            if (getCart.getStatus()) {
                                productBeansArray.addAll(getCart.getData());
                                if (cartAdapter.getItemCount() <= 0) {
                                    getCart.getData().add(null);
                                    cartAdapter.addAllList(getCart.getData());
                                } else {
                                    cartAdapter.removeProgress();
                                    cartAdapter.addAllList(getCart.getData());
                                }
                                cartAdapter.setLoaded();

                            } else {
                                cartAdapter.removeProgress();
                                cartAdapter.setLoaded();
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void callApplyPromoCodeAPI(String code) {
        if (CommonUtils.isInternetOn(context)) {

            map = new HashMap<>();
            map.put("promo", "" + code);


            binding.progress.setVisibility(View.VISIBLE);
            Observable<CheckPromoBean> signupusers = FetchServiceBase.getFetcherService(context)
                    .checkPromo(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CheckPromoBean>() {
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
                        public void onNext(CheckPromoBean checkPromoBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (checkPromoBean.isStatus()) {
                                //preferences.savePromoCode(checkPromoBean.getData());

                                if (checkPromoBean.getData().getMinimum_cart_total() <= subTotal) {
                                    promoCode = checkPromoBean.getData();
                                    cartAdapter.setPromoCode(promoCode);
                                    cartAdapter.putItem(cartAdapter.getItemCount() - 1, null);
                                } else {
                                    CommonUtils.toast(ActivityCart.this, "This promo code is valid for minimum " + checkPromoBean.getData().getMinimum_cart_total() + " amount order");
                                }
                            } else {
                                CommonUtils.toast(ActivityCart.this, checkPromoBean.getMessage());
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
                .setMessage("Would you like to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        removeItem(pos);
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

    private void removeItem(final int pos) {
        if (CommonUtils.isInternetOn(context)) {

            Datum item = productBeansArray.get(pos);


            binding.progress.setVisibility(View.VISIBLE);
            Observable<ModelBean> signupusers = FetchServiceBase.getFetcherService(context)
                    .removeCartItem(preferences.getUserDataPref().getId(),item.getId());
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
                            Log.e("Cart error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean modelBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (modelBean.getStatus()) {
                                jsonRemovedItem.put(cartAdapter.getItem(pos).getProduct().getId());
                                cartAdapter.remove(pos);
                                isCartItemChange = true;
                                if (cartAdapter.getItemCount() == 0) {
                                    finishActivity();
                                }
                            }else {
                                CommonUtils.toast(ActivityCart.this,modelBean.getMessage());
                            }
                        }
                    });
        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }


    private void updateQuantity(final int quantity, final int pos) {
        if (CommonUtils.isInternetOn(context)) {
            map = new HashMap<>();
            map.put("user_id", "" + preferences.getUserDataPref().getId());
            map.put("product_id", "" + cartAdapter.getProductList().get(pos).getProductId());
            map.put("qty", "" + quantity);

            binding.progress.setVisibility(View.VISIBLE);
            Observable<QuantityUpdateResponse> signupusers = FetchServiceBase.getFetcherService(context)
                    .updateQuantity(CommonUtils.converRequestBodyFromMap(map));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QuantityUpdateResponse>() {
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
                        public void onNext(QuantityUpdateResponse quantityUpdateResponse) {
                            binding.progress.setVisibility(View.GONE);
                            if (quantityUpdateResponse.getStatus()) {
                                Datum datum = cartAdapter.getItem(pos);
                                Datum updatedDatum = quantityUpdateResponse.getData();
                                datum.setQty(updatedDatum.getQty());
                                datum.setDiscount(updatedDatum.getDiscount());
                                datum.setCreatedAt(updatedDatum.getCreatedAt());
                                datum.setUpdatedAt(updatedDatum.getUpdatedAt());
                                cartAdapter.putItem(pos, datum);
                                isCartItemChange = true;

                            }
                        }
                    });
        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }


    @Override
    public void onItemClick(View view, int pos) {
        int quantity = 0;
        switch (view.getId()) {

            case R.id.iv_plus:
                if (productBeansArray.get(pos).getQty() < 100) {
                    quantity = productBeansArray.get(pos).getQty() + 1;
                    updateQuantity(quantity, pos);
                }
                break;
            case R.id.iv_minus:
                if (productBeansArray.get(pos).getQty() > 0) {
                    if (productBeansArray.get(pos).getQty() > 1) {
                        quantity = productBeansArray.get(pos).getQty() - 1;
                        updateQuantity(quantity, pos);
                    } else {
                        deleteDialog(pos);
                    }
                }/*else{
                    deleteDialog(pos);
                }*/
                break;

            case R.id.iv_remove:
                deleteDialog(pos);
                break;

            case R.id.btn_apply:

                //CommonUtils.toast(this,"Comming soon!");
                String promoCode = view.getTag(R.string.tag_promo_code).toString();
                callApplyPromoCodeAPI(promoCode);
                break;

            case R.id.btn_address:
                Intent intent = new Intent(this, ActivityMyAddress.class);
                if (cartAdapter.getPromoCode() != null)
                    intent.putExtra(Const.PROMO_CODE, cartAdapter.getPromoCode().getName());
                else
                    intent.putExtra(Const.PROMO_CODE, "");
                intent.putExtra(Const.PROMO_DISCOUNT, cartAdapter.getDiscount());
                intent.putExtra(Const.TOTAL, cartAdapter.getTotal());

                startActivity(intent);
                break;


            default:
                if (view.getParent() instanceof Spinner) {
                    quantity = Integer.parseInt(view.getTag(R.string.tag_quantity).toString());
                    updateQuantity(quantity, pos);
                }
                break;
        }

    }
}
