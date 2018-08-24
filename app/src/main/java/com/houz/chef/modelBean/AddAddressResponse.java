package com.houz.chef.modelBean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m.j on 6/21/2018.
 */

public class AddAddressResponse {


    @SerializedName("status")
    private boolean status;

    @SerializedName("status_code")
    private int status_code;

    @SerializedName("data")
    private AddAddressBean addAddressBean;

    @SerializedName("message")
    private String message;



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



    public AddAddressBean getAddAddressBean() {
        return addAddressBean;
    }

    public void setAddAddressBean(AddAddressBean addAddressBean) {
        this.addAddressBean = addAddressBean;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }
}
