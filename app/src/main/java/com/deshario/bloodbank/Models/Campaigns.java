package com.deshario.bloodbank.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Campaigns implements Parcelable{
    private int campaign_id;
    private String campaign_name;
    private String campaign_desc;
    private String campaign_img;
    private String campaign_created;
    private String campaign_coordinates;
    private String campaign_address;
    private String campaign_key;
    private String campaign_creator;
    private String campaign_joined;
    private int campaign_status;

    public Campaigns(){}

    public Campaigns(JSONObject object) {
        try {
            this.campaign_id = object.getInt("campaign_id");
            this.campaign_name = object.getString("campaign_name");
            this.campaign_desc = object.getString("campaign_desc");
            this.campaign_img = object.getString("campaign_img");
            this.campaign_created = object.getString("campaign_created");
            this.campaign_coordinates = object.getString("campaign_coordinates");
            this.campaign_address = object.getString("campaign_address");
            this.campaign_key = object.getString("campaign_key");
            this.campaign_creator = object.getString("campaign_creator");
            this.campaign_joined = object.getString("campaign_joined");
            this.campaign_status = object.getInt("campaign_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Campaigns> setDatafromJson(JSONArray jsonObjects){
        ArrayList<Campaigns> campaigns = new ArrayList<Campaigns>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                campaigns.add(new Campaigns(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return campaigns;
    }

    // IMPLEMENT PARCELABLE START
    protected Campaigns(Parcel in) {
        campaign_id = in.readInt();
        campaign_name = in.readString();
        campaign_desc = in.readString();
        campaign_img = in.readString();
        campaign_created = in.readString();
        campaign_coordinates = in.readString();
        campaign_address = in.readString();
        campaign_key = in.readString();
        campaign_creator = in.readString();
        campaign_joined = in.readString();
        campaign_status = in.readInt();
    }

    public static final Creator<Campaigns> CREATOR = new Creator<Campaigns>() {
        @Override
        public Campaigns createFromParcel(Parcel in) {
            return new Campaigns(in);
        }

        @Override
        public Campaigns[] newArray(int size) {
            return new Campaigns[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(campaign_id);
        parcel.writeString(campaign_name);
        parcel.writeString(campaign_desc);
        parcel.writeString(campaign_img);
        parcel.writeString(campaign_created);
        parcel.writeString(campaign_coordinates);
        parcel.writeString(campaign_address);
        parcel.writeString(campaign_key);
        parcel.writeString(campaign_creator);
        parcel.writeString(campaign_joined);
        parcel.writeInt(campaign_status);
    }
    // IMPLEMENT PARCELABLE END

    public int getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(int campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }

    public String getCampaign_desc() {
        return campaign_desc;
    }

    public void setCampaign_desc(String campaign_desc) {
        this.campaign_desc = campaign_desc;
    }

    public String getCampaign_img() {
        return campaign_img;
    }

    public void setCampaign_img(String campaign_img) {
        this.campaign_img = campaign_img;
    }

    public String getCampaign_created() {
        return campaign_created;
    }

    public void setCampaign_created(String campaign_created) {
        this.campaign_created = campaign_created;
    }

    public String getCampaign_coordinates() {
        return campaign_coordinates;
    }

    public void setCampaign_coordinates(String campaign_coordinates) {
        this.campaign_coordinates = campaign_coordinates;
    }

    public String getCampaign_address() {
        return campaign_address;
    }

    public void setCampaign_address(String campaign_address) {
        this.campaign_address = campaign_address;
    }

    public String getCampaign_key() {
        return campaign_key;
    }

    public void setCampaign_key(String campaign_key) {
        this.campaign_key = campaign_key;
    }

    public String getCampaign_creator() {
        return campaign_creator;
    }

    public void setCampaign_creator(String campaign_creator) {
        this.campaign_creator = campaign_creator;
    }

    public String getCampaign_joined() {
        return campaign_joined;
    }

    public void setCampaign_joined(String campaign_joined) {
        this.campaign_joined = campaign_joined;
    }

    public int getCampaign_status() {
        return campaign_status;
    }

    public void setCampaign_status(int campaign_status) {
        this.campaign_status = campaign_status;
    }

}
