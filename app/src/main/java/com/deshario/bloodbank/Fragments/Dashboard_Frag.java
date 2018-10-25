package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.asksira.loopingviewpager.LoopingViewPager;
import com.deshario.bloodbank.Adapters.BloodRequestdapter;
import com.deshario.bloodbank.Adapters.CampSliderAdapter;
import com.deshario.bloodbank.Adapters.MyFragmentPageAdapter;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.BloodRequest;
import com.deshario.bloodbank.Models.Campaigns;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.rd.PageIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard_Frag extends Fragment {

    LoopingViewPager viewPager;
    ViewPager mChartViewPager;
    CampSliderAdapter adapter;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    PageIndicatorView indicatorView;
    NestedScrollView nestedScrollView;
    SwipeRefreshLayout mSwipeDashboard;
    MyFragmentPageAdapter mPageAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    private CoordinatorLayout rootLayout;
    LinearLayout SliderLayout;
    private Context context;
    View dMview;

    public Dashboard_Frag() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_frag, container, false);
        setDefaultWhiteDrawable();
        setHasOptionsMenu(true);
        setToolbar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(context);
        rootLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        SliderLayout = (LinearLayout) view.findViewById(R.id.SliderCol);
        nestedScrollView = view.findViewById(R.id.main_scroller);
        indicatorView = view.findViewById(R.id.indicator);
        viewPager = view.findViewById(R.id.viewpager);
        mChartViewPager = (ViewPager) view.findViewById(R.id.chartviewpager);
        mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);


        recyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        recyclerView.setNestedScrollingEnabled(false);
        mSwipeDashboard = view.findViewById(R.id.refresh_dashboard);
        mSwipeDashboard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                work();
                //ViewCompat.setNestedScrollingEnabled(recyclerView, false);
                //nestedScrollView.scrollTo(0, 0);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm2);

        work();
    }

    private void work(){
        getCampSlider();
        makeReport();
        getData();
    }

    private void makeReport() {
        List<Fragment> fragments = buildFragments();
        mPageAdapter = new MyFragmentPageAdapter(context, getChildFragmentManager(), fragments);
        mChartViewPager.setAdapter(mPageAdapter);
    }

    private List<android.support.v4.app.Fragment> buildFragments() {
        List<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();
        //fragments.add(new Report_BloodRequestStatus());
        fragments.add(new Report_BloodGroup_Frag());
        return fragments;
    }

    private void setToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(" Dashboard");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_req_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_req_btn:
                changefrag(new BloodRequest_Create(), MainActivity.TAG_CREATE_REQ_FRAG);
                break;
            default:
                break;
        }
        return true;
    }

    private void changefrag(Fragment fragment, String CustomTAG) {
        if (fragment != null) {
            MainActivity.hidebottomnav();
            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.slide_top));
                fragment.setEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.slide_top));
            }
            fragmentTransaction.replace(R.id.frame_container, fragment, CustomTAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void setDefaultWhiteDrawable() {
        Deshario_Functions.setWhiteTint(getResources().getDrawable(R.drawable.ic_edit_white_24dp));
    }

    private void getCampSlider() {
        //RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest campJsonObject = new JsonObjectRequest(Request.Method.GET, WEBAPI.GetAllCampaigns, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Campaigns> camps = parseCampJson(response);
                        final List<Campaigns> listCamps = new ArrayList<>();
                        for (int i = 0; i < camps.size(); i++) {
                            Campaigns campaigns = camps.get(i);
                            listCamps.add(campaigns);
                        }
                        adapter = new CampSliderAdapter(getActivity(), listCamps, true);
                        final int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            SliderLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gradient));
                        } else {
                            SliderLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient));
                        }
                        viewPager.setAdapter(adapter);
                        indicatorView.setCount(viewPager.getIndicatorCount());

                        final int colors[] = {
                                context.getResources().getColor(R.color.mat_red),
                                context.getResources().getColor(R.color.material_primary),
                                context.getResources().getColor(R.color.success_bootstrap),
                                context.getResources().getColor(R.color.material_purple),
                                context.getResources().getColor(R.color.yellow),
                        };

                        viewPager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
                            @Override
                            public void onIndicatorProgress(int selectingPosition, float progress) {
                                indicatorView.setProgress(selectingPosition, progress);
                            }

                            @Override
                            public void onIndicatorPageChange(int newIndicatorPosition) {
                                //indicatorView.setSelection(newIndicatorPosition);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error :: " + error);
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
        campJsonObject.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(campJsonObject);
    }

    private List<Campaigns> parseCampJson(JSONObject response) {
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

    private void getData() {
        Log.i("MYLOG", "Request Getting");
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest bloodJsonObject = new JsonObjectRequest(Request.Method.GET, WEBAPI.GetAllRequests, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = null;
                        try {
                            status = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (status == "true") {
                            List<BloodRequest> bloodRequests = parseJsonFeed(response);
                            BloodRequestdapter feedAdapter = new BloodRequestdapter(bloodRequests);
                            recyclerView.setAdapter(feedAdapter);
                            rootLayout.setBackgroundColor(context.getResources().getColor(R.color.fbgrey));
                            if (mSwipeDashboard.isRefreshing()) {
                                mSwipeDashboard.setRefreshing(false);
                            }
                            stopShimmer();
                            //int resId = R.anim.anim_main;
                            //LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                            //recyclerView.setLayoutAnimation(animation);
                        } else {
                            Toast.makeText(getActivity(), "API FAILED", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error :: " + error);
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
        bloodJsonObject.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        VolleySingleton.getInstance(context).addToRequestQueue(bloodJsonObject);
    }

    private List<BloodRequest> parseJsonFeed(JSONObject response) {
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

    public void stopShimmer() {
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    public void startShimmer() {
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onResume(){
        //Log.i("MYLOG","ONRESUME");
        mShimmerViewContainer.startShimmerAnimation();
        super.onResume();
        setToolbar();
        setDefaultWhiteDrawable();
    }

    @Override
    public void onPause() {
        //Log.i("MYLOG","ONPAUSE");
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
        setDefaultWhiteDrawable();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

}
