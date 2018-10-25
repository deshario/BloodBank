package com.deshario.bloodbank.Adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.transition.TransitionInflater;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Fragments.Campaign_View_Frag;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.Campaigns;
import com.deshario.bloodbank.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Deshario on 1/11/2018.
 */

public class CampSliderAdapter extends LoopingPagerAdapter<Campaigns>{

    private Context mContext;
    private List<Campaigns> mCampaigns;
    private View mView;
    TextView description;
    ImageView imageView;

    public CampSliderAdapter(Context mContext, List<Campaigns> campaigns, boolean isInfinite) {
        super(mContext, (ArrayList<Campaigns>) campaigns, isInfinite);
        this.mContext = mContext;
        this.mCampaigns = campaigns;
    }

    @Override
    protected int getItemViewType(int listPosition) {
        return 1;
    }

    @Override
    protected View inflateView(int viewType, int listPosition) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pager, null);
        mView = view;
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType){
        //description = convertView.findViewById(R.id.description);
        imageView = convertView.findViewById(R.id.image);

        final Campaigns my_campaigns = mCampaigns.get(listPosition);
        Glide.with(mContext).load(WEBAPI.CampaignImgPath+my_campaigns.getCampaign_img()).into(imageView);
        //description.setText(my_campaigns.getCampaign_name());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                changefrag(my_campaigns);
            }
        });
    }

    private void changefrag(Campaigns campaigns){
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
                .replace(R.id.frame_container, fragment,MainActivity.TAG_CAMPAIGN_VIEW_FRAG)
                .addToBackStack(null)
                .commit();
    }

//    private void openCampaign(Campaigns campaigns){
//        MainActivity.hidebottomnav();
//        Campaign_Details_Frag fragment = new Campaign_Details_Frag();
//        Bundle bundle = new Bundle();
//        //bundle.putParcelable("campaigns", campaigns);
//        fragment.setArguments(bundle);
//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fragment.setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
//            fragment.setEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
//        }
//        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frame_container, fragment,MainActivity.TAG_CAMPAIGN_VIEW_FRAG)
//                .addToBackStack(null)
//                .commit();
//    }

//    private void view(final Campaigns campaign){
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.campaign_view, null);
//        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
//        alert.setView(view);
//        alert.setCancelable(true);
//        final AlertDialog dialog = alert.create();
//        dialog.getWindow().setDimAmount(0.9f);
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//        TextView title,description,enrolled;
//        ImageView camp_pic;
//        ImageButton camp_close;
//        final Button btn_subscribe,btn_invite;
//
//        title = (TextView)view.findViewById(R.id.camp_title);
//        description = (TextView)view.findViewById(R.id.camp_desc);
//        enrolled = (TextView)view.findViewById(R.id.enrolled);
//        camp_pic = (ImageView)view.findViewById(R.id.camp_img);
//        camp_close = (ImageButton)view.findViewById(R.id.close_modal);
//        btn_subscribe = (Button)view.findViewById(R.id.btn_subscribe);
//        btn_invite = (Button)view.findViewById(R.id.btn_invite);
//
//        title.setText(campaign.getCampaign_name());
//        description.setText(campaign.getCampaign_desc());
//        //getSubscribers(enrolled,campaign.getCampaign_id());
//
//        Glide.with(context)
//                .load(WEBAPI.CampaignImgPath+campaign.getCampaign_img())
//                //.centerCrop()
//                //.placeholder(R.drawable.ic_arrow_forward_black_24dp)
//                .placeholder(R.color.dark1)
//                .error(R.color.mat_red)
//                .diskCacheStrategy(DiskCacheStrategy.ALL) // load into cache
//                .crossFade()
//                .into(camp_pic);
//
//        camp_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        btn_subscribe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SQLiteHandler db = new SQLiteHandler(context);
//                HashMap<String, String> user = db.getUserDetails(); // Fetching user details from sqlite
//                String u_id = user.get("u_id");
//                //subscribe(btn_subscribe,u_id,campaign);
//                Toast.makeText(context,"u_id :: "+u_id+"\n CampId :: "+campaign.getCampaign_id(), Toast.LENGTH_LONG).show();
//            }
//        });
//        btn_invite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Campaigns Mcamp = campaign;
//                String invite_text = "Hey Buddy ! Common Let join '"+Mcamp.getCampaign_name()+"' blood donation campaign to save million lives.";
//                Uri imageUri = Uri.parse("android.resource://" + view.getContext().getPackageName() + "/drawable/" + "blood_launcher");
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, invite_text);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                shareIntent.setType("image/jpeg");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                view.getContext().startActivity(Intent.createChooser(shareIntent, "send"));
//            }
//        });
//
//    }

    private void subscribe(final Button joinbtn, final String u_id, final Campaigns campaigns){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.GET_SAVED_BRANCH_REQ,
                new Response.Listener<String>()    {
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject res = new JSONObject(response);
                            int status = (int) res.get("success");
                            if(status == 1){
                                Toast.makeText(context,"Subscribe Success", Toast.LENGTH_SHORT).show();
                                joinbtn.setText("Subscribed");
                            }else if(status == 2){
                                Toast.makeText(context,"Already Subscribed", Toast.LENGTH_SHORT).show();
                                joinbtn.setText("Subscribed");
                            }else{
                                Toast.makeText(context,"Subscribe Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("err : "+error);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(u_id));
                params.put("camp_id", String.valueOf(campaigns.getCampaign_id()));
                params.put("subscribed_date", campaigns.getCampaign_created());
                return params;
            }
        };
        queue.add(postRequest);
    }


//    private void getSubscribers(final TextView txt_subscribers, final int campid){
//        final String newurl = WEBAPI.CountCampaignSubscribers+campid;
//        StringRequest strReq = new StringRequest(Request.Method.GET,newurl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response){
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    String sub = jObj.getString("subscribers");
//                    txt_subscribers.setText(sub+" Enrolled");
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
//                txt_subscribers.setText("Subscribers : null");
//            }
//        });
//        //VolleySingleton.getInstance().addToRequestQueue(strReq, "getsubscribers");
//    }

}
