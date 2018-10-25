package com.deshario.bloodbank.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import com.deshario.bloodbank.R;

import java.util.Calendar;

public class MyTimePickerFragment extends DialogFragment{

    private TimePickerDialog.OnTimeSetListener mListener;
    private Context context;

    public void setListener(TimePickerDialog.OnTimeSetListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //int sec = c.get(Calendar.SECOND);
        //return new TimePickerDialog(getActivity(), timeSetListener, hour, minute, DateFormat.is24HourFormat(getActivity()));
        return new TimePickerDialog(getActivity(), R.style.DialogTheme, mListener, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

//    private TimePickerDialog.OnTimeSetListener timeSetListener =
//            new TimePickerDialog.OnTimeSetListener() {
//                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                    String time = Deshario_Functions.add_zero_or_not(hourOfDay) + ":" + Deshario_Functions.add_zero_or_not(minute) + ":00"; //19:26:58
//                }
//            };
}