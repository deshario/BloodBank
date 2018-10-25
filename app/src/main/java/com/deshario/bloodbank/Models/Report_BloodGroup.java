package com.deshario.bloodbank.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Report_BloodGroup {
    private int blood_id;
    private String blood_name;
    private float blood_amount;
    private float paid_amount;
    private float requested_times;

    public Report_BloodGroup(){}

    public Report_BloodGroup(JSONObject object) {
        try {
            this.blood_id = object.getInt("blood_id");
            this.blood_name = object.getString("blood_name");
            this.blood_amount = BigDecimal.valueOf(object.getDouble("blood_amount")).floatValue();
            this.paid_amount = BigDecimal.valueOf(object.getDouble("paid_amount")).floatValue();
            this.requested_times = object.getInt("requested_times");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Report_BloodGroup> setDatafromJson(JSONArray jsonObjects){
        ArrayList<Report_BloodGroup> bloodGroups = new ArrayList<Report_BloodGroup>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                bloodGroups.add(new Report_BloodGroup(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bloodGroups;
    }

    public int getBlood_id() {
        return blood_id;
    }

    public void setBlood_id(int blood_id) {
        this.blood_id = blood_id;
    }

    public String getBlood_name() {
        return blood_name;
    }

    public void setBlood_name(String blood_name) {
        this.blood_name = blood_name;
    }

    public float getBlood_amount() {
        return blood_amount;
    }

    public void setBlood_amount(float blood_amount) {
        this.blood_amount = blood_amount;
    }

    public float getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(float paid_amount) {
        this.paid_amount = paid_amount;
    }

    public float getRequested_times() {
        return requested_times;
    }

    public void setRequested_times(float requested_times) {
        this.requested_times = requested_times;
    }
}
