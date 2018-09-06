package com.houz.chef.apiBase;

import android.support.annotation.HalfFloat;

import com.houz.chef.modelBean.AddAddressResponse;
import com.houz.chef.modelBean.CheckPromoBean;
import com.houz.chef.modelBean.GetAddressList;
import com.houz.chef.modelBean.GetCart;
import com.houz.chef.modelBean.GetChefDetails;
import com.houz.chef.modelBean.GetFavouriteChefList;
import com.houz.chef.modelBean.GetProductBean;
import com.houz.chef.modelBean.GetProductDetailsBean;
import com.houz.chef.modelBean.LoginBean;
import com.houz.chef.modelBean.ModelBean;
import com.houz.chef.modelBean.NotificationModel;
import com.houz.chef.modelBean.OrderData;
import com.houz.chef.modelBean.PaymentBean;
import com.houz.chef.modelBean.ProductBean;
import com.houz.chef.modelBean.ProfileBean;
import com.houz.chef.modelBean.ProfileResponse;
import com.houz.chef.modelBean.QuantityUpdateResponse;
import com.houz.chef.modelBean.ReferralCodeResponse;
import com.houz.chef.modelBean.SetFavouriteProduct;
import com.houz.chef.modelBean.UpdateSettingResponse;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface FetchServiceInterFace {
    //Auth
    @Multipart
    @POST("register")
    Observable<LoginBean> customeSignUp(@PartMap Map<String, RequestBody> params); //done

    @Multipart
    @POST("verifyaccount")
    Observable<LoginBean> verified(@PartMap Map<String, RequestBody> params); //done

    @Multipart
    @POST("login")
    Observable<LoginBean> customeSignIn(@PartMap Map<String, RequestBody> params); //done

    @POST("products/all")
    Observable<GetProductBean<ArrayList<ProductBean>>> getMenu(); //done

    @GET("products/all")
    Observable<GetProductBean<ArrayList<ProductBean>>> getMenu(@Query("page") int page, @Query("search") String search,@Query("type") String type,@Query("is_veg") String isVeg,@Query("sorting") String sorting); //done

    @GET("getmynotifications/{id}")
    Observable<GetProductBean<ArrayList<NotificationModel>>> getNotifications(@Path("id") int id,@Query("page") int page);

    @GET("product/{userId}/{productId}")
    Observable<GetProductDetailsBean> getProductDetail(@Path("userId") int userId, @Path("productId") int productId); //done

    @POST("chef/{userId}/{chefId}")
    Observable<GetChefDetails> getChefDetail(@Path("userId") int userId, @Path("chefId") int chefId); //done

    @Multipart
    @POST("products/addtofavourite")
    Observable<SetFavouriteProduct> setFavouriteProduct(@PartMap Map<String, RequestBody> params); //done

    @Multipart
    @POST("chef/addtofavourite")
    Observable<SetFavouriteProduct> setFavouriteChef(@PartMap Map<String, RequestBody> params); //done

    @GET("getfavouriteproducts/{userID}")
    Observable<GetProductBean<ArrayList<ProductBean>>> getFavouriteProduct(@Path("userID") int userID); //done

    @GET("getfavouritechefs/{userID}")
    Observable<GetFavouriteChefList> getFavouriteChef(@Path("userID") int userID); //done

    @Multipart
    @POST("cart/mycart")
    Observable<GetCart> getCart(@PartMap Map<String, RequestBody> params); //done

    @Multipart
    @POST("cart/addtocart")
    Observable<ModelBean> addToCart(@PartMap Map<String, RequestBody> params); //done

    @GET("user/get_address_details/{userID}")
    Observable<GetAddressList> getAddressList(@Path("userID") int userID);

    @Multipart
    @POST("user/add_address/{userID}")
    Observable<AddAddressResponse> addAddress(@Path("userID") int userID, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("update_settings/{userID}")
    Observable<UpdateSettingResponse> updateSettings(@Path("userID") int userID, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("user/update_address/{userID}/{address_id}")
    Observable<AddAddressResponse> updateAddress(@Path("userID") int userID, @Path("address_id") int address_id, @PartMap Map<String, RequestBody> params);

    @POST("user/delete_address/{userID}/{address_id}")
    Observable<ModelBean> deleteAddress(@Path("userID") int userID, @Path("address_id") int address_id);

    @POST("cart/mycart/remove/{uid}/{id}")
    Observable<ModelBean> removeCartItem(@Path("uid") int uid,@Path("id") int id);

    @Multipart
    @POST("promocodes/check_promo")
    Observable<CheckPromoBean> checkPromo(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("cart/update_quantity")
    Observable<QuantityUpdateResponse> updateQuantity(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("cart/stripe_payment")
    Observable<PaymentBean> stripePayment(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("orders/getcompletedorders/{id}")
    Observable<OrderData> getCompletedOrders(@Path("id") int id, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("orders/getorders/{id}")
    Observable<OrderData> getOrders(@Path("id") int id, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("change_password/{id}")
    Observable<LoginBean> callChangePassword(@Path("id") int id,@PartMap Map<String, RequestBody> params); //done

    @POST("getprofile/{id}")
    Observable<ProfileBean> callFetchProfile(@Path("id") int id); //done

    @Multipart
    @POST("update_profile/{id}")
    Observable<ProfileResponse> callUpdateProfile(@Path("id") int id, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part image);

    @POST("getreferralcode/{id}")
    Observable<ReferralCodeResponse> callGetReferralCode(@Path("id") int id);

}
