package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deshario.bloodbank.Adapters.MoreMenuAdapter;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.MoreMenu;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreMenu_Frag extends Fragment {

    private Context context;
    private SwipeRefreshLayout refresher;
    TextView username,reg_date;
    LinearLayout profile_bg;

    public MoreMenu_Frag(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.more_menu_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = (TextView)view .findViewById(R.id.user_name);
        reg_date = (TextView)view .findViewById(R.id.registered_date);
        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.content_view);
        FloatingActionButton fab_refresh = (FloatingActionButton) view.findViewById(R.id.refresh_token);
        profile_bg = (LinearLayout)view.findViewById(R.id.img);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                if(refresher.isRefreshing()) {
                    refresher.setRefreshing(false);
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToken();
            }
        });

        String user = Deshario_Functions.getUserinfo(context,"username",false);
        String registered = Deshario_Functions.getUserinfo(context,"created",false);
        String user_id = Deshario_Functions.getUserinfo(context,"user_id",true);
        String date = Deshario_Functions.getCustomDate(registered,3);

        username.setText(user);
        reg_date.setText(date);

        List<MoreMenu> moreMenus = new ArrayList<>();
        String[] menus = {"Verify Donations","Usage Records","Reserve Donation","Logout"};
        String[] desc = {"Make verify for your each donations","All Requests amd Donations Records","Reserve my upcoming donation date","Remove my account"};
        int[] icons = {R.drawable.ic_verified_user_white_36dp,R.drawable.ic_history_white_36dp,R.drawable.ic_today_white_36dp,R.drawable.ic_exit_to_app_white_36dp};
        for(int i=0; i<menus.length; i++){
            MoreMenu moreMenu = new MoreMenu();
            moreMenu.setTitle(menus[i]);
            moreMenu.setDesc(desc[i]);
            moreMenu.setIcon(context.getResources().getDrawable(icons[i]));
            moreMenus.add(moreMenu);
        }
        MoreMenuAdapter moreMenuAdapter = new MoreMenuAdapter(moreMenus);
        recyclerView.setAdapter(moreMenuAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(context, llm.getOrientation()) {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        int position = parent.getChildAdapterPosition(view);
                        // hide the divider for the last child
                        if (position == parent.getAdapter().getItemCount() - 1) {
                            outRect.setEmpty();
                        } else {
                            super.getItemOffsets(outRect, view, parent, state);
                        }
                    }
                }
        );
    }

    private void updateToken() {
        final String username = Deshario_Functions.getUserinfo(context,"username",false);
        final String token = FirebaseInstanceId.getInstance().getToken();
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.URL_UPDATE_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status == "true") {
                                //Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show();
                            } else {
                               //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("virtual_user", username);
                params.put("profile_token", token);
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(postRequest);
    }

}
