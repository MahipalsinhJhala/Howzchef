package com.houz.chef.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.AddAddressBean;
import com.houz.chef.modelBean.Address;
import com.houz.chef.modelBean.User;
import com.houz.chef.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.j on 6/16/2018.
 */

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ViewHolder> {

    private ArrayList<AddAddressBean> addressList;
    private Context context;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;

    public MyAddressAdapter(Context context,RecyclerView recyclerView) {
        addressList= new ArrayList<>();
        this.context = context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(linearLayoutManager != null) {
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    public void clearAll() {
        addressList.clear();
        notifyDataSetChanged();
    }

    public void add(AddAddressBean address){
        addressList.add(address);
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<AddAddressBean> data) {

        addressList.addAll(data);
        notifyDataSetChanged();
    }

    public void put(int pos,AddAddressBean address){
        addressList.set(pos,address);
        notifyDataSetChanged();
    }

    public void remove(int pos){
        addressList.remove(pos);
        notifyDataSetChanged();
    }

    public AddAddressBean getItem(int position){
        return addressList.get(position);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txt_title;
        private TextView txt_address;
        private ImageView iv_default,iv_edit,iv_delete;
        private RelativeLayout rl_address;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_address=itemView.findViewById(R.id.rl_address_item);
            txt_title = itemView.findViewById(R.id.tv_label_address);
            txt_address = itemView.findViewById(R.id.tv_address);
            iv_default = itemView.findViewById(R.id.iv_address_default);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_remove);
        }

        public void bindData(final int position) {
            this.pos = position;
            if (addressList != null && pos >= 0 && pos < addressList.size() && addressList.get(pos) != null) {
                AddAddressBean addAddressBean= addressList.get(position);
                if(addAddressBean.getAddress()!=null && !addAddressBean.getAddress().equals("null")) {
                    Address address = new Address(addAddressBean.getAddress());

                    txt_title.setText(address.getTitle());
                    txt_address.setText(address.getAddress1());

                    if(addAddressBean.getIsDefault()==1){
                        iv_default.setVisibility(View.VISIBLE);
                    }else{
                        iv_default.setVisibility(View.INVISIBLE);
                    }

                    rl_address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            OnItemClickListener onItemClickListener=(OnItemClickListener)v.getContext();
                            if(onItemClickListener!=null){
                                if(addressList.get(pos).getIsDefault()!=1) {
                                    onItemClickListener.onItemClick(v, pos);
                                }
                            }


                        }
                    });


                    iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            OnItemClickListener onItemClickListener=(OnItemClickListener)v.getContext();
                            if(onItemClickListener!=null){
                                if(addressList.get(pos).getIsDefault()!=1) {
                                    onItemClickListener.onItemClick(v, pos);
                                }else{
                                    CommonUtils.toast(context,"Can not delete default address.");
                                }
                            }
                        }
                    });


                    iv_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            OnItemClickListener onItemClickListener=(OnItemClickListener)v.getContext();
                            if(onItemClickListener!=null){
                                onItemClickListener.onItemClick(v,pos);
                            }
                        }
                    });
                }
            }
        }

    }
}
