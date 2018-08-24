package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by m.j on 6/27/2018.
 */

public class QuantityUpdateResponse extends ModelBean {

    @SerializedName("data")
    @Expose
    private Datum data;

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }
}
