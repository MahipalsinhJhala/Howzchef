package com.houz.chef.modelBean;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBean extends BaseObservable implements Parcelable {

    public LoginBean() {
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_code")
    @Expose
    private String status_code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("user")
    @Expose
    private AboutMe user;

    @SerializedName("data")
    @Expose
    private AboutMe data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public AboutMe getUser() {
        return user;
    }

    public void setUser(AboutMe user) {
        this.user = user;
    }

    public AboutMe getData() {
        return data;
    }

    public void setData(AboutMe data) {
        this.data = data;
    }

    protected LoginBean(Parcel in) {
        status = in.readString();
        status_code = in.readString();
        message = in.readString();
        error = in.readString();
        user = (AboutMe) in.readValue(AboutMe.class.getClassLoader());
        data = (AboutMe) in.readValue(AboutMe.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(status);
        dest.writeString(status_code);
        dest.writeString(message);
        dest.writeString(error);
        dest.writeValue(user);
        dest.writeValue(data);
    }

    @SuppressWarnings("unused")
    public static final Creator<LoginBean> CREATOR = new Creator<LoginBean>() {
        @Override
        public LoginBean createFromParcel(Parcel in) {
            return new LoginBean(in);
        }

        @Override
        public LoginBean[] newArray(int size) {
            return new LoginBean[size];
        }
    };
}