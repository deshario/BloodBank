package com.deshario.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.deshario.bloodbank.Configs.SessionManager;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Fragments.UserRegistrationFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ProgressDialog mDialog,pDialog;
    Button login_btn;//continue_with_fb;
    TextView register_btn;
    String firebase_token;
    EditText user,pass;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        firebase_token = FirebaseInstanceId.getInstance().getToken();
        login_btn = (Button)findViewById(R.id.btn_login);
        //continue_with_fb = (Button)findViewById(R.id.fb_login);
        register_btn = (TextView)findViewById(R.id.sign_up);
        user = (EditText)findViewById(R.id.user_name);
        pass = (EditText)findViewById(R.id.pass);

        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Muser = user.getText().toString();
                final String Mpass = pass.getText().toString();
                if(Muser.isEmpty()){
                    user.setError("Please fill this field");
                }else if(Mpass.isEmpty()){
                    pass.setError("Please fill this field");
                }else if(firebase_token.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Token Error",Toast.LENGTH_SHORT).show();
                }else{
                    ManageLogin(Muser,Mpass,firebase_token);
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRegistrationFragment registrationFragment = new UserRegistrationFragment.Builder()
                        .setFacebookID(null)
                        .setUsername(null)
                        .setToken(firebase_token)
                        .build();
                FragmentManager fragmentManager = ((AppCompatActivity)LoginActivity.this).getSupportFragmentManager();
                registrationFragment.show(fragmentManager, "MTAG");
            }
        });

//        continue_with_fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","email"));
////                AccessToken accessToken = AccessToken.getCurrentAccessToken();
////                if(accessToken != null){
////                    LoginManager.getInstance().logOut();
////                }
//            }
//        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Loading");
                mDialog.show();

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                        manageLogin(object);
                    }
                });
                Bundle params = new Bundle();
                params.putString("fields","id,name,email,birthday,friends");
                graphRequest.setParameters(params);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("Login cancel");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("FacebookException error : "+error);
            }
        });

    }

    private void ManageLogin(final String user, final String pass, final String token){
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Validating Login...");
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String data = jsonObject.getString("data");
                            if (status == "true") {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                storeUser(data);
                            } else {
                                Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("virtual_user", user);
                params.put("virtual_password", pass);
                params.put("profile_token", token);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void storeUser(String temp_data){
        try {
            JSONObject data = new JSONObject(temp_data);
            String username = data.getString("username");
            int user_id = data.getInt("u_id");
            int blood_group = data.getInt("blood_group");
            String phone = data.getString("phone");
            String created = data.getString("created");
            String profile_token = data.getString("profile_token");

            SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("user_id", user_id);
            editor.putString("username", username);
            editor.putInt("blood_group", blood_group);
            editor.putString("phone", phone);
            editor.putString("created", created);
            editor.putString("profile_token", profile_token);
            editor.apply();

            session.setLogin(true);
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void manageLogin(JSONObject jsonObject){
        try {
            //username:str, blood_group:int, phone:number, virtual_password:str, profile_token:token
            URL pic = new URL("https://graph.facebook.com/"+jsonObject.getString("id")+"/picture?width=250&height=250");
            System.out.println("pic == "+pic);

            String fb_id = jsonObject.getString("id");
            String username = jsonObject.getString("name");
            String token = FirebaseInstanceId.getInstance().getToken();

            System.out.println("1111 READY TO REGISTER");
            System.out.println("1111 FBID = "+fb_id);
            System.out.println("1111 Username = "+username);
            System.out.println("1111 Token = "+token);

            UserRegistrationFragment registrationFragment = new UserRegistrationFragment.Builder()
                    .setFacebookID(fb_id)
                    .setUsername(username)
                    .setToken(token)
                    .build();
            FragmentManager fragmentManager = ((AppCompatActivity)LoginActivity.this).getSupportFragmentManager();
            registrationFragment.show(fragmentManager, "MTAG");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
