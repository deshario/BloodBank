package com.deshario.bloodbank.Configs;

/**
 * Created by Deshario on 8/24/2017.
 */

import com.deshario.bloodbank.Models.Report_BloodGroup;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class BarBottomValueFormatter implements IAxisValueFormatter {

    private List<Report_BloodGroup> report_bloodGroups;

    public BarBottomValueFormatter(List<Report_BloodGroup> report_bloodGroups) {
        this.report_bloodGroups = report_bloodGroups;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String stringValue;
        int pos = ((int) value);
        if (report_bloodGroups.size() >= 0 && value >= 0) { // If reports and index(value) is not empty
            if (pos < report_bloodGroups.size()) { // If index < reports || for(i=0 i<size(); i++)
                stringValue = report_bloodGroups.get(pos).getBlood_name();
            } else {
                stringValue = "";
            }
        }else {
            stringValue = "";
        }
        return stringValue;
    }
}