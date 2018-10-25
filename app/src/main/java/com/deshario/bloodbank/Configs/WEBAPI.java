package com.deshario.bloodbank.Configs;

public class WEBAPI {

    private static final String local_url = "http://192.168.1.41/";
    private static final String local_dir = "THESIS/bloodbank/web/API/";;
    private static final String local_web_dir = "THESIS/bloodbank/web/";

    private static final String url = "http://sat.nan.rmutl.ac.th/deshario/bloodbank/";
    private static final String dir = "web/API/";
    private static final String web_dir = "web/";

    public static final String CampaignImgPath = url+web_dir+"uploads/campaigns/";
    public static final String GetAllRequests = url+dir+"blood-requests/fetchAll";
    public static final String Get_Branch_Requests = url+dir+"branch-requests/fetchAll";
    public static final String GetAllCampaigns = url+dir+"campaigns/fetchAll";
    //public static final String Get_Donation_History = url+dir+"donation-verified/fetchAllByUserID/";
    public static final String Get_Donation_History = url+dir+"blood-requests-verification/fetchAllByUserID/";
    public static final String Get_Request_History = url+dir+"blood-requests/fetchAllByUserID/";
    public static final String Get_Bloodgroup_Report = url+dir+"analysis/bloodGroup";
    public static final String Get_BloodTypes = url+dir+"blood-types/fetchAll";
    public static final String SendRequestBlood = url+dir+"blood-requests/create";
    public static final String GetAllBranches = url+dir+"branches/fetchAll";
    public static final String CreateReservation = url+dir+"day-reservation/create";
    public static final String ViewReservation = url+dir+"day-reservation/fetchAllByUserID/";

    public static final String Create_Blood_Verification = url+dir+"blood-requests-verification/create";
    public static final String Create_Branch_Verification = url+dir+"branch-requests-verification/create";

    public static final String SubscribeCampaign = url+dir+"campaigns/subscribe";
    public static final String UnSubscribeCampaign = url+dir+"campaigns/unsubscribe";
    public static final String checkCampaignSubscriptions = url+dir+"campaigns/checkSubscriptions/";

    public static final String URL_LOGIN = url+dir+"users/login";
    public static final String URL_REGISTER = url+dir+"users/register";
    public static final String URL_UPDATE_TOKEN = url+dir+"users/updateToken";
    public static final String URL_REMOVE_TOKEN = url+dir+"users/removeToken";

    public static final String SAVE_BLOOD_REQ = url+dir+"saved-blood-requests/Create";
    public static final String GET_SAVED_BLOOD_REQ = url+dir+"saved-blood-requests/fetchAllByUserID/";
    public static final String SAVE_BRANCH_REQ = url+dir+"saved-branch-requests/Create";
    public static final String GET_SAVED_BRANCH_REQ = url+dir+"saved-branch-requests/fetchAllByUserID/";

