package com.deshario.bloodbank.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDonation {
    public String donor;
    public int donor_id;
    public String receiver;
    public int receiver_id;
    public String blood_group;
    public int blood_amount;
    public int paid_amount;
    public String lat_long;
    public String location_name;
    public String full_address;
    public String reason;
    public String postal_code;
    public String request_key;
    public String verified_by;
    public String donated_date;
    public int donation_status;

    public UserDonation(){}

    public UserDonation(JSONObject object) {
        try {
            this.donor = object.getString("donor");
            this.donor_id = object.getInt("donor_id");
            this.receiver = object.getString("receiver");
            this.receiver_id = object.getInt("receiver_id");
            this.blood_group = object.getString("blood_group");
            this.blood_amount = object.getInt("blood_amount");
            this.paid_amount = object.getInt("paid_amount");
            this.lat_long = object.getString("lat_long");
            this.location_name = object.getString("location_name");
            this.full_address = object.getString("full_address");
            this.reason = object.getString("reason");
            this.postal_code = object.getString("postal_code");
            this.request_key = object.getString("request_key");
            this.verified_by = object.getString("verified_by");
            this.donated_date = object.getString("donated_date");
            this.donation_status = object.getInt("donation_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<UserDonation> setDatafromJson(JSONArray jsonObjects){
        ArrayList<UserDonation> user_donations = new ArrayList<UserDonation>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                user_donations.add(new UserDonation(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return user_donations;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public int getDonor_id() {
        return donor_id;
    }

    public void setDonor_id(int donor_id) {
        this.donor_id = donor_id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public int getBlood_amount() {
        return blood_amount;
    }

    public void setBlood_amount(int blood_amount) {
        this.blood_amount = blood_amount;
    }

    public int getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(int paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getLat_long() {
        return lat_long;
    }

    public void setLat_long(String lat_long) {
        this.lat_long = lat_long;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getRequest_key() {
        return request_key;
    }

    public void setRequest_key(String request_key) {
        this.request_key = request_key;
    }

    public String getVerified_by() {
        return verified_by;
    }

    public void setVerified_by(String verified_by) {
        this.verified_by = verified_by;
    }

    public String getDonated_date() {
        return donated_date;
    }

    public void setDonated_date(String donated_date) {
        this.donated_date = donated_date;
    }

    public int getDonation_status() {
        return donation_status;
    }

    public void setDonation_status(int donation_status) {
        this.donation_status = donation_status;
    }
}
