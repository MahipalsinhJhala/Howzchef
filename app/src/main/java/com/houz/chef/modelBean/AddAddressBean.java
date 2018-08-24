package com.houz.chef.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by m.j on 6/21/2018.
 */

public class AddAddressBean implements Parcelable{

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("address")
    private String address;

    private Address addressModel;

    @SerializedName("is_default")
    private int is_default;

    @SerializedName("id")
    private int id;

    public AddAddressBean(){}

    protected AddAddressBean(Parcel in) {
        user_id = in.readInt();
        address = in.readString();
        is_default = in.readInt();
        id = in.readInt();
    }

    public static final Creator<AddAddressBean> CREATOR = new Creator<AddAddressBean>() {
        @Override
        public AddAddressBean createFromParcel(Parcel in) {
            return new AddAddressBean(in);
        }

        @Override
        public AddAddressBean[] newArray(int size) {
            return new AddAddressBean[size];
        }
    };



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public int getIsDefault() {
        return is_default;
    }

    public void setIsDefault(int isDefault) {
        this.is_default = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeString(address);
        dest.writeInt(is_default);
        dest.writeInt(id);
    }



    public Address getAddressModel() {
        addressModel=new Address(address);
        return addressModel;
    }
}
