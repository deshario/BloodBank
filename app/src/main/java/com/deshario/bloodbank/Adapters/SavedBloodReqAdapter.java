package com.deshario.bloodbank.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Models.SavedBloodRequest;
import com.deshario.bloodbank.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedBloodReqAdapter extends RecyclerView.Adapter<SavedBloodReqAdapter.SavedBloodReqViewHolder> {

    private List<SavedBloodRequest> savedBloodRequestList;
    private Context context;
    private ProgressDialog pDialog;

    public SavedBloodReqAdapter(List<SavedBloodRequest> savedBloodRequests) {
        this.savedBloodRequestList = savedBloodRequests;
    }

    @Override
    public int getItemCount() {
        return savedBloodRequestList.size();
    }

    public SavedBloodRequest getItem(int position) {
        return savedBloodRequestList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final SavedBloodReqViewHolder holder, final int position) {
        final SavedBloodRequest savedBloodRequest = savedBloodRequestList.get(position);

        holder.key.setText(savedBloodRequest.getReq_key());
        holder.loc.setText(savedBloodRequest.getLocation_name());
        holder.blood.setText(savedBloodRequest.getBlood_name());

        String Mday = Deshario_Functions.getCustomDate(savedBloodRequest.getSaved_date(), 5);
        String Mmonth = Deshario_Functions.getCustomDate(savedBloodRequest.getSaved_date(), 6);

        holder.day.setText(Mday);
        holder.month.setText(Mmonth);

        final int remain = savedBloodRequest.getBlood_amount() - savedBloodRequest.getPaid_amount();

        if (remain <= 0) {
            holder.status.setText("Closed");
            holder.verify_req_btn.setEnabled(false);
        } else {
            holder.status.setText("Open");
            holder.status.setTextColor(context.getResources().getColor(R.color.material_primary));
            holder.verify_req_btn.setEnabled(true);
        }

        holder.amount.setText(String.valueOf(remain) + " Unit");

        String apikey = context.getResources().getString(R.string.google_maps_key);

        String url = WEBAPI.map_static + savedBloodRequest.getLat_long() +
                WEBAPI.map_style_uber +
                //WEBAPI.map_addmarker+savedBloodRequest.getLat_long()+
                WEBAPI.map_new_params + "17" + // zoom
                WEBAPI.map_size + "200x200" +
                WEBAPI.map_key + apikey;

        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.color.dark1)
                        .dontAnimate()
                        .dontTransform())
                .into(holder.map_view);
    }

    @NonNull
    @Override
    public SavedBloodReqViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_bloodreq_cardview, viewGroup, false);
        context = viewGroup.getContext();
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Sending Verification...");
        return new SavedBloodReqViewHolder(itemView);
    }

    public class SavedBloodReqViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView key, loc, amount, blood, day, month, status;
        private Button verify_req_btn;
        private ImageView map_view;


        public SavedBloodReqViewHolder(View view) {
            super(view);
            map_view = (ImageView) view.findViewById(R.id.temp_map);
            key = (TextView) view.findViewById(R.id.req_key);
            blood = (TextView) view.findViewById(R.id.bloodgroup);
            status = (TextView) view.findViewById(R.id.req_status);
            amount = (TextView) view.findViewById(R.id.req_amount);
            day = (TextView) view.findViewById(R.id.day_name);
            month = (TextView) view.findViewById(R.id.month_name);
            verify_req_btn = (Button) view.findViewById(R.id.verify_blood_req);
            loc = (TextView) view.findViewById(R.id.req_location);
            verify_req_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SavedBloodRequest savedBloodRequest = getItem(getAdapterPosition());
            askPaidAmount(savedBloodRequest);
        }
    }

    private void askPaidAmount(final SavedBloodRequest savedBloodRequest) {
        final int default_amount = savedBloodRequest.getBlood_amount() - savedBloodRequest.getPaid_amount();
        System.out.println("paid default amount = "+default_amount);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setSingleLine();
        FrameLayout container = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dp20);
        params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp20);
        params.topMargin = context.getResources().getDimensionPixelSize(R.dimen.dp5);
        input.setLayoutParams(params);
        container.addView(input);

        input.setLayoutParams(params);

        dialogBuilder.setTitle("Enter Paid Amount");

        dialogBuilder.setView(container);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String paid = input.getText().toString();
                if(paid.isEmpty()){
                    Toast.makeText(context,"Invalid amount !",Toast.LENGTH_SHORT).show();
                }else{
                    final int paid_amount = Integer.valueOf(paid);
                    if(paid_amount <= 0){
                        Toast.makeText(context,"Invalid amount !",Toast.LENGTH_SHORT).show();
                    }else if(paid_amount > default_amount){
                        Toast.makeText(context,"Can't pay more than required amount.",Toast.LENGTH_SHORT).show();
                    }else{
                        create_verification(savedBloodRequest,paid_amount);
                    }
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void create_verification(final SavedBloodRequest savedBloodRequest, final int paid){
        final int reqid = savedBloodRequest.getBloodreq_id();
        final int donated_by = Integer.valueOf(Deshario_Functions.getUserinfo(context,"user_id",true));
        final int donated_to = savedBloodRequest.getRequester_id();

        showDialog();
        RequestQueue queue = Volley.newRequestQueue(context);
        System.out.println("verification :: "+WEBAPI.Create_Blood_Verification);
        StringRequest postRequest = new StringRequest(Request.Method.POST, WEBAPI.Create_Blood_Verification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status == "true"){
                                //resetContent();
                                Toast.makeText(context, "Verfication Sent! Please wait for approval", Toast.LENGTH_SHORT).show();
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
                hideDialog();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("request_id", String.valueOf(reqid));
                params.put("donated_by", String.valueOf(donated_by));
                params.put("donated_to", String.valueOf(donated_to));
                params.put("paid_amount", String.valueOf(paid));
                return params;
            }
        };
        queue.add(postRequest);


        //request_id:int, donated_by:int, donated_to:int, paid_amount:int
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