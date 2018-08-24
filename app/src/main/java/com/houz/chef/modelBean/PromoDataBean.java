package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by m.j on 6/26/2018.
 */

public class PromoDataBean {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("promo_val")
    @Expose
    private float promo_val;

    @SerializedName("limit")
    @Expose
    private int limit;

    @SerializedName("minimum_cart_total")
    @Expose
    private float minimum_cart_total;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPromo_val() {
        return promo_val;
    }

    public void setPromo_val(float promo_val) {
        this.promo_val = promo_val;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public float getMinimum_cart_total() {
        return minimum_cart_total;
    }

    public void setMinimum_cart_total(float minimum_cart_total) {
        this.minimum_cart_total = minimum_cart_total;
    }
}
