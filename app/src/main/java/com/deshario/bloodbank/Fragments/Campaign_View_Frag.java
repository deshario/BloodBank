package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.Campaigns;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Campaign_View_Frag extends Fragment {

    private AppBarLayout appBarLayout;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Toolbar RootToolbar;
    private Campaigns Mcampaigns;
    private SwipeRefreshLayout swipedetails;
    public static int int_items = 2;

    public Campaign_View_Frag() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.campaign_view_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RootToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        //swipedetails = (SwipeRefreshLayout)view.findViewById(R.id.refresh_details);
        ImageView image = (ImageView) view.findViewById(R.id.main_img);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Mcampaigns = bundle.getParcelable("campaigns");
            Glide.with(getActivity())
                    .load(WEBAPI.CampaignImgPath + Mcampaigns.getCampaign_img())
                    .apply(new RequestOptions()
                            .placeholder(R.color.dark1)
                            .error(R.color.mat_red)
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // load into cache
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform())
                    .into(image);

            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.setTitle(Mcampaigns.getCampaign_name());
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                    MainActivity.restoreBottomNav();
                }
            });
            work();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        //bundle.putParcelable("campaigns", Mcampaigns);
    }

    public interface SubscribeCallback {
        void onSuccessResponse(String result);
    }

    public interface UnSubscribeCallback {
        void onSuccessResponse(String result);
    }

    public interface StatusCallback {
        void onSuccessResponse(JSONObject result);
    }

    public void work() {
        MyAdapter mSectionsPagerAdapter = new MyAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mViewPager);
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_description_white_24dp);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_info_outline_white_24dp);
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Fragment currentFragment = getFragmentManager().findFragmentById(R.id.frame_container);
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.frame_container, new Campaign_Details_Frag(), currentFragment.getTag());
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                fragmentTransaction.commit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String PAGE_ARG_BUNDLE = "pageno";
        private static final String CAMP_ARG_BUNDLE = "campaign";
        private Context context;
        private boolean subscribeStatus;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            context = getActivity().getApplicationContext();
        }

        public static PlaceholderFragment newInstance(int sectionNumber, Campaigns campaigns) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(PAGE_ARG_BUNDLE, sectionNumber);
            args.putParcelable(CAMP_ARG_BUNDLE, campaigns);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int DESC = 0;
            int ABOUT = 1;
            int subscribed = 0;
            Bundle args = getArguments();
            int page = args.getInt(PAGE_ARG_BUNDLE);
            final Campaigns campaigns = args.getParcelable(CAMP_ARG_BUNDLE);
            View rootView = null;

            if (page == DESC) {
                rootView = inflater.inflate(R.layout.campaign_view_tab1, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.camp_desc);
                final Button btn_subscribe = (Button) rootView.findViewById(R.id.subscribe_campaign);
                textView.setText(campaigns.getCampaign_desc());
                btn_subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        subscribeCamp(campaigns, new SubscribeCallback(){
                            @Override
                            public void onSuccessResponse(String result){
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String status = jsonObject.getString("status");
                                    String data = jsonObject.getString("data");
                                    if (status.equals("true")){
                                        if(data.equals("Subscription already created")){
                                            //Toast.makeText(context,"Already Subscribed",Toast.LENGTH_SHORT).show();
                                            unsubscribe(campaigns,btn_subscribe);
                                        }else{
                                            btn_subscribe.setText("TAP TO UNSUBSCRIBE");
                                            btn_subscribe.setBackgroundColor(context.getResources().getColor(R.color.material_primary));
                                        }
                                    } else {
                                        Toast.makeText(context,"Something went wrong !",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });

                checkSubscription(context, campaigns, new StatusCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            String status = result.getString("status");
                            if (status.equals("true")) {
                                btn_subscribe.setText("TAP TO UNSUBSCRIBE");
                                btn_subscribe.setBackgroundColor(context.getResources().getColor(R.color.material_primary));
                            } else {
                                btn_subscribe.setText("Subscribe");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else if (page == ABOUT) {
                rootView = inflater.inflate(R.layout.campaign_view_tab2, container, false);
                TextView creator = (TextView) rootView.findViewById(R.id.camp_creator);
                TextView location = (TextView) rootView.findViewById(R.id.camp_address);
                TextView enrolled = (TextView) rootView.findViewById(R.id.camp_enrolled);
                TextView created = (TextView) rootView.findViewById(R.id.camp_created);
                LinearLayout location_tab = (LinearLayout) rootView.findViewById(R.id.location_tab);
                final TextView subscribe_status = (TextView) rootView.findViewById(R.id.subscription_status);
                TextView[] alltextviews = {creator, location, enrolled, created};
                setBold(alltextviews);
                Integer counters = Integer.parseInt(campaigns.getCampaign_joined());
                if (counters > 0 && counters < 10) {
                    enrolled.setText("0" + counters);
                } else {
                    enrolled.setText("" + counters);
                }

                checkSubscription(context, campaigns, new StatusCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            String status = result.getString("status");
                            if (status.equals("true")) {
                                subscribe_status.setText("Subscribed");
                            } else {
                                subscribe_status.setText("Not Subscribed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                creator.setText(campaigns.getCampaign_creator());
                location.setText(campaigns.getCampaign_address());
                String Mdatetime = Deshario_Functions.getCustomDate(campaigns.getCampaign_created(), 3); // 15 Jan, 13:45
                created.setText(Mdatetime);
                location_tab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View bottomSheetView = getLayoutInflater().inflate(R.layout.campaign_view_tab2_dialog, null);
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                        bottomSheetDialog.setContentView(bottomSheetView);
                        //BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
                        TextView submit_btn = (TextView) bottomSheetView.findViewById(R.id.menu_ok);
                        TextView dismiss_btn = (TextView) bottomSheetView.findViewById(R.id.menu_cancel);
                        submit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                                navigate(view, campaigns.getCampaign_coordinates());
                            }
                        });
                        dismiss_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.hide();
                            }
                        });
                        bottomSheetDialog.show();
                    }
                });
            }

            return rootView;
        }

        private void unsubscribe(final Campaigns campaigns, final Button button){
            UnsubscribeCamp(campaigns, new UnSubscribeCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        String data = jsonObject.getString("data");
                        if (status.equals("true")){
                            button.setText("SUBSCRIBE");
                            button.setBackgroundColor(context.getResources().getColor(R.color.new_material_red));
                        } else {
                            Toast.makeText(context,"Something went wrong !",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void navigate(View view, String latlong) {
            Toast.makeText(view.getContext(), "NAVIGATING :: " + latlong, Toast.LENGTH_SHORT).show();
        }

        private void setBold(TextView[] textViews) {
            for (TextView textView : textViews)
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }

        private void checkSubscription(final Context context, final Campaigns campaigns, final StatusCallback statusCallback) {
            final int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context, "user_id", true));
            final String url = WEBAPI.checkCampaignSubscriptions + user_id + "/" + campaigns.getCampaign_id();
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            statusCallback.onSuccessResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error :: " + error);
                        }
                    }
            );
            VolleySingleton.getInstance(context).addToRequestQueue(getRequest);
        }

        private void subscribeCamp(final Campaigns campaigns, final SubscribeCallback subscribeCallback){
            final int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context, "user_id", true));
            StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.SubscribeCampaign,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            subscribeCallback.onSuccessResponse(response);
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
                    params.put("subscribed_by", String.valueOf(user_id));
                    params.put("subscribed_campaign", String.valueOf(campaigns.getCampaign_id()));
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(postRequest);
        }

        private void UnsubscribeCamp(final Campaigns campaigns, final UnSubscribeCallback unSubscribeCallback){
            final int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context, "user_id", true));
            StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.UnSubscribeCampaign,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            unSubscribeCallback.onSuccessResponse(response);
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
                    params.put("subscribed_by", String.valueOf(user_id));
                    params.put("subscribed_campaign", String.valueOf(campaigns.getCampaign_id()));
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(postRequest);
        }

    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position, Mcampaigns);
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Description";
                case 1:
                    return "About";
            }
            return null;
        }
    }

    private void setToolbar() {
        appBarLayout.setVisibility(View.GONE);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void resetToolbar() {
        appBarLayout.setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(RootToolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
    }

    @Override
    public void onStop() {
        super.onStop();
        resetToolbar();
    }

}
