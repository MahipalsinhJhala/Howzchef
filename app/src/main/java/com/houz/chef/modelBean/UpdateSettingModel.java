package com.houz.chef.modelBean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 9/4/2018.
 */

public class UpdateSettingModel {

    @SerializedName("is_recipe_added")
    private int isRecipeAdded;
    @SerializedName("is_recipe_change")
    private int isRecipeChange;
    @SerializedName("is_order_update")
    private int isOrderUpdate;
    @SerializedName("is_chat_msg")
    private int isChatMsg;
    @SerializedName("updated_at")
    private String updatedAt;

    public int getIsRecipeAdded() {
        return isRecipeAdded;
    }

    public void setIsRecipeAdded(int isRecipeAdded) {
        this.isRecipeAdded = isRecipeAdded;
    }

    public int getIsRecipeChange() {
        return isRecipeChange;
    }

    public void setIsRecipeChange(int isRecipeChange) {
        this.isRecipeChange = isRecipeChange;
    }

    public int getIsOrderUpdate() {
        return isOrderUpdate;
    }

    public void setIsOrderUpdate(int isOrderUpdate) {
        this.isOrderUpdate = isOrderUpdate;
    }

    public int getIsChatMsg() {
        return isChatMsg;
    }

    public void setIsChatMsg(int isChatMsg) {
        this.isChatMsg = isChatMsg;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
