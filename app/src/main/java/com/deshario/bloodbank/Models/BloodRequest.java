package com.deshario.bloodbank.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BloodRequest implements Parcelable{
    private int id;
    private int requester_id;
    private String requester_name;
    private String blood_group;
    private int blood_amount;
    private int paid_amount;
    private String lat_long;
    private String location_name;
    private String full_address;
    private String reason;
    private String postal_code;
    private String created;
    private String req_key;
    private int num_donors;
    private int status;

    public BloodRequest(){}

    public BloodRequest(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.requester_id = object.getInt("requester_id");
            this.requester_name = object.getString("requester_name");
            this.blood_group = object.getString("blood_group");
            this.blood_amount = object.getInt("blood_amount");;
            this.paid_amount = object.getInt("paid_amount");
            this.lat_long = object.getString("lat_long");
            this.location_name = object.getString("location_name");
            this.full_address = object.getString("full_address");
            this.reason = object.getString("reason");
            this.postal_code = object.getString("postal_code");
            this.created = object.getString("created");
            this.req_key = object.getString("req_key");
            this.num_donors = object.getInt("num_donors");
            this.status = object.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<BloodRequest> setDatafromJson(JSONArray jsonObjects){
        ArrayList<BloodRequest> bloodRequests = new ArrayList<BloodRequest>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                bloodRequests.add(new BloodRequest(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bloodRequests;
    }

    // IMPLEMENT PARCELABLE START
    protected BloodRequest(Parcel in) {
        id = in.readInt();
        requester_id = in.readInt();
        requester_name = in.readString();
        blood_group = in.readString();
        blood_amount = in.readInt();
        paid_amount = in.readInt();
        lat_long = in.readString();
        location_name = in.readString();
        full_address = in.readString();
        reason = in.readString();
        postal_code = in.readString();
        created = in.readString();
        req_key = in.readString();
        num_donors = in.readInt();
        status = in.readInt();
    }

    public static final Creator<BloodRequest> CREATOR = new Creator<BloodRequest>() {
        @Override
        public BloodRequest createFromParcel(Parcel in) {
            return new BloodRequest(in);
        }

        @Override
        public BloodRequest[] newArray(int size) {
            return new BloodRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(requester_id);
        parcel.writeString(requester_name);
        parcel.writeString(blood_group);
        parcel.writeInt(blood_amount);
        parcel.writeInt(paid_amount);
        parcel.writeString(lat_long);
        parcel.writeString(location_name);
        parcel.writeString(full_address);
        parcel.writeString(reason);
        parcel.writeString(postal_code);
        parcel.writeString(created);
        parcel.writeString(req_key);
        parcel.writeInt(num_donors);
        parcel.writeInt(status);
    }
    // IMPLEMENT PARCELABLE END

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getReq_key() {
        return req_key;
    }

    public void setReq_key(String req_key) {
        this.req_key = req_key;
    }

    public int getNum_donors() {
        return num_donors;
    }

    public void setNum_donors(int num_donors) {
        this.num_donors = num_donors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
