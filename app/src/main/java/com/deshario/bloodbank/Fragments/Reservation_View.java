package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.os.Bundle;
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
import com.deshario.bloodbank.Adapters.ReservationAdapter;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.DayReservation;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reservation_View extends Fragment {

    private Context context;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipecamp;
    private LinearLayout linearLayout;

    public Reservation_View() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reservation_view_frag, container, false);
        // setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipecamp = (SwipeRefreshLayout)view.findViewById(R.id.refresh_camp);
        recyclerView = (RecyclerView)view.findViewById(R.id.cardView);
        linearLayout = (LinearLayout) view.findViewById(R.id.no_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        MainActivity.bottomNavigation.setNotification("",3);

        mSwipecamp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
                getData(user_id);
            }
        });

        int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
        getData(user_id);
    }

    private void getData(int u_id){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, WEBAPI.ViewReservation+u_id, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status == "true") {
                                hideshow(linearLayout,recyclerView);
                                List<DayReservation> camps = parseJsonFeed(response);
                                ReservationAdapter reservationAdapter = new ReservationAdapter(camps);
                                recyclerView.setAdapter(reservationAdapter);
                            } else {
                                hideshow(recyclerView,linearLayout);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(mSwipecamp.isRefreshing()) {
                            mSwipecamp.setRefreshing(false);
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
        );
        VolleySingleton.getInstance(context).addToRequestQueue(getRequest);
    }

    private List<DayReservation> parseJsonFeed(JSONObject response){
        List<DayReservation> dayReservationList;
        DayReservation dayReservation = new DayReservation();
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dayReservationList = dayReservation.setDatafromJson(jsonArray);
        return dayReservationList;
    }

    private void hideshow(View hide, View show){
        hide.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
    }
}
