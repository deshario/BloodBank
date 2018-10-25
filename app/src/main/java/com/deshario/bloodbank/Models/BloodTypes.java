package com.deshario.bloodbank.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BloodTypes implements Parcelable{
    private int blood_id;
    private String blood_name;

    public BloodTypes() {
    }

    // Parcelable Start
    protected BloodTypes(Parcel in) {
        blood_id = in.readInt();
        blood_name = in.readString();
    }

    public static final Creator<BloodTypes> CREATOR = new Creator<BloodTypes>() {
        @Override
        public BloodTypes createFromParcel(Parcel in) {
            return new BloodTypes(in);
        }

        @Override
        public BloodTypes[] newArray(int size) {
            return new BloodTypes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(blood_id);
        parcel.writeString(blood_name);
    }
    // Parcelable End

    public BloodTypes(JSONObject object) {
        try {
            this.blood_id = object.getInt("blood_id");
            this.blood_name = object.getString("blood_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<BloodTypes> setDatafromJson(JSONArray jsonObjects){
        ArrayList<BloodTypes> bloodTypes = new ArrayList<BloodTypes>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                bloodTypes.add(new BloodTypes(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bloodTypes;
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

}
