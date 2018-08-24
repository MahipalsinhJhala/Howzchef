package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class OrderHeaderBean {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("status")
    @Expose
    private String status;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
