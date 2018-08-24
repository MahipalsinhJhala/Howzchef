package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 8/24/2018.
 */

public class NotificationResponse {
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("lastPage")
    @Expose
    private int lastPage;
    @SerializedName("hasMorePages")
    @Expose
    private boolean hasMorePages;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("currentPage")
    @Expose
    private int currentPage;
    @SerializedName("previousPageUrl")
    @Expose
    private String previousPageUrl;
    @SerializedName("nextPageUrl")
    @Expose
    private String nextPageUrl;
    @SerializedName("perPage")
    @Expose
    private int perPage;
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

    @SerializedName("notifications")
    @Expose
    private NotificationModel notificationModel;

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

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    public void setNotificationModel(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }
}
