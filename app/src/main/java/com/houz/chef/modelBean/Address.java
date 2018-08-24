package com.houz.chef.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m.j on 6/16/2018.
 */

public class Address implements Parcelable{



    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("address1")
    private String address1;
    @SerializedName("address2")
    private String address2;

    private int isDefault;
    @SerializedName("state")
    private String state;
    @SerializedName("pincode")
    private String pincode;
    @SerializedName("city")
    private String city;
    @SerializedName("number")
    private String phone;
    @SerializedName("country")
    private String country;

    public Address(){}

    protected Address(Parcel in) {
        id = in.readInt();
        title = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        isDefault = in.readInt();
        state = in.readString();
        pincode = in.readString();
        city = in.readString();
        phone = in.readString();
        country = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Address(String address) {

        try {
            JSONObject jsonObject=new JSONObject(address.replace("\"{","{").replace("}\"","}").replace("\\",""));
            JSONObject jsonAddress=jsonObject.getJSONObject("address");
            this.setTitle(jsonAddress.getString("title"));
            this.setAddress1(jsonAddress.getString("address1"));
            this.setAddress2(jsonAddress.getString("address2"));
            this.setCity(jsonAddress.getString("city"));
            this.setState(jsonAddress.getString("state"));
            this.setCountry(jsonAddress.getString("country"));
            this.setPincode(jsonAddress.getString("zipcode"));
            this.setPhone(jsonAddress.getString("number"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeInt(isDefault);
        dest.writeString(state);
        dest.writeString(pincode);
        dest.writeString(city);
        dest.writeString(phone);
        dest.writeString(country);
    }
}
