 package com.mypakuser.utils;

 import java.util.Map;

 import okhttp3.ResponseBody;
 import retrofit2.Call;
 import retrofit2.http.FieldMap;
 import retrofit2.http.FormUrlEncoded;
 import retrofit2.http.POST;

 public interface Api {

     @FormUrlEncoded
     @POST("forgot_password")
     Call<ResponseBody> forgotPass(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("social_login")
     Call<ResponseBody> socialLogin(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("signup")
     Call<ResponseBody> signUpApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("request_subadmin")
     Call<ResponseBody> requestSubadmin(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("shipping_request")
     Call<ResponseBody> addShippingRequestApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_shipping_request")
     Call<ResponseBody> getAllShippingRequestApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("add_feedback")
     Call<ResponseBody> addFeedbackByUserApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_my_shipping")
     Call<ResponseBody> getMySendingsApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_shipping_details")
     Call<ResponseBody> getShipDetailApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_restaurant_sub_category")
     Call<ResponseBody> getStoreByCatApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_restaurant_child_category")
     Call<ResponseBody> getStoreMealsApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_cart")
     Call<ResponseBody> getStoreCartApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_cart_entertentment")
     Call<ResponseBody> getCartEntApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_count_cart_entertentment")
     Call<ResponseBody> getCartCountEntApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("delete_cart")
     Call<ResponseBody> deleteStoreItemApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("delete_cart_entertentment")
     Call<ResponseBody> deleteEntItemApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_restaurant_details")
     Call<ResponseBody> getStoreDetailApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("find_specialist_category")
     Call<ResponseBody> searchStoreApiCall(@FieldMap Map<String, String> params);

     @POST("get_specialist_category")
     Call<ResponseBody> getSpecialCatApiCall();

     @FormUrlEncoded
     @POST("get_order_history")
     Call<ResponseBody> getMyBookingApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_restaurant_child_category")
     Call<ResponseBody> getStoreItemsApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_count_cart")
     Call<ResponseBody> getCartCountApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("accept_and_cancel_user")
     Call<ResponseBody> acceptCancelUserApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_bid")
     Call<ResponseBody> getBidApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("find_specialist_category")
     Call<ResponseBody> findSpecialCatApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("add_rating")
     Call<ResponseBody> addFeedbackApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("place_order")
     Call<ResponseBody> bookingStoreApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("strips_payment")
     Call<ResponseBody> paymentStoreApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_account_details")
     Call<ResponseBody> getCardApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("add_account_details")
     Call<ResponseBody> addCardApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_entertentment_details")
     Call<ResponseBody> addEntDetailsCall(@FieldMap Map<String, String> params);

     @POST("get_restaurant_category")
     Call<ResponseBody> getStoreCatApiCall();

     @POST("get_upcoming_movie")
     Call<ResponseBody> getUpMoviesApiCall();

     @POST("get_movie")
     Call<ResponseBody> getRemMoviesApiCall();

     @POST("get_entertentment_category")
     Call<ResponseBody> getEntCatApiCall();

     @POST("get_restaurant_item_meals")
     Call<ResponseBody> getStoreMealsCatApiCall();

     @POST("car_list")
     Call<ResponseBody> getAllCars();

     @FormUrlEncoded
     @POST("login")
     Call<ResponseBody> loginApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("change_password")
     Call<ResponseBody> changePassCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_shipping_history")
     Call<ResponseBody> getOrderHistoryCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_nearest_driver")
     Call<ResponseBody> getNearByDriversCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_treater_details")
     Call<ResponseBody> getTheaterDetailsApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_treater")
     Call<ResponseBody> getTheaterApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_entertentment")
     Call<ResponseBody> getEntCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_entertentment_details")
     Call<ResponseBody> getEntDetailsCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_movie_details")
     Call<ResponseBody> getMovieDetailsCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_ticket")
     Call<ResponseBody> getEntTicketCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("add_to_cart")
     Call<ResponseBody> addToCartApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("add_to_cart_entertentment")
     Call<ResponseBody> addToCartEntApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("remove_cart_entertentment")
     Call<ResponseBody> removeEntCartApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("cancel_by_user")
     Call<ResponseBody> cancelTripByUser(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("insert_chat_booking")
     Call<ResponseBody> insertChatBookingCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_chat_booking")
     Call<ResponseBody> getChatBookingCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_driver_latlon")
     Call<ResponseBody> getDriverLocation(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_booking_details")
     Call<ResponseBody>  bookingDetails(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_chat")
     Call<ResponseBody> getAllMessagesCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("insert_chat")
     Call<ResponseBody> insertChatApiCall(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_conversation")
     Call<ResponseBody> getConversation(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("booking_request")
     Call<ResponseBody> taxiBookingRequest(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_current_booking_user")
     Call<ResponseBody> getPendingBooking(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("payment")
     Call<ResponseBody> doTaxiPayment(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_running_booking_user")
     Call<ResponseBody> getActiveBooking(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("get_booking_history")
     Call<ResponseBody> getFinishedBooking(@FieldMap Map<String, String> params);

     @FormUrlEncoded
     @POST("car_type_list")
     Call<ResponseBody> getTypeList(@FieldMap Map<String, String> params);

     @POST("car_list")
     Call<ResponseBody> getCarTypesApi();

     @POST("get_access_token")
     Call<ResponseBody> getClientTokenApi();

 }
