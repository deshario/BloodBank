package com.deshario.bloodbank.Adapters;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.SessionManager;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Fragments.Reservation_Frag;
import com.deshario.bloodbank.Fragments.UsageRecords_Frag;
import com.deshario.bloodbank.Fragments.Verification_Tab_Frag;
import com.deshario.bloodbank.LoginActivity;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.MoreMenu;
import com.deshario.bloodbank.R;
import com.deshario.bloodbank.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Deshario on 1/21/2018.
 */

public class MoreMenuAdapter extends RecyclerView.Adapter<MoreMenuAdapter.MoreMenuViewHolder> {

    private Context context;
    private List<MoreMenu> moreMenus;
    private Fragment fragment = null;
    private String FRAG_TAG = null;
    private boolean anim = false;
    private SessionManager session;

    public MoreMenuAdapter(List<MoreMenu> moreMenuList) {
        this.moreMenus = moreMenuList;
    }

    @Override
    public int getItemCount() {
        return moreMenus.size();
    }

    public MoreMenu getItem(int position) {
        return moreMenus.get(position);
    }

    @Override
    public void onBindViewHolder(final MoreMenuViewHolder holder, final int position) {
        final MoreMenu moreMenu = moreMenus.get(position);
        holder.title.setText(moreMenu.getTitle());
        holder.desc.setText(moreMenu.getDesc());
        holder.imageView.setImageDrawable(moreMenu.getIcon());
    }

    @Override
    public MoreMenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.more_menu_item, viewGroup, false);
        context = viewGroup.getContext();
        session = new SessionManager(context);
        return new MoreMenuViewHolder(view);
    }

    public class MoreMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView desc;
        private CardView cardView;
        private ImageView imageView;
        private ProgressDialog pDialog;

        public MoreMenuViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.menu_title);
            desc = (TextView) view.findViewById(R.id.menu_desc);
            imageView = (ImageView) view.findViewById(R.id.listview_image);
            cardView = (CardView) view.findViewById(R.id.menu_card);
        }

        @Override
        public void onClick(View view) {
            int item_no = getLayoutPosition();
            switch (item_no) {
                case 0:
                    fragment = new Verification_Tab_Frag();
                    FRAG_TAG = MainActivity.TAG_VERIFICATION_FRAG;
                    anim = false;
                    break;
                case 1:
                    fragment = new UsageRecords_Frag();
                    FRAG_TAG = MainActivity.TAG_USAGE_TAB;
                    anim = false;
                    break;
                case 2:
                    fragment = new Reservation_Frag();
                    FRAG_TAG = MainActivity.TAG_PLAN_DONATION;
                    anim = true;
                    break;
                case 3:
                    logout();
                    break;
            }
            if (fragment != null) {
                if (anim == true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //fragment.setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.slide_right));
                        fragment.setEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.fade));
                    }
                }
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment, FRAG_TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

        private void logout() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Logout Confirmation !");
            builder.setMessage("You won't get any application notifications after logout.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    removeToken();
                }
            });

            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

        private void removeToken() {
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading");
            showDialog();
            final String username = Deshario_Functions.getUserinfo(context, "username", false);
            StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.URL_REMOVE_TOKEN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hideDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status == "true") {
                                    Toast.makeText(context, "Logout Success", Toast.LENGTH_SHORT).show();

                                    session.setLogin(false);
                                    session.clearAll();
                                    ((Activity) context).finish();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);

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
                    Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("virtual_user", String.valueOf(username));
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(postRequest);
        }

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

}