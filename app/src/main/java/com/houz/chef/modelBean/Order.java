package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by m.j on 7/7/2018.
 */

public class Order {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("total")
    private int total;

    @SerializedName("commission")
    private int commission;

    @SerializedName("status")
    private int status;

    @SerializedName("delivery_address")
    private String delivery_address;

    @SerializedName("promo_code")
    private String promo_code;

    @SerializedName("promo_discount")
    private String promo_discount;

    @SerializedName("last_four")
    private String last_four;

    @SerializedName("charge_id")
    private String charge_id;

    @SerializedName("products")
    @Expose
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getPromo_discount() {
        return promo_discount;
    }

    public void setPromo_discount(String promo_discount) {
        this.promo_discount = promo_discount;
    }

    public String getLast_four() {
        return last_four;
    }

    public void setLast_four(String last_four) {
        this.last_four = last_four;
    }

    public String getCharge_id() {
        return charge_id;
    }

    public void setCharge_id(String charge_id) {
        this.charge_id = charge_id;
    }
}
