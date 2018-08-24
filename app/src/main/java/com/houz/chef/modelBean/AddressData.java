package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by m.j on 6/18/2018.
 */

public class AddressData {

    @SerializedName("user")
    private AboutMe aboutMe;

    @SerializedName("address_list")
    private ArrayList<AddAddressBean> addressArrayList;


    public AboutMe getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(AboutMe aboutMe) {
        this.aboutMe = aboutMe;
    }

    public ArrayList<AddAddressBean> getAddressArrayList() {
        return addressArrayList;
    }

    public void setAddressArrayList(ArrayList<AddAddressBean> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }
}
