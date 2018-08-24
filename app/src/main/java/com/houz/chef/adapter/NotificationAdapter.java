package com.houz.chef.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.activity.ActivityChefDetail;
import com.houz.chef.activity.ActivityProductDetail;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.NotificationModel;
import com.houz.chef.modelBean.NotificationResponse;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.utils.StaticField;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private ArrayList<NotificationModel> list;
    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    private OnItemClickListener onItemClickListener;

    public NotificationAdapter(Context context, RecyclerView recyclerView, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
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
                        isLoading = true;
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
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
            View view = LayoutInflater.from(context).inflate(R.layout.notification_item_list, parent, false);
            return new NotificationAdapter.NotificationHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false);
            return new NotificationAdapter.LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationHolder) {
            ((NotificationHolder) holder).bindData(position);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void clearList() {
        list.clear();
    }

    public ArrayList<NotificationModel> getList() {
        return list;
    }

    public void addAllList(ArrayList<NotificationModel> data) {
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

    private class NotificationHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvMessage;
        private int pos;
        NotificationHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvMessage = v.findViewById(R.id.tvMessage);
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                tvTitle.setText(list.get(position).getTitle());
                tvMessage.setText(list.get(position).getMessage());
            }
        }
    }
}
