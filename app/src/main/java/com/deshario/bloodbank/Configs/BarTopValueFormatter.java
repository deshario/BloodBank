package com.deshario.bloodbank.Configs;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Deshario on 8/24/2017.
 */

public class BarTopValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public BarTopValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.00");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if(value > 0) {
            //return mFormat.format(value)+"%";
            return value+"%";
        } else {
            return "";
        }
    }
}
