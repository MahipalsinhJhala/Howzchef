package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by m.j on 7/1/2018.
 */

public class PaymentBean extends ModelBean{

    @SerializedName("data")
    @Expose
    private PaymentData data;

    public PaymentData getData() {
        return data;
    }

    public void setData(PaymentData data) {
        this.data = data;
    }
}
