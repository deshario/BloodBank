package com.deshario.bloodbank.Adapters;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Fragments.BranchRequest_View_Frag;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.BranchRequest;
import com.deshario.bloodbank.R;

import java.util.List;


/**
 * Created by Deshario on 1/21/2018.
 */

public class BranchRequestAdapter extends RecyclerView.Adapter<BranchRequestAdapter.BranchRequestViewHolder> {

    private List<BranchRequest> branchRequestList;
    private Context context;

    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(int position); // create callback function
    }

    public BranchRequestAdapter(List<BranchRequest> campaigns, ListAdapterListener mListener) {
        this.branchRequestList = campaigns;
        this.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        return branchRequestList.size();
    }

    public BranchRequest getItem(int position) {
        return branchRequestList.get(position);
    }

    @Override
    public void onBindViewHolder(final BranchRequestViewHolder holder, final int position) {
        final BranchRequest branchRequest = branchRequestList.get(position);
        String branch_name = branchRequest.getBranch_name();
        String distance = Deshario_Functions.getDecimalFormat(branchRequest.getTemp_distance());
        String date = Deshario_Functions.getCustomDate(branchRequest.getCreated(),1);
        holder.title.setText(branch_name);
        holder.date.setText(date+" "+context.getResources().getString(R.string.middle_dot)+" "+distance+" km far away");
        holder.MCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public class BranchRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private TextView date;
        private ImageButton imageButton;
        private CardView MCard;

        public BranchRequestViewHolder(View view) {
            super(view);
            MCard = (CardView) view.findViewById(R.id.branch_card);
            title = (TextView) view.findViewById(R.id.post_title);
            date = (TextView) view.findViewById(R.id.post_date);
            imageButton = (ImageButton) view.findViewById(R.id.branch_btn);
            MCard.setOnClickListener(this);
            imageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clicked = view.getId();
            int item_no = getLayoutPosition();
            BranchRequest branchRequest = getItem(item_no);
            if(clicked == R.id.branch_card){
//                System.out.println("Branch Card");
//                Polyline polyLine;
//                ArrayList <LatLng> list = new ArrayList();
//                list.add(new LatLng(40.692331, -73.952920));
//                list.add(new LatLng(50.692331, -83.952920));
//                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
//                for (int z = 0; z < list.size(); z++) {
//                    LatLng point = list.get(z);
//                    options.add(point);
//                }
//                polyLine = mGoogleMap.addPolyline(options);
            }
            if(clicked == R.id.branch_btn){
                Fragment fragment = new BranchRequest_View_Frag();
                String FRAG_TAG = MainActivity.TAG_BRANCH_REQ_VIEW_FRAG;
                if(fragment != null){
                    MainActivity.hidebottomnav();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("branchrequests", branchRequest);
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment,FRAG_TAG);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        }
    }

    @Override
    public BranchRequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.branchrequest_cardview, viewGroup, false);
        context = viewGroup.getContext();
        return new BranchRequestViewHolder(itemView);
    }

    private String calculateDistance(BranchRequest branchRequest){
        String[] latLng = branchRequest.getBranch_lat_long().split(",");
        double latitude = Double.parseDouble(latLng[0]);
        double longitude = Double.parseDouble(latLng[1]);

        Location locationStart = new Location("RMUTL NAN"); // 18.804914,100.788363
        locationStart.setLatitude(18.804914);
        locationStart.setLongitude(100.788363);

        Location locationEnd = new Location(branchRequest.getBranch_name());
        locationEnd.setLatitude(latitude);
        locationEnd.setLongitude(longitude);

        double distance = locationStart.distanceTo(locationEnd)/1000;
        return Deshario_Functions.getDecimalFormat(distance)+" km";
    }


}