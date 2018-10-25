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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import com.deshario.bloodbank.Models.BloodRequest;
import com.deshario.bloodbank.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BloodReqNavigateDialog extends DialogFragment {

    private OnDialogListener onDialogListener;

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
    private Context context;

    private int message;
    private int positive;
    private int negative;
    private BloodRequest bloodRequest;

    public static class Builder {
        private int message;
        private int positive;
        private int negative;
        private BloodRequest bloodRequest;

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

        public Builder setBloodRequest(BloodRequest bloodRequest) {
            this.bloodRequest = bloodRequest;
            return this;
        }

        public BloodReqNavigateDialog build() {
            return BloodReqNavigateDialog.newInstance(message, positive, negative, bloodRequest);
        }

    }

    public static BloodReqNavigateDialog newInstance(@StringRes int message, @StringRes int positive, @StringRes int negative, BloodRequest request) {
        BloodReqNavigateDialog fragment = new BloodReqNavigateDialog();
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

    private void bindView(View view){
        Mtitle = (TextView) view.findViewById(R.id.M_title);
        Mdate = (TextView) view.findViewById(R.id.M_date);
        Mcaption = (TextView) view.findViewById(R.id.M_caption);
        MCheckbox = (CheckBox) view.findViewById(R.id.chk_insure);
        Mnavigate = (Button) view.findViewById(R.id.btn_navigate);
        btnDismiss = (ImageButton) view.findViewById(R.id.btn_close);
    }

    private void setupView(){
        int required = bloodRequest.getBlood_amount();
        int paid = bloodRequest.getPaid_amount();
        int total = required - paid;
        String Mdatetime = Deshario_Functions.getCustomDate(bloodRequest.getCreated(),3); // 15 Jan, 13:45
        Mtitle.setText(bloodRequest.getReq_key());
        Mcaption.setText(manageCaption(bloodRequest));
        Mdate.setText(Mdatetime);

        Mnavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MCheckbox.isChecked()) {
                    Toast.makeText(getActivity(), "Please insure that this action is true.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), "req_key : "+bloodRequest.getReq_key(), Toast.LENGTH_SHORT).show();
//                    OnDialogListener listener = getOnDialogListener();
//                    if (listener != null) {
//                        listener.onNavigateButtonClicked();
//                    }
                    saveRequest(bloodRequest);
                    navigate(bloodRequest.getLat_long());
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

    private void navigate(String address){ // address is latlong
        Uri location = Uri.parse("google.navigation:q="+address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,location);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }else{
            Toast.makeText(context,"Google Maps Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRequest(final BloodRequest bloodRequest){
        final int user_id = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.SAVE_BLOOD_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        //hideDialog();
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
                params.put("request_id", String.valueOf(bloodRequest.getId()));
                params.put("saved_by", String.valueOf(user_id));
                return params;
            }
        };
        queue.add(postRequest);


        //request_id:int, donated_by:int, donated_to:int, paid_amount:int
    }

    private String manageCaption(BloodRequest bloodRequest) {
        int temp_b_amount = bloodRequest.getBlood_amount();
        int temp_p_amount = bloodRequest.getPaid_amount();
        int temp_left_amount = temp_b_amount - temp_p_amount;
        String temp_b_group = bloodRequest.getBlood_group();
        String temp_reason = bloodRequest.getReason();
        String temp_location = bloodRequest.getLocation_name();
        String text = null;

        //Log.i("MYLOG","place :: "+temp_location+" || bloodamount :: "+temp_p_amount+" || reason :: "+temp_reason);

        String left_amount = "";
        String reason = "";
        String blood_group = temp_b_group + " blood group";

        if (temp_b_amount != 0)
            left_amount = temp_left_amount + " units of ";

        if (!TextUtils.isEmpty(temp_reason)) {
            reason = " for " + temp_reason;
        }
        text = "I need " + left_amount + blood_group + reason + " at " + temp_location;
        return text;
    }

    public void setOnDialogListener(OnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }

    public interface OnDialogListener {
        void onNavigateButtonClicked();
    }

    private OnDialogListener getOnDialogListener() {
        Fragment fragment = getParentFragment();
        try {
            if (fragment != null) {
                return (OnDialogListener) fragment;
            } else {
                return (OnDialogListener) getActivity();
            }
        } catch (ClassCastException ignored) {
            Log.i("MYLOG","Error :: "+ignored);
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MESSAGE, message);
        outState.putInt(KEY_POSITIVE, positive);
        outState.putInt(KEY_NEGATIVE, negative);
        outState.putParcelable(KEY_REQUEST, bloodRequest);
    }

    private void restoreInstanceState(Bundle bundle) {
        message = bundle.getInt(KEY_MESSAGE);
        positive = bundle.getInt(KEY_POSITIVE);
        negative = bundle.getInt(KEY_NEGATIVE);
        bloodRequest = bundle.getParcelable(KEY_REQUEST);
    }

    private void restoreArguments(Bundle bundle) {
        message = bundle.getInt(KEY_MESSAGE);
        positive = bundle.getInt(KEY_POSITIVE);
        negative = bundle.getInt(KEY_NEGATIVE);
        bloodRequest = bundle.getParcelable(KEY_REQUEST);
    }
}