package com.houz.chef.modelBean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 9/4/2018.
 */

public class UpdateSettingResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("status_code")
    private int status_code;

    @SerializedName("data")
    private UpdateSettingModel updateSettingModel;

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public UpdateSettingModel getUpdateSettingModel() {
        return updateSettingModel;
    }

    public void setUpdateSettingModel(UpdateSettingModel updateSettingModel) {
        this.updateSettingModel = updateSettingModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
