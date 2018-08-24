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
import com.houz.chef.activity.ActivityCart;
import com.houz.chef.activity.ActivityChefDetail;
import com.houz.chef.activity.ActivityProductDetail;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.utils.StaticField;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private ArrayList<ProductBean> list;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    private OnItemClickListener onItemClickListener;

    public HomeAdapter(Context context, RecyclerView recyclerView, OnItemClickListener onItemClickListener) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
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

    public ArrayList<ProductBean> getList() {
        return list;
    }

    public void addAllList(ArrayList<ProductBean> data) {
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

    private class Header extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txt_price, tv_quantity;
        private TextView txt_time;
        private TextView txt_item_name;
        private TextView txt_chef_name;
        private RatingBar rb_item;
        private ImageView img_menu, iv_plus, iv_minus;
        private RelativeLayout rlQuantity;

        Header(View v) {
            super(v);
            txt_price = (TextView) v.findViewById(R.id.txt_price);
            txt_time = (TextView) v.findViewById(R.id.txt_time);
            txt_item_name = (TextView) v.findViewById(R.id.txt_item_name);
            txt_chef_name = (TextView) v.findViewById(R.id.txt_chef_name);
            rb_item = (RatingBar) v.findViewById(R.id.rb_item);
            img_menu = (ImageView) v.findViewById(R.id.img_menu);
            iv_plus = (ImageView) v.findViewById(R.id.iv_plus);
            iv_minus = (ImageView) v.findViewById(R.id.iv_minus);
            tv_quantity = (TextView) v.findViewById(R.id.tv_quantity);
            rlQuantity = v.findViewById(R.id.rl_quatity);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        Intent intent = new Intent(context, ActivityProductDetail.class);
                        intent.putExtra(StaticField.ARG_PRODUCTID, list.get(pos).getId());
                        context.startActivity(intent);
                    }
                }
            });

            iv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, pos);
                    }
                }
            });


            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, pos);
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if (list.get(pos).getImage() != null && list.get(pos).getImage() != null
                        && !TextUtils.isEmpty(list.get(pos).getImage())) {
                    Picasso.with(context)
                            .load(list.get(pos).getImage())
                            .into(img_menu);
                }

                if(context instanceof ActivityChefDetail){
                    rlQuantity.setVisibility(View.GONE);
                }else {
                    rlQuantity.setVisibility(View.VISIBLE);
                }
                tv_quantity.setText("" + list.get(pos).getQty());
                if (list.get(pos).getIs_veg() != null && !TextUtils.isEmpty(list.get(pos).getIs_veg())) {
                    if (list.get(pos).getIs_veg().equalsIgnoreCase("1"))
                        txt_item_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.veg_icon, 0);
                    else
                        txt_item_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.nonveg_icon, 0);
                }
                if (list.get(pos).getName() != null && !TextUtils.isEmpty(list.get(pos).getName()))
                    txt_item_name.setText(list.get(pos).getName());
                if (list.get(pos).getMaking_time() != null && !TextUtils.isEmpty(list.get(pos).getMaking_time()))
                    txt_time.setText(list.get(pos).getMaking_time() + " Min");
                if (list.get(pos).getPrice() != null && !TextUtils.isEmpty(list.get(pos).getPrice()))
                    txt_price.setText("$" + list.get(pos).getPrice());
                if (list.get(pos).getUser() != null && list.get(pos).getUser().getName() != null && !TextUtils.isEmpty(list.get(pos).getUser().getName()))
                    txt_chef_name.setText(list.get(pos).getUser().getName());
                if (list.get(pos).getRating() != null && !TextUtils.isEmpty(list.get(pos).getRating()))
                    rb_item.setRating(Float.parseFloat(list.get(pos).getRating()));
            }
        }
    }

}
