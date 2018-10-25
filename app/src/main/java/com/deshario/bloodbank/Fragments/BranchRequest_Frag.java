package com.deshario.bloodbank.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deshario.bloodbank.Adapters.BranchRequestAdapter;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.BranchRequest;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BranchRequest_Frag extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    GoogleMap mGoogleMap;
    MapView mapView;
    String Selected_Map_Type;

    public BranchRequest_Frag() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.branchrequest_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.branches_view);
        mapView = (MapView) view.findViewById(R.id.mapView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.line_divider));
        recyclerView.addItemDecoration(itemDecorator);
        //getBranchRequests();

        AppBarLayout mAppBarLayout = (AppBarLayout) view.findViewById(R.id.appbar_lay);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    MapsInitializer.initialize(context);
                    mGoogleMap = googleMap;
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    try {
                        boolean success = mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style_json));
                        if (!success) {
                            System.out.println("Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        System.out.println("Can't find style. Error:" + e);
                    }
                    mGoogleMap.setMyLocationEnabled(true);
                    getBranchRequests();
                }
            });
        }
    }

    private void setToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(" Branch Request");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //menu.add("Map Type").setIcon(R.drawable.ic_place_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1, 1,  "Normal");
        menu.add(0, 1, 1,  "Satellite");
        menu.add(0, 1, 1,  "Hybrid");
        menu.add(0, 1, 1,  "Terrain");
//        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_place_black_24dp), "Normal"));
//        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.ic_place_black_24dp), "Satellite"));
//        menu.add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.ic_place_black_24dp), "Hybrid"));
//        menu.add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.ic_place_black_24dp), "Terrain"));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private CharSequence menuIconWithText(Drawable r, String title) {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Selected_Map_Type = "NORMAL";
                break;
            case 2:
                Selected_Map_Type = "HYBRID";
                break;
            case 3:
                Selected_Map_Type = "SATELLITE";
                break;
            case 4:
                Selected_Map_Type = "TERRIAN";
                break;
            default:
                Selected_Map_Type = "NORMAL";
        }
        change_Map_Type(Selected_Map_Type);
        return super.onOptionsItemSelected(item);
    }

    public void change_Map_Type(String MAPTYPE){
        switch (MAPTYPE){
            case "NORMAL":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "HYBRID":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "SATELLITE":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case "TERRIAN":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
        }
    }

    private void drawMarker(LatLng point, String title, String desc, boolean locater) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        if (locater == true) {
            MarkerOptions markerOptions = new MarkerOptions();  // Creating an instance of MarkerOptions
            markerOptions.position(point); // Latitude,Longtitude
            markerOptions.title(title); // Title
            markerOptions.snippet(desc);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 18.0f));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16.5f), 4000, null);
            mGoogleMap.addMarker(
                    markerOptions.icon(icon)
            ).showInfoWindow();

        } else {
            MarkerOptions markerOptions = new MarkerOptions();  // Creating an instance of MarkerOptions
            markerOptions.position(point); // Latitude,Longtitude
            markerOptions.title(title); // Title
            markerOptions.snippet(desc);
            mGoogleMap.addMarker(markerOptions); // Adding marker on the Google Map
            //mGoogleMap.addMarker(new MarkerOptions().position(point).title(title)); // Adding marker on the Google Map
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(point)      // Sets the center of the map to Mountain View
                .zoom(0)            // Sets the zoom
                .bearing(0)        // Sets the orientation of the camera to east
                .tilt(45)            // Sets the tilt of the camera to 30 degrees
                .build();            // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void getBranchRequests() {
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, WEBAPI.Get_Branch_Requests, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = null;
                        try {
                            status = response.getString("status");
                            if (status == "true") {
                                List<BranchRequest> originalList = parseJsonFeed(response);
                                List<BranchRequest> branchRequests = calculateDistance_n_Sort(parseJsonFeed(response));
                                BranchRequestAdapter.ListAdapterListener listAdapterListener = null;
                                BranchRequestAdapter branchRequestAdapter = new BranchRequestAdapter(branchRequests, listAdapterListener);
                                recyclerView.setAdapter(branchRequestAdapter);

                                for (int i = 0; i < originalList.size(); i++) {
                                    BranchRequest branchRequest = originalList.get(i);
                                    String[] latlong = branchRequest.getBranch_lat_long().split(",");
                                    double latitude = Double.parseDouble(latlong[0]);
                                    double longitude = Double.parseDouble(latlong[1]);
                                    int actual = branchRequest.getBlood_amount()-branchRequest.getPaid_amount();
                                    String desc = "Request "+actual+" units of "+branchRequest.getBlood_group()+" bloodgroup";
                                    drawMarker(new LatLng(latitude, longitude), branchRequest.getBranch_name(),desc, false);
                                }

                            } else {
                                Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private List<BranchRequest> parseJsonFeed(JSONObject response) {
        List<BranchRequest> branchRequests;
        BranchRequest branchRequest = new BranchRequest();
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        branchRequests = branchRequest.setDatafromJson(jsonArray);
        return branchRequests;
    }

    private List<BranchRequest> calculateDistance_n_Sort(List<BranchRequest> branchRequestList) {
        ArrayList<BranchRequest> sorted_branchrequest_list = new ArrayList<BranchRequest>();
        Location locationStart = new Location("RMUTL NAN"); // 18.804914,100.788363
        locationStart.setLatitude(18.804914);
        locationStart.setLongitude(100.788363);

        for (int i = 0; i < branchRequestList.size(); i++) {
            BranchRequest branchRequest = branchRequestList.get(i);
            String[] latLng = branchRequest.getBranch_lat_long().split(",");
            double latitude = Double.parseDouble(latLng[0]);
            double longitude = Double.parseDouble(latLng[1]);
            Location locationEnd = new Location(branchRequest.getBranch_name());
            locationEnd.setLatitude(latitude);
            locationEnd.setLongitude(longitude);
            double distance = locationStart.distanceTo(locationEnd) / 1000;
            branchRequest.setTemp_distance(distance);
            sorted_branchrequest_list.add(branchRequest);
        }

        Collections.sort(sorted_branchrequest_list, new Comparator<BranchRequest>() {
            public int compare(BranchRequest c1, BranchRequest c2) {
                return Double.compare(c1.getTemp_distance(), c2.getTemp_distance());
            }
        });
        return sorted_branchrequest_list;
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
