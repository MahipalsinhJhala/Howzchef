package com.houz.chef.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.SetFavouriteProduct;
import com.houz.chef.modelBean.User;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Preferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChefListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<User> list;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;

    public ChefListAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        list = new ArrayList<>();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager != null) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chef, parent, false);
            return new Header(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Header) {
            ((Header) holder).bindData(position);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<User> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void addProgress() {
        list.add(null);
        notifyItemInserted(list.size() - 1);
    }

    public void removeProgress() {
        if (list.size() > 0 && list.get(list.size() - 1) == null) {
            list.remove(list.size() - 1);
            notifyItemRemoved(list.size());
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private void callUnfavourite(final int pos) {
        if (CommonUtils.isInternetOn(context))

        {
            Preferences preferences = new Preferences(context);
            Map map = new HashMap<>();
            map.put("user_id", "" + preferences.getUserDataPref().getId());
            map.put("chef_id", "" + list.get(pos).getId());
            map.put("removechef", "Y");

            Observable<SetFavouriteProduct> signupusers = FetchServiceBase.getFetcherService(context)
                    .setFavouriteChef(CommonUtils.converRequestBodyFromMap(map));
            Subscription subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SetFavouriteProduct>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("Favourite Chef error ", "" + e);
                        }

                        @Override
                        public void onNext(SetFavouriteProduct setFavouriteProduct) {
                            if(setFavouriteProduct.getStatus()){
                                list.remove(pos);
                                notifyDataSetChanged();

                            }
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    private class Header extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txt_location;
        private TextView txt_user_name;
        private ImageView img_user;
        private ImageView iv_cross;

        Header(View v) {
            super(v);
            txt_location = (TextView) v.findViewById(R.id.txt_location);
            txt_user_name = (TextView) v.findViewById(R.id.txt_user_name);
            img_user = (ImageView) v.findViewById(R.id.img_user);
            iv_cross = v.findViewById(R.id.img_right);

            iv_cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callUnfavourite(pos);
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
//                        Intent intent = new Intent(context, RequestDetailsActivity.class);
//                        intent.putExtra(StaticField.ARG_PROJECT_DETAILS, list.get(pos));
//                        intent.putExtra(StaticField.ARG_SCREEN_TYPE, "NewJob");
//                        ((DrawerActivity)context).startActivityForResult(intent, StaticField.REQUEST_DETAIL_FORM);
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if (list.get(pos).getProfile() != null && list.get(pos).getProfile() != null
                        && !TextUtils.isEmpty(list.get(pos).getProfile())) {
                    Picasso.with(context)
                            .load(list.get(pos).getProfile())
                            .into(img_user);
                }

                if (list.get(pos).getName() != null && !TextUtils.isEmpty(list.get(pos).getName()))
                    txt_user_name.setText(list.get(pos).getName());
                if (list.get(pos).getLastName() != null && !TextUtils.isEmpty(list.get(pos).getLastName()))
                    txt_location.setText(list.get(pos).getLastName());
            }
        }
    }
}
