package com.deshario.bloodbank.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SavedBloodRequest {

    private int donor_id;
    private String donor_name;
    private int requester_id;
    private String requester_name;
    private int bloodreq_id;
    private String blood_name;
    private int blood_amount;
    private int paid_amount;
    private String location_name;
    private String lat_long;
    private String full_address;
    private String saved_date;
    private String req_key;
    private int req_status;

    public SavedBloodRequest() {
    }

    public SavedBloodRequest(JSONObject object) {
        try {
            this.donor_id = object.getInt("donor_id");
            this.donor_name = object.getString("donor_name");
            this.requester_id = object.getInt("requester_id");
            this.requester_name = object.getString("requester_name");
            this.bloodreq_id = object.getInt("bloodreq_id");
            this.blood_name = object.getString("blood_name");
            this.blood_amount = object.getInt("blood_amount");
            this.paid_amount = object.getInt("paid_amount");
            this.location_name = object.getString("location_name");
            this.lat_long = object.getString("lat_long");
            this.full_address = object.getString("full_address");
            this.saved_date = object.getString("saved_date");
            this.req_key = object.getString("req_key");
            this.req_status = object.getInt("req_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<SavedBloodRequest> setDatafromJson(JSONArray jsonObjects){
        ArrayList<SavedBloodRequest> savedBloodRequests = new ArrayList<SavedBloodRequest>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                savedBloodRequests.add(new SavedBloodRequest(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return savedBloodRequests;
    }

    public int getDonor_id() {
        return donor_id;
    }

    public void setDonor_id(int donor_id) {
        this.donor_id = donor_id;
    }

    public String getDonor_name() {
        return donor_name;
    }

    public void setDonor_name(String donor_name) {
        this.donor_name = donor_name;
    }

    public int getRequester_id() {
        return requester_id;
    }

    public void setRequester_id(int requester_id) {
        this.requester_id = requester_id;
    }

    public String getRequester_name() {
        return requester_name;
    }

    public void setRequester_name(String requester_name) {
        this.requester_name = requester_name;
    }

    public int getBloodreq_id() {
        return bloodreq_id;
    }

    public void setBloodreq_id(int bloodreq_id) {
        this.bloodreq_id = bloodreq_id;
    }

    public String getBlood_name() {
        return blood_name;
    }

    public void setBlood_name(String blood_name) {
        this.blood_name = blood_name;
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

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLat_long() {
        return lat_long;
    }

    public void setLat_long(String lat_long) {
        this.lat_long = lat_long;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getSaved_date() {
        return saved_date;
    }

    public void setSaved_date(String saved_date) {
        this.saved_date = saved_date;
    }

    public String getReq_key() {
        return req_key;
    }

    public void setReq_key(String req_key) {
        this.req_key = req_key;
    }

    public int getReq_status() {
        return req_status;
    }

    public void setReq_status(int req_status) {
        this.req_status = req_status;
    }

}
