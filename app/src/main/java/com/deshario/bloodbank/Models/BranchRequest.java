package com.deshario.bloodbank.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//public class BranchRequest implements Comparable<BranchRequest>{
public class BranchRequest implements Parcelable{
    private int branch_id;
    private int branchrequest_id;
    private String branch_name;
    private String branch_address;
    private String branch_code;
    private String branch_lat_long;
    private String blood_group;
    private int blood_amount;
    private int paid_amount;
    private String created;
    private int request_status;
    private double temp_distance;

    public BranchRequest(){}

    public BranchRequest(JSONObject object) {
        try {
            this.branch_id = object.getInt("branch_id");
            this.branchrequest_id =object.getInt("branchrequest_id");
            this.branch_name = object.getString("branch_name");
            this.branch_address = object.getString("branch_address");
            this.branch_code = object.getString("branch_code");
            this.branch_lat_long = object.getString("branch_lat_long");
            this.blood_group = object.getString("blood_group");
            this.blood_amount = object.getInt("blood_amount");
            this.paid_amount = object.getInt("paid_amount");
            this.created = object.getString("created");
            this.request_status = object.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<BranchRequest> setDatafromJson(JSONArray jsonObjects){
        ArrayList<BranchRequest> branchRequestArrayList = new ArrayList<BranchRequest>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                branchRequestArrayList.add(new BranchRequest(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return branchRequestArrayList;
    }

    // IMPLEMENT PARCELABLE START
    protected BranchRequest(Parcel in) {
        branch_id = in.readInt();
        branchrequest_id = in.readInt();
        branch_name = in.readString();
        branch_address = in.readString();
        branch_code = in.readString();
        branch_lat_long = in.readString();
        blood_group = in.readString();
        blood_amount = in.readInt();
        paid_amount = in.readInt();
        created = in.readString();
        request_status = in.readInt();
        temp_distance = in.readDouble();
    }

    public static final Creator<BranchRequest> CREATOR = new Creator<BranchRequest>() {
        @Override
        public BranchRequest createFromParcel(Parcel in) {
            return new BranchRequest(in);
        }

        @Override
        public BranchRequest[] newArray(int size) {
            return new BranchRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(branch_id);
        parcel.writeInt(branchrequest_id);
        parcel.writeString(branch_name);
        parcel.writeString(branch_address);
        parcel.writeString(branch_code);
        parcel.writeString(branch_lat_long);
        parcel.writeString(blood_group);
        parcel.writeInt(blood_amount);
        parcel.writeInt(paid_amount);
        parcel.writeString(created);
        parcel.writeInt(request_status);
        parcel.writeDouble(temp_distance);
    }
    // IMPLEMENT PARCELABLE END

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public int getBranchrequest_id() {
        return branchrequest_id;
    }

    public void setBranchrequest_id(int branchrequest_id) {
        this.branchrequest_id = branchrequest_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
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

    public String getBranch_lat_long() {
        return branch_lat_long;
    }

    public void setBranch_lat_long(String branch_lat_long) {
        this.branch_lat_long = branch_lat_long;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getRequest_status() {
        return request_status;
    }

    public void setRequest_status(int request_status) {
        this.request_status = request_status;
    }

    public double getTemp_distance() {
        return temp_distance;
    }

    public void setTemp_distance(double temp_distance) {
        this.temp_distance = temp_distance;
    }

    /*
        @Override
        public int compareTo(@NonNull BranchRequest branchRequest) {
            //return new Double(temp_distance).compareTo( branchRequest.getTemp_distance());
            return Double.valueOf(temp_distance).compareTo( branchRequest.getTemp_distance());
        }

        // Usage
            ArrayList<BranchRequest> out = new ArrayList<BranchRequest>();
            out.add(new BranchRequest(20));
            out.add(new BranchRequest(15));
            Collections.sort(out);
            System.out.println(out);
    */
}
