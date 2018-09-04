package com.houz.chef.modelBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AboutMe implements Parcelable {
	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("phone")
	@Expose
	private String phone;
	@SerializedName("first_name")
	@Expose
	private String first_name;
	@SerializedName("last_name")
	@Expose
	private String last_name;
	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("is_chef")
	@Expose
	private String is_chef;
	@SerializedName("profile")
	@Expose
	private String profile;
	@SerializedName("verify_code")
	@Expose
	private String verify_code;
	@SerializedName("is_verified")
	@Expose
	private String is_verified;
	@SerializedName("login_by")
	@Expose
	private String login_by;
	@SerializedName("social_id")
	@Expose
	private String social_id;
	@SerializedName("fcm_token")
	@Expose
	private String fcm_token;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIs_chef() {
		return is_chef;
	}

	public void setIs_chef(String is_chef) {
		this.is_chef = is_chef;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}

	public String getIs_verified() {
		return is_verified;
	}

	public void setIs_verified(String is_verified) {
		this.is_verified = is_verified;
	}

	public String getLogin_by() {
		return login_by;
	}

	public void setLogin_by(String login_by) {
		this.login_by = login_by;
	}

	public String getSocial_id() {
		return social_id;
	}

	public void setSocial_id(String social_id) {
		this.social_id = social_id;
	}

	public String getFcm_token() {
		return fcm_token;
	}

	public void setFcm_token(String fcm_token) {
		this.fcm_token = fcm_token;
	}

	public AboutMe() {
	}

	@Override
	public String toString(){
		return
				"AboutMe{" +
						"id = '" + id + '\'' +
						",name = '" + name + '\'' +
						",phone = '" + phone + '\'' +
						",first_name = '" + first_name + '\'' +
						",last_name = '" + last_name + '\'' +
						",email = '" + email + '\'' +
						",is_chef = '" + is_chef + '\'' +
						",profile = '" + profile + '\'' +
						",verify_code = '" + verify_code + '\'' +
						",is_verified = '" + is_verified + '\'' +
						",login_by = '" + login_by + '\'' +
						",social_id = '" + social_id + '\'' +
						",fcm_token = '" + fcm_token + '\'' +
						"}";
	}

	protected AboutMe(Parcel in) {
		id = in.readByte() == 0x00 ? null : in.readInt();
		name = in.readString();
		phone = in.readString();
		first_name = in.readString();
		last_name = in.readString();
		email = in.readString();
		is_chef = in.readString();
		profile = in.readString();
		verify_code = in.readString();
		is_verified = in.readString();
		login_by = in.readString();
		social_id = in.readString();
		fcm_token = in.readString();
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
		dest.writeString(phone);
		dest.writeString(first_name);
		dest.writeString(last_name);
		dest.writeString(email);
		dest.writeString(is_chef);
		dest.writeString(profile);
		dest.writeString(verify_code);
		dest.writeString(is_verified);
		dest.writeString(login_by);
		dest.writeString(social_id);
		dest.writeString(fcm_token);
	}

	@SuppressWarnings("unused")
	public static final Creator<AboutMe> CREATOR = new Creator<AboutMe>() {
		@Override
		public AboutMe createFromParcel(Parcel in) {
			return new AboutMe(in);
		}

		@Override
		public AboutMe[] newArray(int size) {
			return new AboutMe[size];
		}
	};

	public boolean isIs_favourite() {
		return is_favourite;
	}

	public void setIs_favourite(boolean is_favourite) {
		this.is_favourite = is_favourite;
	}
}