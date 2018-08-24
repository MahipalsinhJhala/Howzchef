package com.houz.chef.modelBean;

import android.provider.Telephony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by m.j on 6/18/2018.
 */

public class GetAddressList {

    @SerializedName("status")
    private Boolean status;

    @SerializedName("data")
    @Expose
    private AddressData data;

    @SerializedName("status_code")
    private int statusCode;


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public AddressData getData() {
        return data;
    }

    public void setData(AddressData data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
