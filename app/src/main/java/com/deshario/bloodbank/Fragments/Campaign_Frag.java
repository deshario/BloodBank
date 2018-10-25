package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deshario.bloodbank.Adapters.CampaignAdapter;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.Campaigns;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class Campaign_Frag extends Fragment {

    View view;
    private ImageLoader imageLoader;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipecamp;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private CoordinatorLayout rootLayout;
    private Context context;
    public Campaign_Frag(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.campaign_frag, container, false);
       // setHasOptionsMenu(true);
        setToolbar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipecamp = (SwipeRefreshLayout)view.findViewById(R.id.refresh_camp);
        recyclerView = (RecyclerView)view.findViewById(R.id.cardView);
        mShimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view);
        rootLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        MainActivity.bottomNavigation.setNotification("",3);

        //mSwipecamp.setRefreshing(true);

        mSwipecamp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                //MainActivity.bottomNavigation.setNotification("", 3);
                //Toast.makeText(getActivity(),"Refreshing",Toast.LENGTH_SHORT).show();
            }
        });

        getData();
    }

    private void setToolbar(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(" Campaigns");
    }

    private void getData(){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, WEBAPI.GetAllCampaigns, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Campaigns> camps = parseJsonFeed(response);
                        CampaignAdapter campaignAdapter = new CampaignAdapter(camps);
                        recyclerView.setAdapter(campaignAdapter);
                        if(mSwipecamp.isRefreshing()) {
                            mSwipecamp.setRefreshing(false);
                        }
                        stopShimmer();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error :: "+error);
                    }
                }
        )
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 60 * 1000; // in 1(1*60) minutes cache will be hit, but also refreshed on background
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

    private List<Campaigns> parseJsonFeed(JSONObject response){
        List<Campaigns> campaigns;
        Campaigns campaign = new Campaigns();
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        campaigns = campaign.setDatafromJson(jsonArray);
        return campaigns;
    }

    @Override
    public void onResume() {
        mShimmerFrameLayout.startShimmerAnimation();
        super.onResume();
        setToolbar();
        getData();
    }

    @Override
    public void onPause() {
        mShimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    public void stopShimmer() {
        mShimmerFrameLayout.stopShimmerAnimation();
        rootLayout.setBackgroundColor(context.getResources().getColor(R.color.fbgrey));
        mShimmerFrameLayout.setVisibility(View.GONE);
    }

    public void startShimmer() {
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.startShimmerAnimation();
    }

}
