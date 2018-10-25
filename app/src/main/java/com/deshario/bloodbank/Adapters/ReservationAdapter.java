package com.deshario.bloodbank.Adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.TransitionInflater;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Fragments.BarCodeViewDialog;
import com.deshario.bloodbank.Fragments.Campaign_View_Frag;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.Campaigns;
import com.deshario.bloodbank.Models.DayReservation;
import com.deshario.bloodbank.R;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<DayReservation> dayReservationList;
    private Context context;

    public ReservationAdapter(List<DayReservation> dayReservations) {
        this.dayReservationList = dayReservations;
    }

    @Override
    public int getItemCount() {
        return dayReservationList.size();
    }

    public DayReservation getItem(int position) {
        return dayReservationList.get(position);
    }

    @Override
    public void onBindViewHolder(final ReservationViewHolder holder, final int position) {
        final DayReservation dayReservation = dayReservationList.get(position);
        String MKey = dayReservation.getReservation_key();
        String Mday = Deshario_Functions.getCustomDate(dayReservation.getReserved_date(), 5);
        String Mmonth = Deshario_Functions.getCustomDate(dayReservation.getReserved_date(), 6);
        String MTime = Deshario_Functions.getCustomDate(dayReservation.getReserved_date(), 2);
        String loc_n_time = dayReservation.getBranch_name() + " | " + MTime;

        holder.day.setText(Mday);
        holder.month.setText(Mmonth);
        holder.reserver.setText(dayReservation.getUsername());
        holder.location.setText(loc_n_time);
        final String bar_url = "http://bwipjs-api.metafloor.com/?bcid=code128&text=" + MKey + "&parsefnc&alttext=" + MKey + "&scale=5";
        Glide.with(context)
                .load(bar_url)
                .apply(new RequestOptions()
                        .placeholder(R.color.dark1)
                        .dontAnimate()
                        .dontTransform())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarCodeViewDialog barCodeViewDialog = new BarCodeViewDialog.Builder()
                        .setDayReservation(dayReservation)
                        .setBarcodeurl(bar_url)
                        .build();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                barCodeViewDialog.show(fragmentManager, "MTAG");
            }
        });
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservation_card, viewGroup, false);
        context = viewGroup.getContext();
        return new ReservationViewHolder(itemView);
    }

    class ReservationViewHolder extends RecyclerView.ViewHolder {

        private TextView day;
        private TextView month;
        private TextView location;
        private TextView reserver;
        private ImageView imageView;

        ReservationViewHolder(View view) {
            super(view);
            day = (TextView) view.findViewById(R.id.day_name);
            month = (TextView) view.findViewById(R.id.month_name);
            reserver = (TextView) view.findViewById(R.id.reserved_by);
            location = (TextView) view.findViewById(R.id.reserved_loc);
            imageView = (ImageView) view.findViewById(R.id.barcodeimg);
        }

    }

    private void changefrag(ImageView imageView, Campaigns campaigns) {
        MainActivity.hidebottomnav();
        Campaign_View_Frag fragment = new Campaign_View_Frag();
        Bundle bundle = new Bundle();
        bundle.putParcelable("campaigns", campaigns);
        fragment.setArguments(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
            fragment.setEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
        }

        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                //.addSharedElement(imageView, "image")
                //.addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
                .replace(R.id.frame_container, fragment, MainActivity.TAG_CAMPAIGN_VIEW_FRAG)
                .addToBackStack(null)
                .commit();
    }
}