
package com.houz.chef.modelBean;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("user")
    @Expose
    private AboutMe user;
    @SerializedName("products")
    @Expose
    private List<ProductBean> products = null;

    public AboutMe getUser() {
        return user;
    }

    public void setUser(AboutMe user) {
        this.user = user;
    }

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

}
