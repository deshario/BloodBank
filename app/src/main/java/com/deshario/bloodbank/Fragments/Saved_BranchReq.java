package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deshario.bloodbank.Adapters.SavedBranchReqAdapter;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.SavedBranchRequest;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Saved_BranchReq extends Fragment {

    private RecyclerView recyclerView;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;

    public Saved_BranchReq() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_request_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = (LinearLayout) view.findViewById(R.id.no_data);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_camp);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
        fetchRequest(user_id);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
                fetchRequest(user_id);
            }
        });

    }

    private void fetchRequest(int id){
        final String url = WEBAPI.GET_SAVED_BRANCH_REQ+id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status == "true"){
                                hideshow(linearLayout,recyclerView);
                                List<SavedBranchRequest> savedBranchRequestList = parseJsonFeed(response);
                                SavedBranchReqAdapter savedBranchReqAdapter = new SavedBranchReqAdapter(savedBranchRequestList);
                                recyclerView.setAdapter(savedBranchReqAdapter);
                            } else {
                                hideshow(recyclerView,linearLayout);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error :: "+error);
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        )   {
//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                try {
//                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
//                    if (cacheEntry == null) {
//                        cacheEntry = new Cache.Entry();
//                    }
//                    final long cacheHitButRefreshed = 2 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
//                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
//                    long now = System.currentTimeMillis();
//                    final long softExpire = now + cacheHitButRefreshed;
//                    final long ttl = now + cacheExpired;
//                    cacheEntry.data = response.data;
//                    cacheEntry.softTtl = softExpire;
//                    cacheEntry.ttl = ttl;
//                    String headerValue;
//                    headerValue = response.headers.get("Date");
//                    if (headerValue != null) {
//                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                    }
//                    headerValue = response.headers.get("Last-Modified");
//                    if (headerValue != null) {
//                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                    }
//                    cacheEntry.responseHeaders = response.headers;
//                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                    return Response.success(new JSONObject(jsonString), cacheEntry);
//                } catch (UnsupportedEncodingException e) {
//                    return Response.error(new ParseError(e));
//                } catch (JSONException e) {
//                    return Response.error(new ParseError(e));
//                }
//            }

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

    private List<SavedBranchRequest> parseJsonFeed(JSONObject response){
        List<SavedBranchRequest> savedBranchRequestList;
        SavedBranchRequest savedBranchRequest = new SavedBranchRequest();
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        savedBranchRequestList = savedBranchRequest.setDatafromJson(jsonArray);
        return savedBranchRequestList;
    }

    private void hideshow(View hide, View show){
        hide.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
    }
}
