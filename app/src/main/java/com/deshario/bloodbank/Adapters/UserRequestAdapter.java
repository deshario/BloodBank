package com.deshario.bloodbank.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Models.UserRequest;
import com.deshario.bloodbank.R;

import java.util.List;

/**
 * Created by Deshario on 1/21/2018.
 */

public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.HistoryViewHolder> {

    private List<UserRequest> userRequestList;
    private Context context;
    private static int count = 0;

    public UserRequestAdapter(Context mcontext, List<UserRequest> lists){
        this.context = mcontext;
        this.userRequestList = lists;
    }

    @Override
    public int getItemCount() {
        return userRequestList.size();
    }

    public UserRequest getItem(int position) {
        return userRequestList.get(position);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, final int position){
        final UserRequest userRequest = userRequestList.get(position);
        //Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        //holder.title.setTypeface(boldTypeface);
        //holder.location.setTypeface(boldTypeface);
        String requested_Date = userRequest.getRequested_date();

        holder.title.setText("Request "+userRequest.getBlood_group()+" group");
        holder.location.setText(userRequest.getLocation_name());
        String date = Deshario_Functions.getSplitedDateOrTimeOnly(userRequest.getRequested_date(),1);
        if(date.equals(Deshario_Functions.getDateTime())){
            holder.req_date.setText(Deshario_Functions.getCustomDate(requested_Date,2));
        }else{
            holder.req_date.setText(Deshario_Functions.getCustomDate(requested_Date,1));
            holder.req_date.setTextColor(context.getResources().getColor(R.color.dark_gray_dark));
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listview, viewGroup, false);
        context = viewGroup.getContext();
        return new HistoryViewHolder(itemView);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView location;
        private TextView req_date;

        public HistoryViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            location = (TextView) view.findViewById(R.id.desc);
            req_date = (TextView) view.findViewById(R.id.timestamp);
        }

    }

}