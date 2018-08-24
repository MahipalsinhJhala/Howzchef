package com.houz.chef.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.houz.chef.R;
import com.houz.chef.adapter.ChefListAdapter;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ChefListFragmentBinding;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.GetFavouriteChefList;
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
public class ChefListFragment extends BaseFragment {

    ChefListFragmentBinding binding;
    ChefListAdapter chefListAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.chef_list_fragment;
    }

    @Override
    public void init() {
        binding = (ChefListFragmentBinding) getBindingObj();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        if (chefListAdapter == null) {
            chefListAdapter = new ChefListAdapter(context, binding.recyclerView);
            chefListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
//                        chefListAdapter.addProgress();
//                        callNewJobListApi();
                }
            });
        }
        binding.recyclerView.setAdapter(chefListAdapter);
        chefListAdapter.clearAll();

        callgetFavouriteChef();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            try {
                if (chefListAdapter != null && chefListAdapter.getItemCount()>0)
                    return;
                callgetFavouriteChef();
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

    private void callgetFavouriteChef() {
        if (CommonUtils.isInternetOn(context)) {

//            pageProduct++;
            binding.progress.setVisibility(View.VISIBLE);
            Observable<GetFavouriteChefList> signupusers = FetchServiceBase.getFetcherService(context)
                    .getFavouriteChef(preferences.getUserDataPref().getId());
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetFavouriteChefList>() {
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
                        public void onNext(GetFavouriteChefList getFavouriteChefList) {
                            binding.progress.setVisibility(View.GONE);
                            if (getFavouriteChefList.getData() != null && getFavouriteChefList.getData().size() > 0) {
                                if (chefListAdapter.getItemCount() <= 0)
                                    chefListAdapter.addAllList(getFavouriteChefList.getChefs());
                                else {
                                    chefListAdapter.removeProgress();
                                    chefListAdapter.addAllList(getFavouriteChefList.getChefs());
                                }
                                chefListAdapter.setLoaded();
                            } else {
                                chefListAdapter.removeProgress();
                                chefListAdapter.setLoaded();
                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, getString(R.string.snack_bar_no_internet));
        }
    }
}
