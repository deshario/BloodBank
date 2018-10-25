package com.deshario.bloodbank.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DayReservation implements Parcelable {
    private int reserved_id;
    private int user_id;
    private String username;
    private int branch_id;
    private String branch_name;
    private String user_notes;
    private String reservation_key;
    private String reserved_date;

    public DayReservation(){}

    public DayReservation(JSONObject object) {
        try {
            this.reserved_id = object.getInt("reserved_id");
            this.user_id = object.getInt("user_id");
            this.username = object.getString("username");
            this.branch_id = object.getInt("branch_id");
            this.branch_name = object.getString("branch_name");
            this.user_notes = object.getString("user_notes");
            this.reservation_key = object.getString("reservation_key");
            this.reserved_date = object.getString("reserved_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<DayReservation> setDatafromJson(JSONArray jsonObjects){
        ArrayList<DayReservation> dayReservations = new ArrayList<DayReservation>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                dayReservations.add(new DayReservation(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dayReservations;
    }

    protected DayReservation(Parcel in) {
        reserved_id = in.readInt();
        user_id = in.readInt();
        username = in.readString();
        branch_id = in.readInt();
        branch_name = in.readString();
        user_notes = in.readString();
        reservation_key = in.readString();
        reserved_date = in.readString();
    }

    public static final Creator<DayReservation> CREATOR = new Creator<DayReservation>() {
        @Override
        public DayReservation createFromParcel(Parcel in) {
            return new DayReservation(in);
        }

        @Override
        public DayReservation[] newArray(int size) {
            return new DayReservation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(reserved_id);
        parcel.writeInt(user_id);
        parcel.writeString(username);
        parcel.writeInt(branch_id);
        parcel.writeString(branch_name);
        parcel.writeString(user_notes);
        parcel.writeString(reservation_key);
        parcel.writeString(reserved_date);
    }

    public int getReserved_id() {
        return reserved_id;
    }

    public void setReserved_id(int reserved_id) {
        this.reserved_id = reserved_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getUser_notes() {
        return user_notes;
    }

    public void setUser_notes(String user_notes) {
        this.user_notes = user_notes;
    }

    public String getReservation_key() {
        return reservation_key;
    }

    public void setReservation_key(String reservation_key) {
        this.reservation_key = reservation_key;
    }

    public String getReserved_date() {
        return reserved_date;
    }

    public void setReserved_date(String reserved_date) {
        this.reserved_date = reserved_date;
    }

}
