package com.houz.chef.modelBean;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetProductBean<T> extends BaseObservable implements Parcelable {

    public GetProductBean() {
    }

    @SerializedName("current_page")
    @Expose
    private String current_page;


    @SerializedName("lastPage")
    @Expose
    private String last_page;

    @SerializedName("status_code")
    @Expose
    private String status_code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName(value = "products",alternate = "notifications")
    @Expose
    private T result;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    protected GetProductBean(Parcel in) {
        current_page = in.readString();
        status_code = in.readString();
        message = in.readString();
        error = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(current_page);
        dest.writeString(status_code);
        dest.writeString(message);
        dest.writeString(error);
    }

    @SuppressWarnings("unused")
    public static final Creator<GetProductBean> CREATOR = new Creator<GetProductBean>() {
        @Override
        public GetProductBean createFromParcel(Parcel in) {
            return new GetProductBean(in);
        }

        @Override
        public GetProductBean[] newArray(int size) {
            return new GetProductBean[size];
        }
    };

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }
}