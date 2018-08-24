
package com.houz.chef.modelBean;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFavouriteChefList {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Integer> data = null;
    @SerializedName("chefs")
    @Expose
    private ArrayList<User> chefs = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public ArrayList<User> getChefs() {
        return chefs;
    }

    public void setChefs(ArrayList<User> chefs) {
        this.chefs = chefs;
    }

}