    public static final String map_static = "https://maps.googleapis.com/maps/api/staticmap?center=";
    public static final String map_style_uber = "&style=feature:administrative%7Celement:geometry.fill%7Ccolor:0xd6e2e6&style=feature:administrative%7Celement:geometry.stroke%7Ccolor:0xcfd4d5&style=feature:administrative%7Celement:labels.text.fill%7Ccolor:0x7492a8&style=feature:administrative.neighborhood%7Celement:labels.text.fill%7Clightness:25&style=feature:landscape.man_made%7Celement:geometry.fill%7Ccolor:0xdde2e3&style=feature:landscape.man_made%7Celement:geometry.stroke%7Ccolor:0xcfd4d5&style=feature:landscape.natural%7Celement:geometry.fill%7Ccolor:0xdde2e3&style=feature:landscape.natural%7Celement:labels.text.fill%7Ccolor:0x7492a8&style=feature:landscape.natural.terrain%7Cvisibility:off&style=feature:poi%7Celement:geometry.fill%7Ccolor:0xdde2e3&style=feature:poi%7Celement:labels.icon%7Csaturation:-100&style=feature:poi%7Celement:labels.text.fill%7Ccolor:0x588ca4&style=feature:poi.park%7Celement:geometry.fill%7Ccolor:0xa9de83&style=feature:poi.park%7Celement:geometry.stroke%7Ccolor:0xbae6a1&style=feature:poi.sports_complex%7Celement:geometry.fill%7Ccolor:0xc6e8b3&style=feature:poi.sports_complex%7Celement:geometry.stroke%7Ccolor:0xbae6a1&style=feature:road%7Celement:labels.icon%7Csaturation:-45%7Clightness:10%7Cvisibility:on&style=feature:road%7Celement:labels.text.fill%7Ccolor:0x41626b&style=feature:road.arterial%7Celement:geometry.fill%7Ccolor:0xffffff&style=feature:road.highway%7Celement:geometry.fill%7Ccolor:0xc1d1d6&style=feature:road.highway%7Celement:geometry.stroke%7Ccolor:0xa6b5bb&style=feature:road.highway%7Celement:labels.icon%7Cvisibility:on&style=feature:road.highway.controlled_access%7Celement:geometry.fill%7Ccolor:0x9fb6bd&style=feature:road.local%7Celement:geometry.fill%7Ccolor:0xffffff&style=feature:transit%7Celement:labels.icon%7Csaturation:-70&style=feature:transit.line%7Celement:geometry.fill%7Ccolor:0xb4cbd4&style=feature:transit.line%7Celement:labels.text.fill%7Ccolor:0x588ca4&style=feature:transit.station%7Cvisibility:off&style=feature:transit.station%7Celement:labels.text.fill%7Ccolor:0x008cb5%7Cvisibility:on&style=feature:transit.station.airport%7Celement:geometry.fill%7Csaturation:-100%7Clightness:-5&style=feature:water%7Celement:geometry.fill%7Ccolor:0xa6cbe3";
    public static final String map_style = "&style=element:geometry%7Ccolor:0xebe3cd&style=element:labels.text.fill%7Ccolor:0x523735&style=element:labels.text.stroke%7Ccolor:0xf5f1e6&style=feature:administrative%7Celement:geometry.stroke%7Ccolor:0xc9b2a6&style=feature:administrative.land_parcel%7Celement:geometry.stroke%7Ccolor:0xdcd2be&style=feature:administrative.land_parcel%7Celement:labels.text.fill%7Ccolor:0xae9e90&style=feature:landscape.natural%7Celement:geometry%7Ccolor:0xdfd2ae&style=feature:poi%7Celement:geometry%7Ccolor:0xdfd2ae&style=feature:poi%7Celement:labels.text.fill%7Ccolor:0x93817c&style=feature:poi.park%7Celement:geometry.fill%7Ccolor:0xa5b076&style=feature:poi.park%7Celement:labels.text.fill%7Ccolor:0x447530&style=feature:road%7Celement:geometry%7Ccolor:0xf5f1e6&style=feature:road.arterial%7Celement:geometry%7Ccolor:0xfdfcf8&style=feature:road.highway%7Celement:geometry%7Ccolor:0xf8c967&style=feature:road.highway%7Celement:geometry.stroke%7Ccolor:0xe9bc62&style=feature:road.highway.controlled_access%7Celement:geometry%7Ccolor:0xe98d58&style=feature:road.highway.controlled_access%7Celement:geometry.stroke%7Ccolor:0xdb8555&style=feature:road.local%7Celement:labels.text.fill%7Ccolor:0x806b63&style=feature:transit.line%7Celement:geometry%7Ccolor:0xdfd2ae&style=feature:transit.line%7Celement:labels.text.fill%7Ccolor:0x8f7d77&style=feature:transit.line%7Celement:labels.text.stroke%7Ccolor:0xebe3cd&style=feature:transit.station%7Celement:geometry%7Ccolor:0xdfd2ae&style=feature:water%7Celement:geometry.fill%7Ccolor:0xb9d3c2&style=feature:water%7Celement:labels.text.fill%7Ccolor:0x92998d";

