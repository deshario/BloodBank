package com.deshario.bloodbank.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SavedBranchRequest {

    private int user_id;
    private String user_name;
    private String blood_name;
    private int blood_amount;
    private int paid_amount;
    private int branchreq_id;
    private String branch_name;
    private String branch_code;
    private String branch_lat_long;
    private String branch_address;
    private String saved_date;
    private String req_key;
    private int req_status;

    public SavedBranchRequest() {
    }

    public SavedBranchRequest(JSONObject object) {
        try {
            this.user_id = object.getInt("user_id");
            this.user_name = object.getString("user_name");
            this.blood_name = object.getString("blood_name");
            this.blood_amount = object.getInt("blood_amount");
            this.paid_amount = object.getInt("paid_amount");
            this.branchreq_id = object.getInt("branchreq_id");
            this.branch_name = object.getString("branch_name");
            this.branch_code = object.getString("branch_code");
            this.branch_lat_long = object.getString("branch_lat_long");
            this.branch_address = object.getString("branch_address");
            this.saved_date = object.getString("saved_date");
            this.req_key = object.getString("req_key");
            this.req_status = object.getInt("req_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<SavedBranchRequest> setDatafromJson(JSONArray jsonObjects){
        ArrayList<SavedBranchRequest> savedBranchRequestArrayList = new ArrayList<SavedBranchRequest>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                savedBranchRequestArrayList.add(new SavedBranchRequest(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return savedBranchRequestArrayList;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public int getBranchreq_id() {
        return branchreq_id;
    }

    public void setBranchreq_id(int branchreq_id) {
        this.branchreq_id = branchreq_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getBranch_lat_long() {
        return branch_lat_long;
    }

    public void setBranch_lat_long(String branch_lat_long) {
        this.branch_lat_long = branch_lat_long;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
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
