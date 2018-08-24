package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.j on 7/7/2018.
 */

public class OrderData extends ModelBean {

    @SerializedName("total")
    private int total;

    @SerializedName("lastPage")
    private int lastPage;

    @SerializedName("hasMorePages")
    private boolean hasMorePages;

    @SerializedName("count")
    private int count;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("previousPageUrl")
    private String previousPageUrl;

    @SerializedName("nextPageUrl")
    private String nextPageUrl;

    @SerializedName("perPage")
    private int perPage;

    @SerializedName("orders")
    @Expose
    private ArrayList<Order> orders;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getPreviousPageUrl() {
        return previousPageUrl;
    }

    public void setPreviousPageUrl(String previousPageUrl) {
        this.previousPageUrl = previousPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
