package com.deshario.bloodbank.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.BranchRequest;
import com.deshario.bloodbank.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BranchRequest_View_Frag extends Fragment{

    private AppBarLayout appBarLayout;
    private Toolbar RootToolbar;
    private Context context;

    public BranchRequest_View_Frag(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.branch_request_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RootToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        ImageView imageView = (ImageView) view.findViewById(R.id.maps_demo);
        FloatingActionButton navigate_btn = (FloatingActionButton) view.findViewById(R.id.fab_navigate);

        ImageView ico1 = (ImageView) view.findViewById(R.id.icon1);
        ImageView ico2 = (ImageView) view.findViewById(R.id.icon2);
        ImageView ico3 = (ImageView) view.findViewById(R.id.icon3);
        ImageView ico4 = (ImageView) view.findViewById(R.id.icon4);
        ImageView ico5 = (ImageView) view.findViewById(R.id.icon5);

        TextView title_1 = (TextView) view.findViewById(R.id.title1);
        TextView title_2 = (TextView) view.findViewById(R.id.title2);
        TextView title_3 = (TextView) view.findViewById(R.id.title3);
        TextView title_4 = (TextView) view.findViewById(R.id.title4);
        TextView title_5 = (TextView) view.findViewById(R.id.title5);

        TextView desc1 = (TextView) view.findViewById(R.id.description1);
        TextView desc2 = (TextView) view.findViewById(R.id.description2);
        TextView desc3 = (TextView) view.findViewById(R.id.description3);
        TextView desc4 = (TextView) view.findViewById(R.id.description4);
        TextView desc5 = (TextView) view.findViewById(R.id.description5);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            final BranchRequest branchRequest = bundle.getParcelable("branchrequests");
            final String apikey = context.getResources().getString(R.string.google_maps_key);
            Glide.with(context)
                    .load(WEBAPI.map_static+branchRequest.getBranch_lat_long()+WEBAPI.map_style_uber+WEBAPI.map_params+apikey)
                    .apply(new RequestOptions()
                            .placeholder(R.color.dark1)
                            .centerCrop()
                            .error(R.color.mat_red)
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // load into cache
                            .dontAnimate()
                            .dontTransform())
                    .into(imageView);

            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.setTitle(branchRequest.getBranch_name());
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                    MainActivity.restoreBottomNav();
                }
            });

            navigate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BranchReqNavigateDialog branchReqNavigateDialog = new BranchReqNavigateDialog.Builder()
                            .setBranchRequest(branchRequest)
                            .build();
                    FragmentManager fragmentManager = getFragmentManager();
                    branchReqNavigateDialog.show(fragmentManager, "MTAG");
                }
            });

            ico1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vpn_key_white_36dp));
            ico1.setRotation(270);
            title_1.setText(branchRequest.getBranch_code());
            desc1.setText("Branch Code");

            ico2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pin_drop_white_36dp));
            title_2.setText(branchRequest.getBranch_address());
            desc2.setText("Branch Address");

            ico3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_invert_colors_white_36dp));
            title_3.setText(branchRequest.getBlood_group());
            desc3.setText("BloodGroup");

            int actual_amount = branchRequest.getBlood_amount()-branchRequest.getPaid_amount();
            ico4.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_colorize_white_36dp));
            title_4.setText(""+Deshario_Functions.add_zero_or_not(actual_amount)+" Units");
            desc4.setText("Blood Amount");

            String date = Deshario_Functions.getCustomDate(branchRequest.getCreated(),3);
            ico5.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_today_white_36dp));
            title_5.setText(date);
            desc5.setText("Created");
        }
    }

    private void setToolbar() {
        appBarLayout.setVisibility(View.GONE);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void resetToolbar() {
        appBarLayout.setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(RootToolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
    }

    @Override
    public void onStop() {
        super.onStop();
        resetToolbar();
    }
}
