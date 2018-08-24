package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 8/7/2018.
 */

public class ReferralCodeResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status_code")
    @Expose
    private int status_code;

    @SerializedName("data")
    private ReferralModel referralModel;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public ReferralModel getReferralModel() {
        return referralModel;
    }

    public void setReferralModel(ReferralModel referralModel) {
        this.referralModel = referralModel;
    }

    public class ReferralModel {
        @SerializedName(value = "Referral Code", alternate = "Referral_Code")
        public String referralCode;
        @SerializedName("userid")
        public String userId;

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
