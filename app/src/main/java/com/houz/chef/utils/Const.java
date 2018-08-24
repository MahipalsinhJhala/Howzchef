package com.houz.chef.utils;

public class Const {
	
	
	// web services
	public class ServiceType {

		private static final String HOST_URL = "https://app.houzchef.com/public/";


		private static final String BASE_URL = HOST_URL + "api/";
		public static final String LOGIN = BASE_URL + "login";
		public static final String REGISTER = BASE_URL + "register";

	}

	// prefname
	public static String PREF_NAME = "houzchef";


	// service codes
	public class ServiceCode {
		public static final int REGISTER = 1;
		public static final int LOGIN = 2;
	}

	// service parameters
	public class Params {

		public static final String PICTURE = "picture";
		public static final String EMAIL = "email";
		public static final String PASSWORD = "password";
		public static final String FIRSTNAME = "first_name";
		public static final String LASTNAME = "last_name";
		public static final String ISCHEF = "is_chef";
		public static final String DEVICETYPE = "device_type";
		public static final String TOKEN = "fcm_token";
		public static final String LOGINBY = "login_by";
		public static final String PHONE = "phone";

	}
	public static final int CHOOSE_PHOTO = 112;
	public static final int TAKE_PHOTO = 113;
	public static final int ADDCONTACT = 114;
	public static final int CHOOSE_FILE = 115;
	public static final int ADD_ADDRESS=116;
	public static final int UPDATE_ADDRESS=117;
	public static int GPS_ENABLE = 118;
	public static int CART_DATA_UPDATE=119;

	// Order status........
	public static final int ORDER_PLACED=0;
	public static final int ORDER_PROCESSING=1;
	public static final int ORDER_DELIVERY_OUT=2;
	public static final int ORDER_DELIVERED=3;
	public static final int ORDER_CANCELLED=4;


	public static String PROMO_TYPE_FIXED="0";
	public static String PROMO_TYPE_DISCOUNT="1";
	public static final String URL = "url";
	public static final String DEVICE_TYPE_ANDROID = "android";
	public static final String SOCIAL_FACEBOOK = "facebook";
	public static final String MANUAL = "manual";
	public static final String ADDRESS_COUNT="address_count";
	public static final String IS_UPDATE="is_update";
	public static final String EXTRA_OBJ="extra_obj";
	public static final String REMOVED_ITEM_IDS="removed_item_ids";
	public static final String POSITION="position";
	public static final String TOTAL="total";
	public static final String DELIVERY_ADDRESS="delivery_address";
	public static final String PROMO_CODE="promo_code";
	public static final String PROMO_DISCOUNT="promo_discount";

}
