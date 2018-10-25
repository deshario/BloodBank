package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deshario.bloodbank.Adapters.BloodRequestdapter;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.BloodRequest;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 */
public class BloodRequest_Frag extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;
    private FloatingActionButton FabNew;

    public BloodRequest_Frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bloodrequest_frag, container, false);
        setHasOptionsMenu(true);
        setToolbar();
        setDefaultWhiteDrawable();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)view.findViewById(R.id.cardList);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        FabNew = view.findViewById(R.id.gotop);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        MainActivity.bottomNavigation.setNotification("",1);

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && FabNew.getVisibility() == View.VISIBLE) {
                    FabNew.hide();
                } else if (dy < 0 && FabNew.getVisibility() != View.VISIBLE) {
                    FabNew.show();
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

//        FabNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                mSwipeRefreshLayout.setRefreshing(true);
//                getData();
//            }
//        });

        //If at any point, you want to disable pull to refresh gestures and progress animations, call setEnabled(false) on the view.
        //mSwipeRefreshLayout.setEnabled(false);
    }

    private void setToolbar(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(" Blood Requests");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_req_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.new_req_btn:
                changefrag(new BloodRequest_Create(), MainActivity.TAG_CREATE_REQ_FRAG);
                break;
            default:
                break;
        }
        return true;
    }

    private void changefrag(Fragment fragment, String CustomTAG){
        if(fragment != null){
            MainActivity.hidebottomnav();
            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.slide_top));
                fragment.setEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.slide_top));
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment,CustomTAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    private void setDefaultWhiteDrawable(){
        //Deshario_Functions.setWhiteTint(getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));
    }

    private void getData(){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, WEBAPI.GetAllRequests, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response){
                        List<BloodRequest> bloodRequests = parseJsonFeed(response);
                        System.out.println("bloodRequests :: "+bloodRequests.size());
                        BloodRequestdapter bloodRequestdapter = new BloodRequestdapter(bloodRequests);
                        recyclerView.setAdapter(bloodRequestdapter);
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error :: "+error);
                    }
                }
        )   {
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

    private List<BloodRequest> parseJsonFeed(JSONObject response){
        List<BloodRequest> bloodRequests;
        BloodRequest model = new BloodRequest();
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bloodRequests = model.setDatafromJson(jsonArray);
        return bloodRequests;
    }


    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
        setDefaultWhiteDrawable();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }
}
