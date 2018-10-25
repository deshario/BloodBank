package com.deshario.bloodbank.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.transition.TransitionInflater;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.deshario.bloodbank.Configs.WEBAPI;
import com.deshario.bloodbank.Fragments.Campaign_View_Frag;
import com.deshario.bloodbank.MainActivity;
import com.deshario.bloodbank.Models.Campaigns;
import com.deshario.bloodbank.R;

import java.util.List;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder> {

    private List<Campaigns> campaigns;
    private Context context;

    public CampaignAdapter(List<Campaigns> campaigns) {
        this.campaigns = campaigns;
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    public Campaigns getItem(int position) {
        return campaigns.get(position);
    }

    @Override
    public void onBindViewHolder(final CampaignViewHolder holder, final int position){
        final Campaigns my_campaigns = campaigns.get(position);
        String camp_name = my_campaigns.getCampaign_name();
        String creator = my_campaigns.getCampaign_creator();
        String camp_created = my_campaigns.getCampaign_created();
        String camp_subscribers = my_campaigns.getCampaign_joined();

        SpannableString str = new SpannableString(creator+" had created "+camp_name);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, creator.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new StyleSpan(Typeface.BOLD), creator.length()+12, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.title.setText(str);
        holder.date.setText(camp_created);
        Glide.with(context)
                .load(WEBAPI.CampaignImgPath+campaigns.get(position).getCampaign_img())
                .apply(new RequestOptions()
                        .placeholder(R.color.dark1)
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform())
                .into(holder.imageView);
        //ViewCompat.setTransitionName(holder.imageView, "simple_fragment_transition");

        holder.MCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                changefrag(holder.imageView,my_campaigns);
            }
        });

    }

    @Override
    public CampaignViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.campaign_cardview, viewGroup, false);
        context = viewGroup.getContext();
        return new CampaignViewHolder(itemView);
    }

    public class CampaignViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView date;
        private ImageView imageView;
        private CardView MCard;

        public CampaignViewHolder(View view) {
            super(view);
            MCard = (CardView) view.findViewById(R.id.card_view);
            title = (TextView) view.findViewById(R.id.post_title);
            date = (TextView) view.findViewById(R.id.post_date);
            imageView = (ImageView) view.findViewById(R.id.coverImageView);
        }

    }

    private void changefrag(ImageView imageView, Campaigns campaigns){
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
                //.addSharedElement(imageView, "image")
                //.addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
                .replace(R.id.frame_container, fragment,MainActivity.TAG_CAMPAIGN_VIEW_FRAG)
                .addToBackStack(null)
                .commit();
    }
}