    public static final String map_new_params = "&maptype=roadmap&scale=2&zoom=";
    public static final String map_size = "&size=";
    public static final String map_key = "&key=";
    public static final String map_style_uber_custom_features = "&style=feature:administrative%7Celement:geometry.fill%7Ccolor:0xd6e2e6&style=feature:administrative%7Celement:geometry.stroke%7Ccolor:0xcfd4d5&style=feature:administrative%7Celement:labels.text.fill%7Ccolor:0x7492a8&style=feature:administrative.land_parcel%7Cvisibility:off&style=feature:administrative.neighborhood%7Cvisibility:off&style=feature:administrative.neighborhood%7Celement:labels.text.fill%7Clightness:25&style=feature:landscape.man_made%7Celement:geometry.fill%7Ccolor:0xdde2e3&style=feature:landscape.man_made%7Celement:geometry.stroke%7Ccolor:0xcfd4d5&style=feature:landscape.natural%7Celement:geometry.fill%7Ccolor:0xdde2e3&style=feature:landscape.natural%7Celement:labels.text.fill%7Ccolor:0x7492a8&style=feature:landscape.natural.terrain%7Cvisibility:off&style=feature:poi%7Celement:geometry.fill%7Ccolor:0xdde2e3&style=feature:poi%7Celement:labels.icon%7Csaturation:-100&style=feature:poi%7Celement:labels.text%7Cvisibility:off&style=feature:poi%7Celement:labels.text.fill%7Ccolor:0x588ca4&style=feature:poi.business%7Cvisibility:off&style=feature:poi.park%7Celement:geometry.fill%7Ccolor:0xa9de83&style=feature:poi.park%7Celement:geometry.stroke%7Ccolor:0xbae6a1&style=feature:poi.sports_complex%7Celement:geometry.fill%7Ccolor:0xc6e8b3&style=feature:poi.sports_complex%7Celement:geometry.stroke%7Ccolor:0xbae6a1&style=feature:road%7Celement:labels%7Cvisibility:off&style=feature:road%7Celement:labels.icon%7Csaturation:-45%7Clightness:10%7Cvisibility:off&style=feature:road%7Celement:labels.text.fill%7Ccolor:0x41626b&style=feature:road.arterial%7Celement:geometry.fill%7Ccolor:0xffffff&style=feature:road.highway%7Celement:geometry.fill%7Ccolor:0xc1d1d6&style=feature:road.highway%7Celement:geometry.stroke%7Ccolor:0xa6b5bb&style=feature:road.highway%7Celement:labels.icon%7Cvisibility:on&style=feature:road.highway.controlled_access%7Celement:geometry.fill%7Ccolor:0x9fb6bd&style=feature:road.local%7Celement:geometry.fill%7Ccolor:0xffffff&style=feature:transit%7Cvisibility:off&style=feature:transit%7Celement:labels.icon%7Csaturation:-70&style=feature:transit.line%7Celement:geometry.fill%7Ccolor:0xb4cbd4&style=feature:transit.line%7Celement:labels.text.fill%7Ccolor:0x588ca4&style=feature:transit.station%7Cvisibility:off&style=feature:transit.station%7Celement:labels.text.fill%7Ccolor:0x008cb5%7Cvisibility:on&style=feature:transit.station.airport%7Celement:geometry.fill%7Csaturation:-100%7Clightness:-5&style=feature:water%7Celement:geometry.fill%7Ccolor:0xa6cbe3&style=feature:water%7Celement:labels.text%7Cvisibility:off";
    public static final String map_addmarker = "&markers=color:red%7C"; // + latlong

    public static final String map_params = "&maptype=roadmap&scale=2&zoom=17&size=400x200&key=";
    public static final String GETPLACEDETAILS = "https://maps.googleapis.com/maps/api/place/details/json?key='  '&placeid='  '";

}