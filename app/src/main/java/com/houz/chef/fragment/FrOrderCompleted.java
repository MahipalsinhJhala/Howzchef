package com.houz.chef.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import com.houz.chef.R;
import com.houz.chef.activity.DrawerActivity;
import com.houz.chef.adapter.ExpandableListAdapter;
import com.houz.chef.adapter.OrderPagerAdapter;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.FrOrderBinding;
import com.houz.chef.modelBean.GetFavouriteChefList;
import com.houz.chef.modelBean.Order;
import com.houz.chef.modelBean.OrderData;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.widgets.LoadMoreExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m.j on 7/8/2018.
 */

public class FrOrderCompleted extends BaseFragment implements LoadMoreExpandableListView.OnLoadMoreListener{

    FrOrderBinding binding;
    private ExpandableListAdapter adapter;
    private int currentPage, lastPage;
    private List<Order> orderDataList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fr_order;
    }

    @Override
    public void init() {
        binding = (FrOrderBinding) getBindingObj();

        binding.orderElv.removeHeaderView();
        getOrderAPI(true);
        setListener();
    }

    private void setListener() {
        setRefreshListener();
        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                adapter = null;
                getOrderAPI(false);
            }
        });

        binding.orderElv.setOnLoadMoreListener(this);
    }

    private void setRefreshListener() {
        binding.orderElv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                boolean enable = false;
                if (binding.orderElv!= null && binding.orderElv.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = binding.orderElv
                            .getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = binding.orderElv.getChildAt(0)
                            .getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                } else if (binding.orderElv == null) {
                    enable = true;
                }
                binding.swiperefresh.setEnabled(enable);
            }
        });
    }


    private void setData() {

        if (adapter == null) {
            adapter = new ExpandableListAdapter(getContext(), orderDataList);
            binding.orderElv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    private void getOrderAPI(final boolean isLoadMore) {
        if (CommonUtils.isInternetOn(getContext())) {
            currentPage++;
            HashMap<String, String> params = new HashMap<>();
            params.put("page", "" + currentPage);

            if (!isLoadMore)
                binding.llProgress.setVisibility(View.VISIBLE);

            Observable<OrderData> signupusers = FetchServiceBase.getFetcherService(context)
                    .getCompletedOrders(preferences.getUserDataPref().getId(), CommonUtils.converRequestBodyFromMap(params));
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<OrderData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("RecipeListFrag error ", "" + e);
                            if (isLoadMore)
                                binding.orderElv.onLoadMoreComplete();
                            else
                                binding.swiperefresh.setRefreshing(false);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(OrderData orderData) {
                            binding.progress.setVisibility(View.GONE);
                            if (orderData.getStatus()) {
                                lastPage = orderData.getLastPage();

                                if (isLoadMore) {
                                    binding.orderElv.onLoadMoreComplete();
                                }else {
                                    binding.swiperefresh.setRefreshing(false);
                                    setListener();
                                }
                                if (orderData.getOrders() != null && orderData.getOrders().size() > 0) {
                                    if(!isLoadMore){
                                        orderDataList.clear();
                                    }
                                    orderDataList.addAll(orderData.getOrders());
                                }
                                setData();
                            }
                        }
                    });
        } else {
            CommonUtils.toast(getContext(), getString(R.string.snack_bar_no_internet));
        }
    }

    @Override
    public void onLoadMore() {
        if (currentPage < lastPage) {
            getOrderAPI(true);
            binding.orderElv.onLoadMore();
        }else{
            binding.orderElv.onLoadMoreComplete();
        }
    }
}
