package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deshario.bloodbank.Configs.BarBottomValueFormatter;
import com.deshario.bloodbank.Configs.BarTopValueFormatter;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.Report_BloodGroup;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class Report_BloodGroup_Frag extends Fragment {

    private ListView listView;
    private ImageButton Moptmenu;
    private TextView Mreport_title;
    private List<Report_BloodGroup> reports;
    private ChartDataAdapter chartDataAdapter;
    private Context context;
    private int TYPE_BLOOD_AMOUNT = 1;
    private int TYPE_REQUESTED_TIMES = 2;

    public Report_BloodGroup_Frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.report_bloodgroup_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.mListview);
        Mreport_title = (TextView) view.findViewById(R.id.report_title);
        Moptmenu = (ImageButton) view.findViewById(R.id.opt_menu);
        Moptmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                PopupMenu popup = new PopupMenu(getActivity(), view,Gravity.RIGHT);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.report_bloodgroup_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener());
            }
        });
        fetch();
    }

    private class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu1:
                    extractData(reports, TYPE_REQUESTED_TIMES);
                    return true;
                case R.id.menu2:
                    extractData(reports,TYPE_BLOOD_AMOUNT);
                    return true;
            }
            return false;
        }
    }

    private class ChartDataAdapter extends ArrayAdapter<BarData> {

        private List<Report_BloodGroup> report_bloodGroups;

        public ChartDataAdapter(Context context, List<BarData> objects, List<Report_BloodGroup> bloodGroupList){
            super(context, 0, objects);
            this.report_bloodGroups = bloodGroupList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.report_bloodgroup_chart, null);
                holder.chart = convertView.findViewById(R.id.chart);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Bardata {Top Bar Values}
            BarData data = getItem(position);
            //data.setValueTextColor(Color.RED);
            data.setValueTextSize(12); // top value text
            //data.setValueTypeface(mTfLight);

            holder.chart.getDescription().setEnabled(false);
            holder.chart.setDrawGridBackground(false);
            //holder.chart.getLegend().setEnabled(false);

            // Bottom
            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //xAxis.setTypeface(mTfLight);
            xAxis.setDrawGridLines(false);
            xAxis.setTextSize(12); // bottom text
            //xAxis.setAxisMaximum(3);
            xAxis.setValueFormatter(new BarBottomValueFormatter(report_bloodGroups));
            xAxis.setGranularity(1f);
            xAxis.setCenterAxisLabels(false);
            xAxis.setAxisMinimum(data.getXMin()-1f);
            xAxis.setAxisMaximum(data.getXMax()+1f);

            // Left
            YAxis leftAxis = holder.chart.getAxisLeft();
            //leftAxis.setTypeface(mTfLight);
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMaximum(100);


            // Right
            YAxis rightAxis = holder.chart.getAxisRight();
            //rightAxis.setEnabled(false);
            //rightAxis.setTypeface(mTfLight);
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMaximum(100);

            // set data
            holder.chart.setData(data);
            holder.chart.setFitBars(true);

            // Don't forget to refresh the chart
            //holder.chart.invalidate();
            holder.chart.animateY(700);

            return convertView;
        }

        private class ViewHolder {

            BarChart chart;
        }
    }

    private void fetch() {
        //RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, WEBAPI.Get_Bloodgroup_Report, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        reports = parseJsonFeed(response);
                        extractData(reports,TYPE_REQUESTED_TIMES);
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
        VolleySingleton.getInstance(context).addToRequestQueue(getRequest);
    }

    private List<Report_BloodGroup> parseJsonFeed(JSONObject response) {
        List<Report_BloodGroup> report_bloodGroups;
        Report_BloodGroup report = new Report_BloodGroup();
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        report_bloodGroups = report.setDatafromJson(jsonArray);
        return report_bloodGroups;
    }

    private void extractData(List<Report_BloodGroup> reports, int type){
        ModifyTitle(type);
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        float total = 0;
        for (int j = 0; j < reports.size(); j++) {
            String temp_blood = reports.get(j).getBlood_name();
            float temp_total;
            if(type == TYPE_REQUESTED_TIMES){
                temp_total = reports.get(j).getRequested_times();
            }else{
                temp_total = reports.get(j).getBlood_amount();
            }
            total = total + temp_total;
        }

        for (int i = 0; i < reports.size(); i++){
            if(type == TYPE_REQUESTED_TIMES){
                float temp = reports.get(i).getRequested_times()*100/total;
                entries.add(new BarEntry(i,ModifyValue(temp)));
            }else{
                float temp = reports.get(i).getBlood_amount()*100/total;
                entries.add(new BarEntry(i,ModifyValue(temp)));
            }
        }
        BarDataSet barDataSet = new BarDataSet(entries, "BGroup");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setBarShadowColor(Color.rgb(203, 203, 203));

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new BarTopValueFormatter()); // bardata top value
        barData.setBarWidth(0.6f);

        ArrayList<BarData> barDataArrayList = new ArrayList<BarData>();
        barDataArrayList.add(barData);
        chartDataAdapter = new ChartDataAdapter(context, barDataArrayList,reports);
        listView.setAdapter(chartDataAdapter);
        chartDataAdapter.notifyDataSetChanged();
    }

    private float ModifyValue(float value){
        String pattern = "###,###.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return Float.valueOf(decimalFormat.format(value));
    }

    private void ModifyTitle(int type){
        if(type == TYPE_REQUESTED_TIMES){
            Mreport_title.setText("MOST REQ TIMES");
        }else{
            Mreport_title.setText("MOST REQ AMOUNT");
        }
    }
}
