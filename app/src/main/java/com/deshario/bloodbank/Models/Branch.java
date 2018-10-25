package com.deshario.bloodbank.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Branch {
    private int branch_id;
    private String branch_name;
    private String branch_latlong;
    private String branch_address;
    private String branch_code;
    private String branch_created;

    public Branch() {
    }

    public Branch(JSONObject object) {
        try {
            this.branch_id = object.getInt("branch_id");
            this.branch_name = object.getString("branch_name");
            this.branch_latlong = object.getString("branch_lat_long");
            this.branch_address = object.getString("branch_address");
            this.branch_code = object.getString("branch_code");
            this.branch_created = object.getString("branch_created");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Branch> setDatafromJson(JSONArray jsonObjects){
        ArrayList<Branch> branches = new ArrayList<Branch>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                branches.add(new Branch(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return branches;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_latlong() {
        return branch_latlong;
    }

    public void setBranch_latlong(String branch_latlong) {
        this.branch_latlong = branch_latlong;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getBranch_created() {
        return branch_created;
    }

    public void setBranch_created(String branch_created) {
        this.branch_created = branch_created;
    }

}
