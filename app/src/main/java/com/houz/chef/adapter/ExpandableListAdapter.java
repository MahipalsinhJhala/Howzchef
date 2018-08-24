package com.houz.chef.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.modelBean.Order;
import com.houz.chef.modelBean.OrderData;
import com.houz.chef.modelBean.OrderHeaderBean;
import com.houz.chef.modelBean.Product;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.utils.Const;
import com.houz.chef.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private List<Order> order_arr;
    private Context context;
    Viewholder vh = null;
    ExpandableListView mExpandableListView;

    public ExpandableListAdapter(Context context, List<Order> order_arr) {

        this.context = context;
        this.order_arr = order_arr;
    }

    @Override
    public Product getChild(int groupPosition, int childPosititon) {
        return this.order_arr.get(groupPosition).getProducts().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            vh = new Viewholder();
            convertView = infalInflater.inflate(R.layout.item_orders, null);

            vh.txt_order_name = (TextView) convertView.findViewById(R.id.txt_order_name);
            vh.txt_order_price = (TextView) convertView.findViewById(R.id.txt_order_price);
            vh.txt_chef_name = (TextView) convertView.findViewById(R.id.txt_chef_name);
            vh.img_order = (ImageView) convertView.findViewById(R.id.img_order);
            vh.ivRemove = convertView.findViewById(R.id.iv_remove);
            vh.ivRemove.setVisibility(View.GONE);
            vh.ratingBar=convertView.findViewById(R.id.rb_item);
            convertView.setTag(vh);
        } else
            vh = (Viewholder) convertView.getTag();

        try {
            Product product = order_arr.get(groupPosition).getProducts().get(childPosition);
            if (product.getImage().length() > 0) {
                Picasso.with(activity)
                        .load(product.getImage())
                        .transform(new RoundedTransformation(5, 4))
                        .resizeDimen(R.dimen.rounded_img, R.dimen.rounded_img)
                        .centerCrop()
                        .into(vh.img_order);
            }
            vh.txt_order_name.setText(product.getName());
            //vh.txt_chef_name.setText(product.());
            vh.rb_item.setRating(product.getRating());
            vh.txt_order_price.setText(product.getPrice());

        } catch (Exception e) {
            Log.e("ExpandableListAdapter", "" + e.getLocalizedMessage());
        }

        return convertView;
    }

    private static class Viewholder {
        ImageView img_order, ivRemove;
        RatingBar ratingBar;
        TextView txt_order_name, txt_order_price, txt_chef_name;
        MaterialRatingBar rb_item;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.order_arr.get(groupPosition).getProducts().size();
    }

    @Override
    public Order getGroup(int groupPosition) {
        return this.order_arr.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.order_arr.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, final ViewGroup parent) {

        mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        int pos = groupPosition + 1;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_orders_header, null);
        }
        Order order = getGroup(groupPosition);
//        Log.e("group size: "," "+this.pack_id_arr.size());

        TextView txt_order_id = (TextView) convertView.findViewById(R.id.txt_order_id);

        txt_order_id.setText("Order #" + order.getId());

        TextView txt_order_price = (TextView) convertView.findViewById(R.id.txt_order_price);

        txt_order_price.setText("$" + order_arr.get(groupPosition).getTotal());

        TextView txt_order_status = (TextView) convertView.findViewById(R.id.txt_order_status);



        String order_status="";

        if (order_arr.get(groupPosition).getStatus() == Const.ORDER_PLACED) {
            order_status="Accepted";
            txt_order_status.setTextColor(context.getResources().getColor(R.color.green));
        }else if (order_arr.get(groupPosition).getStatus() == Const.ORDER_DELIVERY_OUT) {
            order_status="Delivery Out";
            txt_order_status.setTextColor(context.getResources().getColor(R.color.red));
        }else if (order_arr.get(groupPosition).getStatus() == Const.ORDER_PROCESSING) {
            order_status="In Process";
            txt_order_status.setTextColor(context.getResources().getColor(R.color.orange));
        }
        txt_order_status.setText(order_status);
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
