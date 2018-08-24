package com.houz.chef.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.houz.chef.R;
import com.houz.chef.adapter.HomeAdapter;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ChefDetailsActivityBinding;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.GetChefDetails;
import com.houz.chef.modelBean.GetProductDetailsBean;
import com.houz.chef.modelBean.Product;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.modelBean.SetFavouriteProduct;
import com.houz.chef.modelBean.User;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.StaticField;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ActivityChefDetail extends BaseActivity implements View.OnClickListener,OnItemClickListener {

    ChefDetailsActivityBinding binding;
    private int pageProduct,chefId=0,userId = 0;
    private HomeAdapter homeAdapter;
    AboutMe chefBean = new AboutMe();
    ArrayList<ProductBean> productBeansArray = new ArrayList<>();
    HashMap<String, String> map;
    @Override
    public int getLayoutResId() {
        return R.layout.chef_details_activity;
    }

    @Override
    public void init() {
        binding = (ChefDetailsActivityBinding) getBindingObj();
        binding.imgSideBar.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent != null) {
            if (intent.hasExtra(StaticField.ARG_CHEFID))
                chefId = intent.getIntExtra(StaticField.ARG_CHEFID,0);
        }

        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        if (homeAdapter == null) {
            homeAdapter = new HomeAdapter(context, binding.recyclerView,this);
            homeAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
//                    homeAdapter.addProgress();
//                    callgetAllMenu();
                }
            });
        }
        userId = preferences.getUserDataPref().getId();
        binding.recyclerView.setAdapter(homeAdapter);
        pageProduct = 0;
        homeAdapter.clearAll();
        callgetChef(chefId,userId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_side_bar:
                finish();
                break;

            case R.id.img_favourite_bar:
                callsetFavouriteChef();
                break;

            case R.id.img_call:
                if (chefBean.getPhone() != null && !chefBean.getPhone().equalsIgnoreCase(""))
                {
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
                                            callIntent.setData(Uri.parse("tel:"+chefBean.getPhone()));
                                            startActivity(callIntent);
                                        }
                                    } else if (permission.shouldShowRequestPermissionRationale) {
                                        CommonUtils.toast(context, getString(R.string.image_permission_txt));
                                    }
                                }
                            });

                }
                else
                    CommonUtils.toast(context,"No phone number added by Chef.");
                break;

            case R.id.img_mail:
                if (chefBean.getEmail() != null && !chefBean.getEmail().equalsIgnoreCase(""))
                {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + chefBean.getEmail()));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
                else
                    CommonUtils.toast(context,"No email ID added by Chef.");
                break;
        }
    }

    private void callgetChef(int chefId,int userId) {
        if (CommonUtils.isInternetOn(context)) {

            pageProduct++;
            binding.progress.setVisibility(View.VISIBLE);
            Observable<GetChefDetails> signupusers = FetchServiceBase.getFetcherService(context)
                    .getChefDetail(userId,chefId);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetChefDetails>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("chefDetail error ",""+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(GetChefDetails getChefDetails) {
                            binding.progress.setVisibility(View.GONE);
                            setDetails(getChefDetails);
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void callsetFavouriteChef() {
        if (CommonUtils.isInternetOn(context)) {

            map = new HashMap<>();
            map.put("user_id", ""+preferences.getUserDataPref().getId());
            map.put("chef_id", ""+chefBean.getId());

            binding.progress.setVisibility(View.VISIBLE);
            Observable<SetFavouriteProduct> signupusers = FetchServiceBase.getFetcherService(context)
                    .setFavouriteChef(CommonUtils.converRequestBodyFromMap(map));
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
                            Log.e("Favourite Chef error ",""+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(SetFavouriteProduct setFavouriteProduct) {
                            binding.progress.setVisibility(View.GONE);
                            if (setFavouriteProduct.getStatus()) {
                                CommonUtils.toast(context,setFavouriteProduct.getMessage());
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    private void setDetails(GetChefDetails getChefDetails){
        try{
            if (getChefDetails.getStatus())
            {
                chefBean = getChefDetails.getData().getUser();
                productBeansArray.addAll(getChefDetails.getData().getProducts());

                for (int i=0;i<productBeansArray.size();i++)
                {
                    productBeansArray.get(i).setUser(chefBean);
                }

                if (chefBean.getProfile() != null && chefBean.getProfile() != null
                        && !TextUtils.isEmpty(chefBean.getProfile())) {
                    Picasso.with(context)
                            .load(chefBean.getProfile())
                            .into(binding.imgChef);
                }
                if(chefBean.getName() != null && !TextUtils.isEmpty(chefBean.getName()))
                    binding.txtChefName.setText(chefBean.getName());
                if(chefBean.getName() != null && !TextUtils.isEmpty(chefBean.getName()))
                    binding.txtChefName.setText(chefBean.getName());

                if (productBeansArray.size() > 0) {
                    if (homeAdapter.getItemCount() <= 0)
                        homeAdapter.addAllList(productBeansArray);
                    else {
                        homeAdapter.removeProgress();
                        homeAdapter.addAllList(productBeansArray);
                    }
                    homeAdapter.setLoaded();
                } else {
                    homeAdapter.removeProgress();
                    homeAdapter.setLoaded();
                }
                binding.imgFavouriteBar.setOnClickListener(this);
            }
            else
            {
                Log.e("ChefDetails error ","Response False");
            }

        }catch (Exception e)
        {
            Log.e("ChefDetails Exception "," "+e);
        }
    }

    @Override
    public void onItemClick(View view, int pos) {

    }
}
