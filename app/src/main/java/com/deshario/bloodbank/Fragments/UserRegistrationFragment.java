package com.deshario.bloodbank.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.BloodTypes;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRegistrationFragment extends DialogFragment {

    private static final String KEY_MESSAGE = "key_message";
    private static final String KEY_POSITIVE = "key_positive";
    private static final String KEY_NEGATIVE = "key_negative";
    private static final String KEY_FB_ID = "key_fbid";
    private static final String KEY_USER = "key_user";
    private static final String KEY_TOKEN = "key_token";
    private int checked_blood = -1;
    private List<BloodTypes> bloodTypesList;

    private TextView et_username;
    //private TextView et_phone;
    private RadioGroup bloodRadio;
    private LinearLayout linearLayout;
    private TextView et_pass,et_confirm_pass;
    private TextView sign_in;
    private CheckBox MCheckbox;
    private Button Mnavigate;
    private Button btn_register;
    private ProgressDialog pDialog;
    private ImageButton btnDismiss;
    private Context context;

    private int message;
    private int positive;
    private int negative;
    private String facebook_id;
    private String username;
    private String token;

    public static class Builder {
        private int message;
        private int positive;
        private int negative;
        private String facebook_id;
        private String username;
        private String token;

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

        public Builder setFacebookID(String fb_id) {
            this.facebook_id = fb_id;
            return this;
        }

        public Builder setUsername(String user_name) {
            this.username = user_name;
            return this;
        }

        public Builder setToken(String firebase_token) {
            this.token = firebase_token;
            return this;
        }

        public UserRegistrationFragment build() {
            return UserRegistrationFragment.newInstance(message, positive, negative, facebook_id, username, token);
        }

    }

    public static UserRegistrationFragment newInstance(@StringRes int message, @StringRes int positive, @StringRes int negative, String facebook_id, String username, String token) {
        UserRegistrationFragment fragment = new UserRegistrationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MESSAGE, message);
        bundle.putInt(KEY_POSITIVE, positive);
        bundle.putInt(KEY_NEGATIVE, negative);
        bundle.putString(KEY_FB_ID,facebook_id);
        bundle.putString(KEY_USER,username);
        bundle.putString(KEY_TOKEN,token);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        context = getActivity().getApplicationContext();
        if (savedInstanceState == null) {
            restoreArguments(getArguments());
        } else {
            restoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                //System.out.println("Back Dialog");
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_register, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT); //width,height
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setupView();
        fetchBloodTypes();
    }

    private void bindView(View view){
        et_username = (TextView) view.findViewById(R.id.Musername);
        //et_phone = (TextView) view.findViewById(R.id.Mphone);
        et_pass = (TextView) view.findViewById(R.id.Mpass);
        et_confirm_pass = (TextView) view.findViewById(R.id.MConfirmpass);
        sign_in = (TextView) view.findViewById(R.id.sign_in);
        btn_register = (Button) view.findViewById(R.id.register_now);
        linearLayout = (LinearLayout) view.findViewById(R.id.customRadioGroup);
        btnDismiss = (ImageButton) view.findViewById(R.id.btn_close);
    }

    private void setupView(){
        if(username != null){
            et_username.setText(username);
            et_username.setFocusableInTouchMode(false);
            et_username.setFocusable(false);
        }

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int blood_id = checked_blood;
                //final String phone = et_phone.getText().toString();
                final String phone = "";
                final String username = et_username.getText().toString();
                final String password = et_pass.getText().toString();
                final String confirm_password = et_confirm_pass.getText().toString();
                final String firebase_token = token;

                if (!validate(blood_id, username, password, confirm_password, firebase_token)) {
                    //Toast.makeText(context, "All fields Required !", Toast.LENGTH_SHORT).show();
                    String info = "Set error field";
                } else {
                    //Toast.makeText(context,"phone = "+phone+"\nUsername = "+username+"\nPassword = "+password+"\nConfirm Password = "+confirm_password+"\nFBID = "+firebase_token,Toast.LENGTH_LONG).show();
                    register(blood_id, phone, username, confirm_password, firebase_token);
                }
            }
        });
    }

    public boolean validate(int blood, String username, String password, String confirm_password, String firebase_token) {
        boolean valid = true;

//        if (phone.isEmpty()){
//            et_phone.setError("Invalid Phone Number");
//        }

        if (username.isEmpty()){
            et_username.setError("Please fill this field");
            valid = false;
        }

        if (password.isEmpty()){
            et_pass.setError("Please fill this field");
            valid = false;
        }

        if (confirm_password.isEmpty()){
            et_confirm_pass.setError("Please fill this field");
            valid = false;
        }

        if(!password.equals(confirm_password)){
            et_confirm_pass.setError("Please input the same password");
            valid = false;
        }

        if(firebase_token.isEmpty()){
            Toast.makeText(context,"Invalid Token",Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if(valid){ // All OK
            if (blood <= -1) {
                Toast.makeText(context,"Invalid Bloodgroup",Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        return valid;
    }

    private void register(final int blood_id, final String phone, final String username, final String confirm_password, final String firebase_token){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Registering...");
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status == "true") {
                                Toast.makeText(context, "Register Success. Please Login to Continue", Toast.LENGTH_SHORT).show();
                                checked_blood = -1;
                                bloodRadio.clearCheck();
                                dismissDialog();
                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(context, " " + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("blood_group", String.valueOf(blood_id));
                params.put("virtual_password", confirm_password);
                params.put("profile_token",firebase_token);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void dismissDialog(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null){
            LoginManager.getInstance().logOut();
        }
        dismiss();
    }

    private void createRadioButton(List<BloodTypes> bloodTypes){
        bloodRadio = new RadioGroup(context); //create the RadioGroup
        bloodRadio.setOrientation(RadioGroup.HORIZONTAL);//or RadioGroup.VERTICAL
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // Unchecked
                        new int[]{android.R.attr.state_checked} // Checked
                },
                new int[]{
                        getResources().getColor(R.color.new_material_red), // Unchecked Value
                        getResources().getColor(R.color.fbwhite) // Checked Value
                }
        );
        for (int i = 0; i < bloodTypes.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(bloodTypes.get(i).getBlood_id());
            radioButton.setText(bloodTypes.get(i).getBlood_name());
            radioButton.setTextSize(22);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setChecked(false);
            radioButton.setPadding(10, 10, 10, 10);
            radioButton.setBackground(context.getResources().getDrawable(R.drawable.circle_blood_type));
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setTypeface(Typeface.DEFAULT_BOLD);
            radioButton.setTextColor(colorStateList);
            if (i != 0) {
                LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 0, 0, 0);
                radioButton.setLayoutParams(layoutParams);
            }
            bloodRadio.addView(radioButton);
        }

        linearLayout.addView(bloodRadio);
        bloodRadio.setSelected(false);
        bloodRadio.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
    }

    private void fetchBloodTypes() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading");
        showDialog();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, WEBAPI.Get_BloodTypes, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        List<BloodTypes> bloodTypes = parseJsonFeed(response);
                        createRadioButton(bloodTypes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error : "+error,Toast.LENGTH_SHORT).show();
                        hideDialog();
                        dismissDialog();
                        System.out.println("error :: " + error);
                    }
                }
        );
        VolleySingleton.getInstance(context).addToRequestQueue(getRequest);
    }

    private List<BloodTypes> parseJsonFeed(JSONObject response) {
        List<BloodTypes> bloodTypesList;
        BloodTypes bloodType = new BloodTypes();
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bloodTypesList = bloodType.setDatafromJson(jsonArray);
        return bloodTypesList;
    }

    RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId){
            checked_blood = checkedId;
            System.out.println("Selected = "+checkedId);
        }};

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MESSAGE, message);
        outState.putInt(KEY_POSITIVE, positive);
        outState.putInt(KEY_NEGATIVE, negative);
        outState.putString(KEY_FB_ID,facebook_id);
        outState.putString(KEY_USER,username);
        outState.putString(KEY_TOKEN,token);
    }

    private void restoreInstanceState(Bundle bundle) {
        message = bundle.getInt(KEY_MESSAGE);
        positive = bundle.getInt(KEY_POSITIVE);
        negative = bundle.getInt(KEY_NEGATIVE);
        facebook_id = bundle.getString(KEY_FB_ID);
        username = bundle.getString(KEY_USER);
        token = bundle.getString(KEY_TOKEN);
    }

    private void restoreArguments(Bundle bundle) {
        message = bundle.getInt(KEY_MESSAGE);
        positive = bundle.getInt(KEY_POSITIVE);
        negative = bundle.getInt(KEY_NEGATIVE);
        facebook_id = bundle.getString(KEY_FB_ID);
        username = bundle.getString(KEY_USER);
        token = bundle.getString(KEY_TOKEN);
    }
}