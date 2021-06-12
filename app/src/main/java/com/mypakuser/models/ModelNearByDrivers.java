package com.mypakuser.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelNearByDrivers implements Serializable {

    private ArrayList<Result> result;
    private String message;
    private String status;

    public void setResult(ArrayList<Result> result){
        this.result = result;
    }
    public ArrayList<Result> getResult(){
        return this.result;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }

    public class Result
    {
        private String id;

        private String user_name;

        private String mobile;

        private String email;

        private String password;

        private String type;

        private String image;

        private String otp;

        private String status;

        private String lat;

        private String lon;

        private String address;

        private String social_id;

        private String date_time;

        private String ios_register_id;

        private String register_id;

        private String licence_number;

        private String cust_id;

        private String stripe_account_id;

        private String distance;

        private int estimate_time;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setUser_name(String user_name){
            this.user_name = user_name;
        }
        public String getUser_name(){
            return this.user_name;
        }
        public void setMobile(String mobile){
            this.mobile = mobile;
        }
        public String getMobile(){
            return this.mobile;
        }
        public void setEmail(String email){
            this.email = email;
        }
        public String getEmail(){
            return this.email;
        }
        public void setPassword(String password){
            this.password = password;
        }
        public String getPassword(){
            return this.password;
        }
        public void setType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }
        public void setImage(String image){
            this.image = image;
        }
        public String getImage(){
            return this.image;
        }
        public void setOtp(String otp){
            this.otp = otp;
        }
        public String getOtp(){
            return this.otp;
        }
        public void setStatus(String status){
            this.status = status;
        }
        public String getStatus(){
            return this.status;
        }
        public void setLat(String lat){
            this.lat = lat;
        }
        public String getLat(){
            return this.lat;
        }
        public void setLon(String lon){
            this.lon = lon;
        }
        public String getLon(){
            return this.lon;
        }
        public void setAddress(String address){
            this.address = address;
        }
        public String getAddress(){
            return this.address;
        }
        public void setSocial_id(String social_id){
            this.social_id = social_id;
        }
        public String getSocial_id(){
            return this.social_id;
        }
        public void setDate_time(String date_time){
            this.date_time = date_time;
        }
        public String getDate_time(){
            return this.date_time;
        }
        public void setIos_register_id(String ios_register_id){
            this.ios_register_id = ios_register_id;
        }
        public String getIos_register_id(){
            return this.ios_register_id;
        }
        public void setRegister_id(String register_id){
            this.register_id = register_id;
        }
        public String getRegister_id(){
            return this.register_id;
        }
        public void setLicence_number(String licence_number){
            this.licence_number = licence_number;
        }
        public String getLicence_number(){
            return this.licence_number;
        }
        public void setCust_id(String cust_id){
            this.cust_id = cust_id;
        }
        public String getCust_id(){
            return this.cust_id;
        }
        public void setStripe_account_id(String stripe_account_id){
            this.stripe_account_id = stripe_account_id;
        }
        public String getStripe_account_id(){
            return this.stripe_account_id;
        }
        public void setDistance(String distance){
            this.distance = distance;
        }
        public String getDistance(){
            return this.distance;
        }
        public void setEstimate_time(int estimate_time){
            this.estimate_time = estimate_time;
        }
        public int getEstimate_time(){
            return this.estimate_time;
        }
    }

}
