package com.mypakuser.utils;

//                paramHash = new HashMap<>();
//                paramHash.put("user_id",modelLogin.getResult().getId());
//                paramHash.put("pickup_location", dialogBinding.etPickAdd.getText().toString().trim());
//                paramHash.put("pickup_lat",String.valueOf(pickUpLatLon.latitude));
//                paramHash.put("pickup_lon",String.valueOf(pickUpLatLon.longitude));
//                paramHash.put("drop_location",dialogBinding.etDropAdd.getText().toString().trim());
//                paramHash.put("drop_lat",String.valueOf(dropOffLatLon.latitude));
//                paramHash.put("drop_lon",String.valueOf(pickUpLatLon.longitude));
//                paramHash.put("pickup_date",dialogBinding.etPickDate.getText().toString().trim());
//                paramHash.put("dropoff_date",dialogBinding.etDropDate.getText().toString().trim());
//                paramHash.put("recipient_name",dialogBinding.etRecipName.getText().toString().trim());
//                paramHash.put("mobile_no",dialogBinding.etMobile.getText().toString().trim());
//                paramHash.put("parcel_quantity",dialogBinding.spQuantity.getSelectedItem().toString());
//                paramHash.put("parcel_category",dialogBinding.spItemCat.getSelectedItem().toString());
//                paramHash.put("item_detail",dialogBinding.etItemDetail.getText().toString().trim());
//                paramHash.put("dev_instruction",dialogBinding.etDevInstruction.getText().toString().trim());
//                paramHash.put("direction_json","");

public interface AppConstant {

    String BASE_URL = "https://equipmeapp.co.nz/Mypak/webservice/";
    String IMAGE_URL = "https://www.pickpock.net/uploads/images/";

    String LOGIN_API = "login";
    String INSTANT = "instant";
    String SCHEDULE = "schedule";
    String DEV_TYPE = "devtype";
    String STORE_BOOKING_PARAMS = "bookingparam";
    String SIGNUP_API = "signup";
    String EURO = "€";
    String EXCLUSIVE = "exclusive";
    String NORMAL = "normal";
    String CLASSIC = "classic";
    String UPDATE_PROFILE_API = "update_profile";
    String FORGOT_PASSWORD_API = "forgot_password";
    String CHANGE_PASSWORD_API = "change_password";

    String IS_FILTER = "is_filter";
    String IS_SEARCH = "is_search";
    String SEARCH_DATA = "search_data";
    String DOLLAR = "$";
    String FILTER_SELECTED_DATA = "filter_selected_item";

    String IS_REGISTER = "user_register";
    String USER_DETAILS = "user_details";
    String USER = "USER";
    String LAT_LON_LIST = "latlonlist";
    String PROVIDER = "PROVIDER";
    String UPDATE_ORDER_STATUS = "update_order_status";

}



