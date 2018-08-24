package com.houz.chef.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductBean implements Parcelable {
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
	private String owner_id;
	@SerializedName("rating")
	@Expose
	private String rating;
	@SerializedName("making_time")
	@Expose
	private String making_time;
	@SerializedName("price")
	@Expose
	private String price;
	@SerializedName("sale_price")
	@Expose
	private String sale_price;
	@SerializedName("commission")
	@Expose
	private String commission;
	@SerializedName("is_veg")
	@Expose
	private String is_veg;
	@SerializedName("specifications")
	@Expose
	private String specifications;
	@SerializedName("delivery_options")
	@Expose
	private String delivery_options;
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
	@SerializedName("discount")
	@Expose
	private String discount;
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
	@SerializedName("created_at")
	@Expose
	private String created_at;
	@SerializedName("user")
	@Expose
	private AboutMe user;

	@SerializedName("qty")
	@Expose
	private int qty;

	@SerializedName("is_favourite")
	@Expose
	private boolean is_favourite;

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

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getMaking_time() {
		return making_time;
	}

	public void setMaking_time(String making_time) {
		this.making_time = making_time;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getIs_veg() {
		return is_veg;
	}

	public void setIs_veg(String is_veg) {
		this.is_veg = is_veg;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getDelivery_options() {
		return delivery_options;
	}

	public void setDelivery_options(String delivery_options) {
		this.delivery_options = delivery_options;
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

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
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

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public AboutMe getUser() {
		return user;
	}

	public void setUser(AboutMe user) {
		this.user = user;
	}

	public ProductBean() {
	}

	@Override
	public String toString(){
		return
				"product{" +
						"id = '" + id + '\'' +
						",name = '" + name + '\'' +
						",description = '" + description + '\'' +
						",ingredients = '" + ingredients + '\'' +
						",owner_id = '" + owner_id + '\'' +
						",rating = '" + rating + '\'' +
						",making_time = '" + making_time + '\'' +
						",price = '" + price + '\'' +
						",sale_price = '" + sale_price + '\'' +
						",commission = '" + commission + '\'' +
						",is_veg = '" + is_veg + '\'' +
						",specifications = '" + specifications + '\'' +
						",delivery_options = '" + delivery_options + '\'' +
						",price = '" + image + '\'' +
						",sale_price = '" + image1 + '\'' +
						",commission = '" + image2 + '\'' +
						",is_veg = '" + image3 + '\'' +
						",specifications = '" + image4 + '\'' +
						",delivery_options = '" + delivery_options + '\'' +
						",discount = '" + discount + '\'' +
						",calorie = '" + calorie + '\'' +
						",protein = '" + protein + '\'' +
						",carbs = '" + carbs + '\'' +
						",fat = '" + fat + '\'' +
						",fiber = '" + fiber + '\'' +
						",created_at = '" + created_at + '\'' +
						",user = '" + user + '\'' +
						"}";
	}

	protected ProductBean(Parcel in) {
		id = in.readByte() == 0x00 ? null : in.readInt();
		name = in.readString();
		description = in.readString();
		ingredients = in.readString();
		owner_id = in.readString();
		rating = in.readString();
		making_time = in.readString();
		price = in.readString();
		sale_price = in.readString();
		commission = in.readString();
		is_veg = in.readString();
		specifications = in.readString();
		delivery_options = in.readString();
		image = in.readString();
		image1 = in.readString();
		image2 = in.readString();
		image3 = in.readString();
		image4 = in.readString();
		discount = in.readString();
		calorie = in.readString();
		carbs = in.readString();
		protein = in.readString();
		fiber = in.readString();
		fat = in.readString();
		created_at = in.readString();
		user = (AboutMe) in.readValue(AboutMe.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (id == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeInt(id);
		}
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(ingredients);
		dest.writeString(owner_id);
		dest.writeString(rating);
		dest.writeString(making_time);
		dest.writeString(price);
		dest.writeString(sale_price);
		dest.writeString(commission);
		dest.writeString(is_veg);
		dest.writeString(specifications);
		dest.writeString(delivery_options);
		dest.writeString(image);
		dest.writeString(image1);
		dest.writeString(image2);
		dest.writeString(image3);
		dest.writeString(image4);
		dest.writeString(discount);
		dest.writeString(calorie);
		dest.writeString(carbs);
		dest.writeString(fat);
		dest.writeString(fiber);
		dest.writeString(protein);
		dest.writeString(created_at);
		dest.writeValue(user);
	}

	@SuppressWarnings("unused")
	public static final Creator<ProductBean> CREATOR = new Creator<ProductBean>() {
		@Override
		public ProductBean createFromParcel(Parcel in) {
			return new ProductBean(in);
		}

		@Override
		public ProductBean[] newArray(int size) {
			return new ProductBean[size];
		}
	};

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public boolean isIs_favourite() {
		return is_favourite;
	}

	public void setIs_favourite(boolean is_favourite) {
		this.is_favourite = is_favourite;
	}
}