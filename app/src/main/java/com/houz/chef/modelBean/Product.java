
package com.houz.chef.modelBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("ingredients")
    @Expose
    private String ingredients;
    @SerializedName("owner_id")
    @Expose
    private Integer ownerId;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("making_time")
    @Expose
    private Integer makingTime;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("sale_price")
    @Expose
    private Integer salePrice;
    @SerializedName("commission")
    @Expose
    private Integer commission;
    @SerializedName("is_veg")
    @Expose
    private Integer isVeg;
    @SerializedName("specifications")
    @Expose
    private String specifications;
    @SerializedName("delivery_options")
    @Expose
    private String deliveryOptions;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image1")
    @Expose
    private String image1;
    @SerializedName("image2")
    @Expose
    private String image2;
    @SerializedName("image3")
    @Expose
    private String image3;
    @SerializedName("image4")
    @Expose
    private String image4;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("calorie")
    @Expose
    private String calorie;
    @SerializedName("protein")
    @Expose
    private String protein;
    @SerializedName("carbs")
    @Expose
    private String carbs;
    @SerializedName("fat")
    @Expose
    private String fat;
    @SerializedName("fiber")
    @Expose
    private String fiber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getMakingTime() {
        return makingTime;
    }

    public void setMakingTime(Integer makingTime) {
        this.makingTime = makingTime;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getCommission() {
        return commission;
    }

    public void setCommission(Integer commission) {
        this.commission = commission;
    }

    public Integer getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(Integer isVeg) {
        this.isVeg = isVeg;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(String deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getFiber() {
        return fiber;
    }

    public void setFiber(String fiber) {
        this.fiber = fiber;
    }

}
