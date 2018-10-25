package com.deshario.bloodbank.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Models.UserDonation;
import com.deshario.bloodbank.R;

import java.util.List;


/**
 * Created by Deshario on 1/21/2018.
 */

public class UserDonationAdapter extends RecyclerView.Adapter<UserDonationAdapter.HistoryViewHolder> {

    private List<UserDonation> userDonationList;
    private Context context;
    static int count = 0;

    public UserDonationAdapter(Context mcontext, List<UserDonation> histories){
        this.context = mcontext;
        this.userDonationList = histories;
    }

    @Override
    public int getItemCount() {
        return userDonationList.size();
    }

    public UserDonation getItem(int position) {
        return userDonationList.get(position);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, final int position){
        final UserDonation userDonation = userDonationList.get(position);
        //Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        //holder.title.setTypeface(boldTypeface);
        //holder.location.setTypeface(boldTypeface);

        String donated_date = userDonation.getDonated_date();

        holder.title.setText("Donated "+userDonation.getBlood_group()+" group");
        holder.location.setText(userDonation.getLocation_name());
        String date = Deshario_Functions.getSplitedDateOrTimeOnly(userDonation.getDonated_date(),1);
        if(date.equals(Deshario_Functions.getDateTime())){
            holder.donation_date.setText(Deshario_Functions.getCustomDate(donated_date,2));
        }else{
            holder.donation_date.setText(Deshario_Functions.getCustomDate(donated_date,1));
            holder.donation_date.setTextColor(context.getResources().getColor(R.color.dark_gray_dark));
        }
        holder.Micon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_white_36dp));
        holder.Micon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_success));
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listview, viewGroup, false);
        context = viewGroup.getContext();
        return new HistoryViewHolder(itemView);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView location;
        private TextView donation_date;
        private ImageView Micon;

        public HistoryViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            location = (TextView) view.findViewById(R.id.desc);
            donation_date = (TextView) view.findViewById(R.id.timestamp);
            Micon = (ImageView) view.findViewById(R.id.listview_image);
        }

    }

}