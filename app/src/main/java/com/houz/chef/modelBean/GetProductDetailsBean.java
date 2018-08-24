package com.houz.chef.modelBean;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProductDetailsBean<T> {

    public GetProductDetailsBean() {
    }

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private ProductBean data;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ProductBean getData() {
        return data;
    }

    public void setData(ProductBean data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}