package com.houz.chef.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.activity.ActivityCart;
import com.houz.chef.activity.ActivityChefDetail;
import com.houz.chef.activity.ActivityProductDetail;
import com.houz.chef.activity.DrawerActivity;
import com.houz.chef.interfaces.OnItemClickListener;
import com.houz.chef.interfaces.OnLoadMoreListener;
import com.houz.chef.modelBean.Datum;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.modelBean.PromoDataBean;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Const;
import com.houz.chef.utils.RoundedTransformation;
import com.houz.chef.utils.StaticField;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_FOOTER= 1;
    private Context context;
    private ArrayList<Datum> list;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    private float subTotal,grandTotal,deliveryCharge;
    private float promoDiscount;
    private PromoDataBean promoCode;
    public CartAdapter(Context context, RecyclerView recyclerView) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_orders, parent, false);
            return new Header(view);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.lyt_cart_recycle_bottom, parent, false);
            return new BottomViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_FOOTER: VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Header) {
            ((Header) holder).bindData(position);
        } else if (holder instanceof BottomViewHolder) {
            BottomViewHolder loadingViewHolder = (BottomViewHolder) holder;
            loadingViewHolder.bindData(false);
        }
    }

    public void setPromoCode(PromoDataBean promoCode){
        this.promoCode=promoCode;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<Datum> getProductList(){
        return  list;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAllList(ArrayList<Datum> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public Datum getItem(int pos){
        return list.get(pos);
    }

    public void putItem(int pos,Datum datum){
        list.set(pos,datum);
        notifyDataSetChanged();
    }


    public PromoDataBean getPromoCode(){
        return this.promoCode;
    }

    public float getDiscount(){
        return promoDiscount;
    }

    public float getTotal(){
        return grandTotal;
    }

    public void remove(int pos) {
        list.remove(pos);
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

    private class BottomViewHolder extends RecyclerView.ViewHolder {
        public EditText edtPromo;
        public Button btnAPply,btnAddress;
        public TextView txtSubTotal,txtDeliveryCharge,txtGrandTotal;
        public ImageView ivRemovePromo;

        public BottomViewHolder(View view) {
            super(view);
            edtPromo=view.findViewById(R.id.edt_promo);
            btnAPply=view.findViewById(R.id.btn_apply);
            btnAddress=view.findViewById(R.id.btn_address);
            txtSubTotal=view.findViewById(R.id.txt_sub_total);
            txtDeliveryCharge=view.findViewById(R.id.txt_delivery_charge);
            txtGrandTotal=view.findViewById(R.id.txt_grand_total);
            ivRemovePromo=view.findViewById(R.id.iv_remove_promo);


            ivRemovePromo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //OnItemClickListener onItemClickListener=(OnItemClickListener) v.getContext();

                    //if(onItemClickListener!=null){
                        if(promoCode!=null){

                            BottomViewHolder.this.bindData(true);
                            //onItemClickListener.onItemClick(v,0);

                        }
                    //}
                }
            });

            btnAPply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnItemClickListener onItemClickListener = (OnItemClickListener) v.getContext();
                    if (onItemClickListener != null ) {
                        if(!edtPromo.getText().toString().isEmpty()) {
                            v.setTag(R.string.tag_promo_code, edtPromo.getText().toString().trim());
                            onItemClickListener.onItemClick(v, list.size());
                        }else{
                            CommonUtils.toast(context,"Please enter promo code");
                        }
                    }
                }
            });

            btnAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnItemClickListener onItemClickListener=(OnItemClickListener) v.getContext();

                    if(onItemClickListener!=null){
                            onItemClickListener.onItemClick(v,0);
                    }
                }
            });
        }

        public void bindData(boolean isRemove) {

            subTotal = 0;
            grandTotal = 0;

            for (Datum datum : getProductList()) {
                if(datum!=null)
                subTotal += (datum.getProduct().getSalePrice() * datum.getQty());
            }

            if (promoCode != null) {
                if(isRemove){
                    edtPromo.setText("");
                    edtPromo.setEnabled(true);
                    ivRemovePromo.setVisibility(View.INVISIBLE);
                    promoDiscount=0;
                    promoCode=null;
                }else {
                    edtPromo.setEnabled(false);
                    edtPromo.setText(promoCode.getName());
                    ivRemovePromo.setVisibility(View.VISIBLE);
                    if (promoCode.getType().equals(Const.PROMO_TYPE_FIXED)) {
                        promoDiscount=promoCode.getPromo_val();
                        subTotal -= promoDiscount;
                    } else {
                        promoDiscount=(subTotal * promoCode.getPromo_val()) / 100;
                        subTotal -= promoDiscount;
                    }
                }


            }
            grandTotal = subTotal + deliveryCharge;

            txtSubTotal.setText("" + subTotal);
            txtDeliveryCharge.setText("" + deliveryCharge);
            txtGrandTotal.setText("" + grandTotal);
        }
    }

    private class Header extends RecyclerView.ViewHolder {
        private int pos;
        private TextView txt_order_price;
        private TextView txt_order_name;
        private TextView txt_chef_name,tvQuantity;
        private RatingBar rb_item;
        private RelativeLayout rl_item,rlQuantity;
        private ImageView img_order, ivRemove,ivPlus,ivMinus;
        private Spinner spinner;
        Header(View v) {
            super(v);
            rl_item = (RelativeLayout) v.findViewById(R.id.rl_item);
            txt_order_price = (TextView) v.findViewById(R.id.txt_order_price);
            txt_order_name = (TextView) v.findViewById(R.id.txt_order_name);
            txt_chef_name = (TextView) v.findViewById(R.id.txt_chef_name);
            rb_item = (RatingBar) v.findViewById(R.id.rb_item);
            tvQuantity=v.findViewById(R.id.tv_quantity);
            ivPlus=v.findViewById(R.id.iv_plus);
            ivMinus=v.findViewById(R.id.iv_minus);
            img_order = (ImageView) v.findViewById(R.id.img_order);
            ivRemove = v.findViewById(R.id.iv_remove);
            spinner=v.findViewById(R.id.spr_quantity);
            rlQuantity=v.findViewById(R.id.rl_quatity);
            //spinner.setVisibility(View.VISIBLE);

         /*   rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                        Intent intent = new Intent(context, ActivityProductDetail.class);
                        intent.putExtra(StaticField.ARG_PRODUCTID, list.get(pos).getId());
                        ((ActivityCart) context).startActivity(intent);
                    }
                }
            });
*/


            if(context instanceof ActivityCart){
                rlQuantity.setVisibility(View.VISIBLE);
            }else {
                rlQuantity.setVisibility(View.GONE);
            }


            ivPlus.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 OnItemClickListener onItemClickListener = (OnItemClickListener) context;
                 if (onItemClickListener != null) {
                     view.setTag(R.string.tag_quantity, spinner.getAdapter().getItem(pos));
                     onItemClickListener.onItemClick(view, pos);
                 }

             }
         });


         ivMinus.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 OnItemClickListener onItemClickListener = (OnItemClickListener) context;
                 if (onItemClickListener != null) {
                     view.setTag(R.string.tag_quantity, spinner.getAdapter().getItem(pos));
                     onItemClickListener.onItemClick(view, pos);
                 }

             }
         });
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 if(view!=null) {
                     OnItemClickListener onItemClickListener = (OnItemClickListener) context;

                     if (onItemClickListener != null) {
                         view.setTag(R.string.tag_quantity, spinner.getAdapter().getItem(position));
                         onItemClickListener.onItemClick(view, pos);
                     }
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }

         });
            ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnItemClickListener onItemClickListener = (OnItemClickListener) v.getContext();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, pos);
                    }
                }
            });
        }

        public void bindData(int position) {
            this.pos = position;
            if (list != null && pos >= 0 && pos < list.size() && list.get(pos) != null) {
                if (list.get(pos).getProduct().getImage() != null && list.get(pos).getProduct().getImage() != null
                        && !TextUtils.isEmpty(list.get(pos).getProduct().getImage())) {
                    Picasso.with(context)
                            .load(list.get(pos).getProduct().getImage())
                            .transform(new RoundedTransformation(5, 4))
                            .resizeDimen(R.dimen.rounded_img, R.dimen.rounded_img)
                            .centerCrop()
                            .into(img_order);
                }

                tvQuantity.setText(""+list.get(position).getQty());
                //spinner.setSelection(list.get(pos).getQty()-1,false);

                if (list.get(pos).getProduct().getIsVeg() != null) {
                    if (list.get(pos).getProduct().getIsVeg() == 1)
                        txt_order_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.veg_icon, 0);
                    else
                        txt_order_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.nonveg_icon, 0);
                }
                if (list.get(pos).getProduct().getName() != null && !TextUtils.isEmpty(list.get(pos).getProduct().getName()))
                    txt_order_name.setText(list.get(pos).getProduct().getName());
                if (list.get(pos).getPrice() != null)
                    txt_order_price.setText("$" + list.get(pos).getProduct().getSalePrice());
                if (list.get(pos).getProduct().getRating() != null)
                    rb_item.setRating(Float.parseFloat("" + list.get(pos).getProduct().getRating()));
            }
        }
    }

}
