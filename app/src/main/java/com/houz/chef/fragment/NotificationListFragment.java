package com.houz.chef.fragment;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.houz.chef.R;
import com.houz.chef.activity.DrawerActivity;
import com.houz.chef.adapter.HomeAdapter;
import com.houz.chef.adapter.NotificationAdapter;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.NotificationListFragmentBinding;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.Datum;
import com.houz.chef.modelBean.GetProductBean;
import com.houz.chef.modelBean.NotificationModel;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Preferences;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class NotificationListFragment extends BaseFragment implements OnItemClickListener {

    NotificationListFragmentBinding binding;
    private int pageProduct = 0;
    private int lastPage = 0;
    private NotificationAdapter notificationAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.notification_list_fragment;
    }

    @Override
    public void init() {
        binding = (NotificationListFragmentBinding) getBindingObj();
        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) context).ToggleDrawer();
            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageProduct = 0;
                callGetAllNotification();
            }
        });

        if (notificationAdapter == null) {
            notificationAdapter = new NotificationAdapter(context, binding.recyclerView, this);
            notificationAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (pageProduct < lastPage) {
                        notificationAdapter.addProgress();
                        callGetAllNotification();
                    } else {
                        notificationAdapter.removeProgress();
                        notificationAdapter.setLoaded();
                    }
                }
            });
        }

        binding.recyclerView.setAdapter(notificationAdapter);
        pageProduct = 0;
        notificationAdapter.clearAll();
        callGetAllNotification();
    }


    private void callGetAllNotification() {
        if (CommonUtils.isInternetOn(context)) {

            pageProduct++;
            int id = new Preferences(getContext()).getUserDataPref().getId();
            if (!notificationAdapter.isLoading() && !binding.swiperefresh.isRefreshing() && binding.progress.getVisibility() != View.VISIBLE)
                binding.progress.setVisibility(View.VISIBLE);
            Observable<GetProductBean<ArrayList<NotificationModel>>> signupusers = FetchServiceBase.getFetcherService(context)
                    .getNotifications(id,pageProduct);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetProductBean<ArrayList<NotificationModel>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @SuppressLint("LongLogTag")
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("NotificationListFragment error ", "" + e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(GetProductBean<ArrayList<NotificationModel>> getProductBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (notificationAdapter.isLoading()) {
                                notificationAdapter.removeProgress();
                            } else {
                                if (binding.swiperefresh.isRefreshing()) {
                                    binding.swiperefresh.setRefreshing(false);
                                }
                            }
                            if (getProductBean.getResult() != null && getProductBean.getResult().size() > 0) {
                                lastPage = Integer.parseInt(getProductBean.getLast_page());
                                binding.swiperefresh.setVisibility(View.VISIBLE);
                                //binding.llNoData.setVisibility(View.GONE);
                                if (notificationAdapter.isLoading()) {
                                    notificationAdapter.setLoaded();
                                    notificationAdapter.addAllList(getProductBean.getResult());
                                } else {
                                    notificationAdapter.clearList();
                                    notificationAdapter.addAllList(getProductBean.getResult());
                                }


                                notificationAdapter.notifyDataSetChanged();

                            } else {
                                notificationAdapter.clearAll();
                                binding.swiperefresh.setVisibility(View.GONE);
                                //binding.llNoData.setVisibility(View.VISIBLE);
                                notificationAdapter.removeProgress();
                                notificationAdapter.setLoaded();
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
