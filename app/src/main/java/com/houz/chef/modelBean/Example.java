package com.houz.chef.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example implements Parcelable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("contractor_id")
    @Expose
    private int contractorId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("categories")
    @Expose
    private Categories categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    protected Example(Parcel in) {
        id = in.readInt();
        categoryId = in.readInt();
        contractorId = in.readInt();
        createdAt = in.readString();
        categories = (Categories) in.readValue(Categories.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(categoryId);
        dest.writeInt(contractorId);
        dest.writeString(createdAt);
        dest.writeValue(categories);
    }

    @SuppressWarnings("unused")
    public static final Creator<Example> CREATOR = new Creator<Example>() {
        @Override
        public Example createFromParcel(Parcel in) {
            return new Example(in);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };
}