package com.deshario.bloodbank.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.BranchRequest;
import com.deshario.bloodbank.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BranchReqNavigateDialog extends DialogFragment {

    private static final String KEY_MESSAGE = "key_message";
    private static final String KEY_POSITIVE = "key_positive";
    private static final String KEY_NEGATIVE = "key_negative";
    private static final String KEY_REQUEST = "key_request";

    private TextView Mtitle;
    private TextView Mcaption;
    private TextView Mdate;
    private CheckBox MCheckbox;
    private Button Mnavigate;
    private Button btnInvite;
    private ImageButton btnDismiss;
    private ImageView imageView;
    private Context context;

    private int message;
    private int positive;
    private int negative;
    private BranchRequest branchRequest;

    public static class Builder {
        private int message;
        private int positive;
        private int negative;
        private BranchRequest branchRequest;

        public Builder() {
        }

        public Builder setMessage(@StringRes int message) {
            this.message = message;
            return this;
        }

        public Builder setPosition(@StringRes int positive) {
            this.positive = positive;
            return this;
        }

        public Builder setNegative(@StringRes int negative) {
            this.negative = negative;
            return this;
        }

        public Builder setBranchRequest(BranchRequest branchRequest) {
            this.branchRequest = branchRequest;
            return this;
        }

        public BranchReqNavigateDialog build() {
            return BranchReqNavigateDialog.newInstance(message, positive, negative, branchRequest);
        }

    }

    public static BranchReqNavigateDialog newInstance(@StringRes int message, @StringRes int positive, @StringRes int negative, BranchRequest request) {
        BranchReqNavigateDialog fragment = new BranchReqNavigateDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MESSAGE, message);
        bundle.putInt(KEY_POSITIVE, positive);
        bundle.putInt(KEY_NEGATIVE, negative);
        bundle.putParcelable(KEY_REQUEST, request);
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
        return inflater.inflate(R.layout.bloodrequest_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //getDialog().getWindow().getAttributes().alpha = 0.9f;
            getDialog().getWindow().setDimAmount(0.9f);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //width,height
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setupView();
    }

    private void bindView(View view) {
        Mtitle = (TextView) view.findViewById(R.id.M_title);
        Mdate = (TextView) view.findViewById(R.id.M_date);
        Mcaption = (TextView) view.findViewById(R.id.M_caption);
        MCheckbox = (CheckBox) view.findViewById(R.id.chk_insure);
        Mnavigate = (Button) view.findViewById(R.id.btn_navigate);
        imageView = (ImageView) view.findViewById(R.id.image);
        btnDismiss = (ImageButton) view.findViewById(R.id.btn_close);
    }

    private void setupView(){
        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pin_drop_white_36dp));
        String Mdatetime = Deshario_Functions.getCustomDate(branchRequest.getCreated(), 3); // 15 Jan, 13:45
        Mtitle.setText(branchRequest.getBranch_name());
        int actual_amount = branchRequest.getBlood_amount()-branchRequest.getPaid_amount();
        Mcaption.setText("We need "+Deshario_Functions.add_zero_or_not(actual_amount)+" units of "+branchRequest.getBlood_group()+" bloodgroup");
        Mdate.setText(Mdatetime);

        Mnavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MCheckbox.isChecked()) {
                    Toast.makeText(getActivity(), "Please insure that this action is true.", Toast.LENGTH_SHORT).show();
                } else {
                    saveRequest(branchRequest);
                    navigate(branchRequest.getBranch_lat_long());
                    dismiss();
                }
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void saveRequest(final BranchRequest branchRequest){
        final int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.SAVE_BRANCH_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status == "true"){
                                Toast.makeText(context, "Request Saved for verfication", Toast.LENGTH_SHORT).show();
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
                System.out.println("error :: "+error);
                //hideDialog();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("requests_id", String.valueOf(branchRequest.getBranchrequest_id()));
                params.put("saved_by", String.valueOf(user_id));
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void navigate(String address){ // address is latlong
        Uri location = Uri.parse("google.navigation:q="+address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,location);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            System.out.println("loc :: "+location);
            context.startActivity(mapIntent);
        }else{
            Toast.makeText(context,"Google Maps Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MESSAGE, message);
        outState.putInt(KEY_POSITIVE, positive);
        outState.putInt(KEY_NEGATIVE, negative);
        outState.putParcelable(KEY_REQUEST, branchRequest);
    }

    private void restoreInstanceState(Bundle bundle) {
        message = bundle.getInt(KEY_MESSAGE);
        positive = bundle.getInt(KEY_POSITIVE);
        negative = bundle.getInt(KEY_NEGATIVE);
        branchRequest = bundle.getParcelable(KEY_REQUEST);
    }

    private void restoreArguments(Bundle bundle) {
        message = bundle.getInt(KEY_MESSAGE);
        positive = bundle.getInt(KEY_POSITIVE);
        negative = bundle.getInt(KEY_NEGATIVE);
        branchRequest = bundle.getParcelable(KEY_REQUEST);
    }
}