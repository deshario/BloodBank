package com.deshario.bloodbank.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.BloodRequest;
import com.deshario.bloodbank.Models.BloodTypes;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodRequest_Create extends Fragment implements View.OnClickListener {

    private Context context;
    private LinearLayout linearLayout;
    private Button btn_request;
    private RadioGroup bloodRadio;
    private CheckBox agreement;
    private FloatingActionButton fabbtn;
    private EditText et_amount, et_location, et_description;
    private String latlong, address = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog pDialog;
    private ImageView imageView;
    private Bitmap bitmap;
    private int checked_blood = -1;
    private List<BloodTypes> bloodTypesList;

    private final int CAMERA_N_WRITE_PERMISSIONS = 99;
    private final int LOCATION_PERMISSION = 88;

    private final int PLACE_PICKER_REQUEST = 3;
    private final int PICK_IMAGE_CAMERA = 2;
    private final int PICK_IMAGE_GALLERY = 1;

    private final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public BloodRequest_Create() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

        System.out.println("savedInstanceState == "+savedInstanceState);
        if(savedInstanceState == null) {
            fetchBloodTypes();
        }
        //fetchBloodTypes();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) bloodTypesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bloodrequest_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = (LinearLayout) view.findViewById(R.id.customRadioGroup);
        btn_request = (Button) view.findViewById(R.id.btn_request);
        agreement = (CheckBox) view.findViewById(R.id.chk_agreement);
        fabbtn = (FloatingActionButton) view.findViewById(R.id.fab_photo);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_request);
        et_amount = (EditText) view.findViewById(R.id.amount);
        et_location = (EditText) view.findViewById(R.id.location);
        et_description = (EditText) view.findViewById(R.id.description);
        imageView = (ImageView) view.findViewById(R.id.img);

        btn_request.setOnClickListener(this);
        fabbtn.setOnClickListener(this);
        et_location.setOnClickListener(this);

        NestedScrollView nsv = (NestedScrollView) view.findViewById(R.id.nested_scroll);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fabbtn.hide();
                } else {
                    fabbtn.show();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetContent();
            }
        });
    }

    private void setToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(" Create Request");
    }

    private void resetContent() {
        latlong = "";
        address = "";
        et_amount.setText("");
        et_location.setText("");
        et_description.setText("");
        agreement.setChecked(false);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void createRadioButton(List<BloodTypes> bloodTypes) {
        System.out.println("Creating bloodtypes");
        bloodRadio = new RadioGroup(context); //create the RadioGroup
        bloodRadio.setOrientation(RadioGroup.HORIZONTAL);//or RadioGroup.VERTICAL
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // Unchecked
                        new int[]{android.R.attr.state_checked} // Checked
                },
                new int[]{
                        getResources().getColor(R.color.new_material_red), // Unchecked Value
                        getResources().getColor(R.color.fbwhite) // Checked Value
                }
        );
        for (int i = 0; i < bloodTypes.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(bloodTypes.get(i).getBlood_id());
            radioButton.setText(bloodTypes.get(i).getBlood_name());
            radioButton.setTextSize(22);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setChecked(false);
            radioButton.setPadding(10, 10, 10, 10);
            radioButton.setBackground(context.getResources().getDrawable(R.drawable.circle_blood_type));
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setTypeface(Typeface.DEFAULT_BOLD);
            radioButton.setTextColor(colorStateList);
            if (i != 0) {
                LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 0, 0, 0);
                radioButton.setLayoutParams(layoutParams);
            }
            bloodRadio.addView(radioButton);
        }

        linearLayout.addView(bloodRadio);
        bloodRadio.setSelected(false);
        bloodRadio.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
    }

    private void fetchBloodTypes() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading");
        showDialog();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, WEBAPI.Get_BloodTypes, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        List<BloodTypes> bloodTypes = parseJsonFeed(response);
                        createRadioButton(bloodTypes);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        closeFrag();
                        System.out.println("error :: " + error);
                    }
                }
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 2 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(getRequest);
    }

    private List<BloodTypes> parseJsonFeed(JSONObject response) {
        List<BloodTypes> bloodTypesList;
        BloodTypes bloodType = new BloodTypes();
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bloodTypesList = bloodType.setDatafromJson(jsonArray);
        return bloodTypesList;
    }

    RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId){
                    checked_blood = checkedId;
                    System.out.println("Selected = "+checkedId);
                }};

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location:
                LocationPermission();
                break;
            case R.id.btn_request:
                submit();
                break;
            case R.id.fab_photo:
                PhotoPickerPermission();
                break;
            default:
        }
    }

    private void LocationPermission() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion >= Build.VERSION_CODES.M) {
            check_location_Permission();
        } else {
            location_granted();
        }
    }

    private void check_location_Permission() {
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) { // result = 0
            location_granted();
        } else { // result = -1
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        }
    }

    private void location_granted() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = builder.build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /*  =============================================== */

    private void PhotoPickerPermission() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion >= Build.VERSION_CODES.M) {
            check_photopicker_Permission();
        } else {
            photo_picker_granted();
        }
    }

    private void check_photopicker_Permission() {
        int write_access = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera_access = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (write_access == PackageManager.PERMISSION_GRANTED && camera_access == PackageManager.PERMISSION_GRANTED) { // result = 0
            photo_picker_granted();
        } else { // result = -1
            requestPermissions(PERMISSIONS, CAMERA_N_WRITE_PERMISSIONS);
        }
    }

    private void photo_picker_granted() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Image");
        String[] pictureDialogItems = {"From Gallery", "From Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent choosePictureIntent = new Intent();
        choosePictureIntent.setType("image/*");
        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (choosePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(choosePictureIntent, PICK_IMAGE_GALLERY);
        }
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA);
        }
    }

    /*  =============================================== */

    private void submit() {
        int blood_id = checked_blood;
        String amount = et_amount.getText().toString();
        String location = et_location.getText().toString(); // place name
        String description = et_description.getText().toString();

        if (!validate(blood_id, amount, location, latlong, address)) {
            Toast.makeText(context, "All fields Required !", Toast.LENGTH_SHORT).show();
        } else {
            if (!agreement.isChecked()) {
                Toast.makeText(context, "Please accept terms to continue", Toast.LENGTH_SHORT).show();
            } else {
                BloodRequest bloodRequest = new BloodRequest();
                bloodRequest.setRequester_id(1);
                bloodRequest.setBlood_amount(Integer.valueOf(amount));
                bloodRequest.setBlood_group(String.valueOf(blood_id));
                bloodRequest.setLat_long(latlong);
                bloodRequest.setFull_address(address);
                bloodRequest.setReason(description);
                bloodRequest.setLocation_name(location);
                request(bloodRequest);
            }
        }
    }

    public boolean validate(int id, String amount, String location, String latlong, String address) {
        boolean valid = true;
        if (id <= -1) {
            valid = false;
            //System.out.println("ID == " + id);
        }

        if (amount.isEmpty()) {
            valid = false;
            //System.out.println("amount == " + amount);
        }

        if (location.isEmpty()) {
            valid = false;
            //System.out.println("location == " + location);
        }

        if (latlong == null || latlong.isEmpty()) {
            valid = false;
            //System.out.println("latlong == " + latlong);
        }

        if (address == null || address.isEmpty()) {
            valid = false;
            //System.out.println("address == " + address);
        }

        return valid;
    }

    private void request(final BloodRequest bloodRequest) {
        final int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
        pDialog.setMessage("Requesting...");
        showDialog();
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.SendRequestBlood,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status == "true") {
                                resetContent();
                                checked_blood = -1;
                                bloodRadio.clearCheck();
                                Toast.makeText(context, "Request Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("requester_id", String.valueOf(user_id));
                params.put("blood_group", bloodRequest.getBlood_group());
                params.put("blood_amount", String.valueOf(bloodRequest.getBlood_amount()));
                params.put("lat_long", bloodRequest.getLat_long());
                params.put("location_name", bloodRequest.getLocation_name());
                params.put("full_address", bloodRequest.getFull_address());
                params.put("reason", bloodRequest.getReason());
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(postRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void closeFrag() {
        getActivity().onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { // MAIN OUTTER PERMISSION
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Granted
                    location_granted();
                } else { // Denied
                    Toast.makeText(context, "Location Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_N_WRITE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean cameraGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFileGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraGranted && writeExternalFileGranted) {
                        photo_picker_granted();
                    } else {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    Place place = PlacePicker.getPlace(context, data);
                    et_location.setText(place.getName());
                    latlong = String.valueOf(place.getLatLng().latitude + "," + place.getLatLng().longitude);
                    address = String.valueOf(place.getAddress());
                }
                break;
            case PICK_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PICK_IMAGE_CAMERA:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

}
