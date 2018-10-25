package com.deshario.bloodbank.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.deshario.bloodbank.Models.DayReservation;
import com.deshario.bloodbank.R;

public class BarCodeViewDialog extends DialogFragment {

    private static final String KEY_RESERVATION = "key_reservation";
    private static final String KEY_BARCODE = "key_barcode";

    private TextView Mtitle;
    private ImageView barcode;
    private ImageButton dismiss_btn;
    private DayReservation dayReservation;
    private String barcode_url;
    private Context context;

    public static class Builder {
        private DayReservation dayReservation;
        private String barcode_url;

        public Builder() {

        }

        public Builder setDayReservation(DayReservation dayReservation) {
            this.dayReservation = dayReservation;
            return this;
        }

        public Builder setBarcodeurl(String url) {
            this.barcode_url = url;
            return this;
        }

        public BarCodeViewDialog build() {
            return BarCodeViewDialog.newInstance(dayReservation,barcode_url);
        }

    }

    public static BarCodeViewDialog newInstance(DayReservation dayReservation, String barcode) {
        BarCodeViewDialog fragment = new BarCodeViewDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_RESERVATION, dayReservation);
        bundle.putString(KEY_BARCODE, barcode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        if (savedInstanceState == null) {
            restoreArguments(getArguments());
        } else {
            restoreInstanceState(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reservation_view_barcode, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setDimAmount(0.9f);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //width,height
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setupView();
    }

    private void bindView(View view){
        Mtitle = (TextView) view.findViewById(R.id.name);
        barcode = (ImageView) view.findViewById(R.id.img);
        dismiss_btn = (ImageButton) view.findViewById(R.id.btn_close);
    }

    private void setupView(){
        Mtitle.setText(dayReservation.getBranch_name());
        Glide.with(context)
                .load(barcode_url)
                .apply(new RequestOptions()
                        .placeholder(R.color.dark1)
                        .dontAnimate()
                        .dontTransform())
                .into(barcode);

        dismiss_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RESERVATION, dayReservation);
        outState.putString(KEY_BARCODE, barcode_url);
    }

    private void restoreInstanceState(Bundle bundle) {
        dayReservation = bundle.getParcelable(KEY_RESERVATION);
        barcode_url = bundle.getString(KEY_BARCODE);
    }

    private void restoreArguments(Bundle bundle) {
        dayReservation = bundle.getParcelable(KEY_RESERVATION);
        barcode_url = bundle.getString(KEY_BARCODE);
    }
}