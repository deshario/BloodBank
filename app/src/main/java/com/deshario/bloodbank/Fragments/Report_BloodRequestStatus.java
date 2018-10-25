package com.deshario.bloodbank.Fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.deshario.bloodbank.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Report_BloodRequestStatus extends Fragment {

    private PieChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    public Report_BloodRequestStatus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.report_req, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //tvX = findViewById(R.id.tvXMax);
        //tvY = findViewById(R.id.tvYMax);

//        mSeekBarX = findViewById(R.id.seekBar1);
//        mSeekBarY = findViewById(R.id.seekBar2);
//        mSeekBarX.setProgress(4);
//        mSeekBarY.setProgress(10);

        mChart = view.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        //mChart.setExtraOffsets(5, 10, 5, 5);
        //moveOffScreen();

        mChart.setDragDecelerationFrictionCoef(0.95f);

        //mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        //mChart.setHoleRadius(58f);
        //mChart.setTransparentCircleRadius(61f);

        mChart.setHoleRadius(40f);
//        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);

        //setData(5, 100);
        String[] mList = new String[] {
                "A+", "B+", "AB+", "AB-", "O+", "O-"
        };
        setData(mList, 100);

        //mChart.animateY(1400, Easing.EaseInOutQuad);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //mChart.spin(2000, 0, 360, Easing.EasingOption.EaseInBack);

//        mSeekBarX.setOnSeekBarChangeListener(this);
        //mSeekBarY.setOnSeekBarChangeListener(this);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(false);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        //mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

        // hide y values
//        for (IDataSet<?> set : mChart.getData().getDataSets())
//            set.setDrawValues(!set.isDrawValuesEnabled());
//        mChart.invalidate();
    }


    //private void setData(int count, float range) {
    private void setData(String dataSset[], float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        NumberFormat formatter = new DecimalFormat("#0.00");
        System.out.println(formatter.format(4.0));

        for (int i = 0; i < dataSset.length ; i++) {
            entries.add(new PieEntry(
                    (int) ((Math.random() * mult) + mult / 5),
                    dataSset[i],
                    getResources().getDrawable(R.mipmap.ic_launcher)
            ));
        }


//        for (int i = 0; i < count ; i++) {
////            entries.add(new PieEntry(i,"AB+"));
//            entries.add(i,new PieEntry(120, mParties[i % mParties.length], getResources().getDrawable(R.drawable.star)));
//        }

        PieDataSet dataSet = new PieDataSet(entries, "");


        // old PieDataSet dataSet = new PieDataSet(entries, "Election Results");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(1f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.material_red));
        colors.add(getResources().getColor(R.color.material_primary));
        colors.add(getResources().getColor(R.color.material_success));
        colors.add(getResources().getColor(R.color.info_bootstrap));
//        }

        // colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        //SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        SpannableString s = new SpannableString("Blood\ngroups");
        s.setSpan(new RelativeSizeSpan(1.3f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

}
