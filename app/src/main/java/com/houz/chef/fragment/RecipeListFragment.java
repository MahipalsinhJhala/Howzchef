package com.houz.chef.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.houz.chef.R;
import com.houz.chef.adapter.HomeAdapter;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.RecipeListFragmentBinding;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.GetProductBean;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.utils.CommonUtils;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class RecipeListFragment extends BaseFragment implements OnItemClickListener {

    RecipeListFragmentBinding binding;
    private int pageProduct;
    private HomeAdapter homeAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.recipe_list_fragment;
    }

    @Override
    public void init() {
        binding = (RecipeListFragmentBinding) getBindingObj();

        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        if (homeAdapter == null) {
            homeAdapter = new HomeAdapter(context, binding.recyclerView, this);
            homeAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
//                    homeAdapter.addProgress();
//                    callgetAllMenu();
                }
            });
        }
        binding.recyclerView.setAdapter(homeAdapter);
        pageProduct = 0;
        homeAdapter.clearAll();
        callgetFavouriteProduct();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            try {
                pageProduct = 0;
                if (homeAdapter != null && homeAdapter.getItemCount() > 0)
                    return;
                callgetFavouriteProduct();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            if(subscription != null){
                subscription.unsubscribe();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void callgetFavouriteProduct() {
        if (CommonUtils.isInternetOn(context)) {

            pageProduct++;
            binding.progress.setVisibility(View.VISIBLE);
            Observable<GetProductBean<ArrayList<ProductBean>>> signupusers = FetchServiceBase.getFetcherService(context)
                    .getFavouriteProduct(preferences.getUserDataPref().getId());
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetProductBean<ArrayList<ProductBean>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("RecipeListFrag error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(GetProductBean<ArrayList<ProductBean>> getProductBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (getProductBean.getResult() != null && getProductBean.getResult().size() > 0) {
                                if (homeAdapter.getItemCount() <= 0)
                                    homeAdapter.addAllList(getProductBean.getResult());
                                else {
                                    homeAdapter.removeProgress();
                                    homeAdapter.addAllList(getProductBean.getResult());
                                }
                                homeAdapter.setLoaded();
                            } else {
                                homeAdapter.removeProgress();
                                homeAdapter.setLoaded();
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }

    @Override
    public void onItemClick(View view, int pos) {

    }
}
