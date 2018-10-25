package com.deshario.bloodbank.Fragments;


import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.deshario.bloodbank.Adapters.BranchSelectAdapter;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.Branch;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reservation_Create extends Fragment implements View.OnClickListener {

    private EditText Mtime, Mnotes, Mbranch;
    private Context context;
    private Button btn_reserve;
    private int branch_id;
    private Calendar selected_date;
    private ProgressDialog pDialog;

    public Reservation_Create() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_donation_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pDialog = new ProgressDialog(getActivity());

        final CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarView);

        Date currentTime = Calendar.getInstance().getTime();
        try {
            calendarView.setDate(currentTime);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DATE, -1);
        calendarView.setMinimumDate(min);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                selected_date = eventDay.getCalendar();
            }
        });

        Mbranch = (EditText) view.findViewById(R.id.Mbranches);
        Mtime = (EditText) view.findViewById(R.id.M_time);
        Mnotes = (EditText) view.findViewById(R.id.MNotes);
        btn_reserve = (Button) view.findViewById(R.id.reserve_now);
        btn_reserve.setOnClickListener(this);

        Mbranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchBranches();
            }
        });

        Mtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTimePickerFragment newFragment = new MyTimePickerFragment();
                newFragment.setListener(timeSetListener);
                newFragment.show(getFragmentManager(), "time picker");
            }
        });
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    final String selected_time = Deshario_Functions.add_zero_or_not(hourOfDay) + ":" + Deshario_Functions.add_zero_or_not(minute); //19:26
                    Mtime.setText(selected_time);
                }
            };

    private void AdapBranches(final List<Branch> branchList) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View Mview = inflater.inflate(R.layout.branch_selector_main, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(Mview);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setDimAmount(0.9f);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

//        // Get screen width and height in pixels
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        // The absolute width of the available display size in pixels.
//        int displayWidth = displayMetrics.widthPixels;
//        int displayHeight = displayMetrics.heightPixels;
//
//        // Initialize a new window manager layout parameters
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(dialog.getWindow().getAttributes()); // Copy the alert dialog window attributes to new layout parameter instance
//
//        int dialogWindowWidth = (int) (displayWidth * 0.9f); // Set alert dialog width equal to screen width 70%
//        int dialogWindowHeight = (int) (displayHeight * 0.7f); // Set alert dialog height equal to screen height 70%
//        layoutParams.width = dialogWindowWidth;
//        layoutParams.height = dialogWindowHeight;
//
//        // Apply the newly created layout parameters to the alert dialog window
//        dialog.getWindow().setAttributes(layoutParams);

        ListView listView = (ListView) Mview.findViewById(R.id.list_view);
        Button btn_submit = (Button) Mview.findViewById(R.id.btn_select);

        final BranchSelectAdapter adapter = new BranchSelectAdapter(context, branchList, true);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        dialog.show();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                final Branch branch = adapter.getSelectedItem();
                if (branch == null) {
                    Toast.makeText(context, "Please Select Any Branch", Toast.LENGTH_SHORT).show();
                } else {
                    Mbranch.setText(branch.getBranch_name());
                    branch_id = branch.getBranch_id();
                }
            }
        });
    }

    private void fetchBranches(){
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, WEBAPI.GetAllBranches, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Branch> branchList = parseJsonFeed(response);
                        AdapBranches(branchList);
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error :: " + error);
                        pDialog.hide();
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

    private List<Branch> parseJsonFeed(JSONObject response) {
        List<Branch> branchList;
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        branchList = Branch.setDatafromJson(jsonArray);
        return branchList;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.reserve_now:
                sendReserve();
                break;
            default:
        }
    }

    private void sendReserve() {
        final String time = Mtime.getText().toString();
        final String notes = Mnotes.getText().toString();

        if (selected_date == null || branch_id == 0 || time.isEmpty() || notes.isEmpty()) {
            Toast.makeText(context, "All Fields are required !", Toast.LENGTH_SHORT).show();
        } else {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = format1.format(selected_date.getTime());
            String date_time = formatted+" "+time+":00";
            System.out.println("date_time :: "+date_time);
            int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
            sendBroadcast(user_id,branch_id,date_time,notes);
        }
    }

    private void resetContent(){
        Mtime.setText("");
        Mnotes.setText("");
        Mbranch.setText("");
    }

    private void sendBroadcast(final int u_id, final int branch_id, final String reserved_date, final String note) {
        pDialog.setMessage("Sending Reservation...");
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.CreateReservation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status == "true"){
                                resetContent();
                                Toast.makeText(context, "Reservation Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(u_id));
                params.put("branch_id", String.valueOf(branch_id));
                params.put("reserved_date",reserved_date);
                params.put("user_notes", note);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
