package com.houz.chef.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.interfaces.DrawerListener;
import com.houz.chef.modelBean.DrawerBean;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private DrawerListener drawerListener;
    private ArrayList<DrawerBean> recommendList;

    public DrawerAdapter(Context context, ArrayList<DrawerBean> recommendBean, DrawerListener drawerListener) {
        this.context = context;
        this.recommendList = recommendBean;
        this.drawerListener = drawerListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_drawer, parent, false);
        return new Header(v);
    }

    @Override
    public int getItemViewType(int position) {
        return recommendList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Header) {
            ((Header) holder).bindData(position);
        }
    }

    @Override
    public int getItemCount() {
        return recommendList.size();
    }

    private class Header extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txtName;
        private ImageView imgCount;

        Header(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.txt_name);
            imgCount = (ImageView) v.findViewById(R.id.img_count);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(drawerListener != null && recommendList != null && pos >= 0 && pos< recommendList.size())
                        drawerListener.onClick(recommendList.get(pos));
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if(recommendList != null && pos >= 0 && pos < recommendList.size()) {
                if (recommendList.get(pos) != null && recommendList.get(pos).getName() != null)
                    txtName.setText(recommendList.get(pos).getName());
                if (recommendList.get(pos) != null && recommendList.get(pos).getNotiCount() >= 0) {
                    imgCount.setVisibility(View.VISIBLE);
                } else
                    imgCount.setVisibility(View.GONE);
            } else
                imgCount.setVisibility(View.GONE);
        }
    }
}
