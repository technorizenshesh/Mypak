package com.mypakuser.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelOrderHistory implements Serializable {

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

    public class Result implements Serializable
    {
        private String id;

        private String user_id;

        private String driver_id;

        private String pickup_location;

        private String pickup_lat;

        private String pickup_lon;

        private String drop_location;

        private String drop_lat;

        private String drop_lon;

        private String status;

        private String date_time;

        private String parcel_date;

        private String title;

        private String description;

        private String parcel_quantity;

        private String parcel_category;

        private String item_detail;

        private String vehicle_id;

        private String dev_type;

        private String parcel_time;

        private String parcel_image;

        private String payment_type;

        private String total_price;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setUser_id(String user_id){
            this.user_id = user_id;
        }
        public String getUser_id(){
            return this.user_id;
        }
        public void setDriver_id(String driver_id){
            this.driver_id = driver_id;
        }
        public String getDriver_id(){
            return this.driver_id;
        }
        public void setPickup_location(String pickup_location){
            this.pickup_location = pickup_location;
        }
        public String getPickup_location(){
            return this.pickup_location;
        }
        public void setPickup_lat(String pickup_lat){
            this.pickup_lat = pickup_lat;
        }
        public String getPickup_lat(){
            return this.pickup_lat;
        }
        public void setPickup_lon(String pickup_lon){
            this.pickup_lon = pickup_lon;
        }
        public String getPickup_lon(){
            return this.pickup_lon;
        }
        public void setDrop_location(String drop_location){
            this.drop_location = drop_location;
        }
        public String getDrop_location(){
            return this.drop_location;
        }
        public void setDrop_lat(String drop_lat){
            this.drop_lat = drop_lat;
        }
        public String getDrop_lat(){
            return this.drop_lat;
        }
        public void setDrop_lon(String drop_lon){
            this.drop_lon = drop_lon;
        }
        public String getDrop_lon(){
            return this.drop_lon;
        }
        public void setStatus(String status){
            this.status = status;
        }
        public String getStatus(){
            return this.status;
        }
        public void setDate_time(String date_time){
            this.date_time = date_time;
        }
        public String getDate_time(){
            return this.date_time;
        }
        public void setParcel_date(String parcel_date){
            this.parcel_date = parcel_date;
        }
        public String getParcel_date(){
            return this.parcel_date;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setDescription(String description){
            this.description = description;
        }
        public String getDescription(){
            return this.description;
        }
        public void setParcel_quantity(String parcel_quantity){
            this.parcel_quantity = parcel_quantity;
        }
        public String getParcel_quantity(){
            return this.parcel_quantity;
        }
        public void setParcel_category(String parcel_category){
            this.parcel_category = parcel_category;
        }
        public String getParcel_category(){
            return this.parcel_category;
        }
        public void setItem_detail(String item_detail){
            this.item_detail = item_detail;
        }
        public String getItem_detail(){
            return this.item_detail;
        }
        public void setVehicle_id(String vehicle_id){
            this.vehicle_id = vehicle_id;
        }
        public String getVehicle_id(){
            return this.vehicle_id;
        }
        public void setDev_type(String dev_type){
            this.dev_type = dev_type;
        }
        public String getDev_type(){
            return this.dev_type;
        }
        public void setParcel_time(String parcel_time){
            this.parcel_time = parcel_time;
        }
        public String getParcel_time(){
            return this.parcel_time;
        }
        public void setParcel_image(String parcel_image){
            this.parcel_image = parcel_image;
        }
        public String getParcel_image(){
            return this.parcel_image;
        }
        public void setPayment_type(String payment_type){
            this.payment_type = payment_type;
        }
        public String getPayment_type(){
            return this.payment_type;
        }
        public void setTotal_price(String total_price){
            this.total_price = total_price;
        }
        public String getTotal_price(){
            return this.total_price;
        }
    }

}
