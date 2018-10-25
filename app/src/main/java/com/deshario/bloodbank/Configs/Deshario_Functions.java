package com.deshario.bloodbank.Configs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Deshario on 10/24/2017.
 */

public class Deshario_Functions {

    public static Drawable setTint(Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    public static String getUserinfo(Context context, String requried, boolean IsInt){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", MODE_PRIVATE);
        if(IsInt){
            return String.valueOf(sharedPreferences.getInt(requried, 0));
        }else{
            return String.valueOf(sharedPreferences.getString(requried, ""));
        }
    }

    public static Drawable setWhiteTint(Drawable drawable) {
        final Drawable MDrawable = drawable;
        //MDrawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        MDrawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        return MDrawable;
    }

    public static String getCustomDate(String date, int type){
        String thai_date = date;
        String[] date_time = thai_date.split(" ");
        String[] _date = date_time[0].split("-");
        String[] _time = date_time[1].split(":");
        int _year = Integer.valueOf(_date[0]);
        int _month = Integer.valueOf(_date[1]);
        int _day = Integer.valueOf(_date[2]);
        int _hour = Integer.valueOf(_time[0]);
        int _min = Integer.valueOf(_time[1]);
        int _sec = Integer.valueOf(_time[2]);
        String month_ = getMonths(_month,false);
        int year_ = _year;
        thai_date = _day+" "+month_+" "+year_;

        String dateOnly = add_zero_or_not(_day)+" "+month_; // Type 1
        String timeOnly = add_zero_or_not(_hour)+":"+add_zero_or_not(_min); // Type 2
        String both = add_zero_or_not(_day)+" "+month_+", "+add_zero_or_not(_hour)+":"+add_zero_or_not(_min); // Type 3
        String credit_card = add_zero_or_not(_day)+"/"+add_zero_or_not(_month); // Type 4
        String day_no = add_zero_or_not(_day); // Type 5
        String month_name = month_; // Type 6
        String customizedDate = null;
        switch (type){
            case 1:
                customizedDate = dateOnly;
                break;
            case 2:
                customizedDate = timeOnly;
                break;
            case 3:
                customizedDate = both;
                break;
            case 4:
                customizedDate = credit_card;
                break;
            case 5: // Day No
                customizedDate = day_no;
                break;
            case 6: // Month Short Name
                customizedDate = month_name;
                break;
        }
        return customizedDate;
    }

    public static String getSplitedDateOrTimeOnly(String Mdate,int type){
        final int dateOnly = 1;
        final int timeOnly = 2;
        String[] date_time = Mdate.split(" ");
        String date = date_time[0]; //2018-06-18
        String time = date_time[1]; //22:00:35
        if(type == dateOnly){
            return date;
        }else{
            return time;
        }
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String today = Deshario_Functions.getSplitedDateOrTimeOnly(dateFormat.format(date),1);
        return today;
    }

    public static String add_zero_or_not(int number){
        String num = null;
        if(number < 10){
            num = "0"+number;
            return num;
        }else{
            num = ""+number;
            return num;
        }
    }

    public static String getMonths(int month, boolean full) {
        String[] en_months_short = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        String[] en_months_long = new String[]{
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };
        if(full == true){
            return en_months_long[month-1];
        }else{
            return en_months_short[month-1];
        }
    }

    public static void showToast(Context context, String Message){
        Toast.makeText(context,Message, Toast.LENGTH_SHORT).show();
    }

   public static String getDecimalFormat(double value){
       DecimalFormat mFormat = new DecimalFormat("###,###,##0.00");
       String formatted = mFormat.format(value);
       return formatted;
   }

}
