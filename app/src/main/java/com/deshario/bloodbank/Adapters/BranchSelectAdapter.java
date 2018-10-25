package com.deshario.bloodbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.deshario.bloodbank.Models.Branch;
import com.deshario.bloodbank.R;

import java.util.List;


public class BranchSelectAdapter extends BaseAdapter {
    private Context context;
    private List<Branch> branches;
    private LayoutInflater inflater;
    private boolean isListView;
    private int selectedPosition = -1;
    private int sel_position;

    public BranchSelectAdapter(Context context, List<Branch> arrayList, boolean isListView) {
        this.context = context;
        this.branches = arrayList;
        this.isListView = isListView;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return branches.size();
    }

    @Override
    public Object getItem(int i) {
        return branches.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            //inflate the layout on basis of boolean
            if (isListView)
                view = inflater.inflate(R.layout.branch_selector_cardview, viewGroup, false);
            else
                // view = inflater.inflate(R.layout.grid_custom_row_layout, viewGroup, false);
                view = inflater.inflate(R.layout.branch_selector_cardview, viewGroup, false);

            viewHolder.label = (TextView) view.findViewById(R.id.label);
            viewHolder.radioButton = (RadioButton) view.findViewById(R.id.radio_button);
            view.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.label.setText(branches.get(i).getBranch_name());

        //check the radio button if both position and selectedPosition matches
        viewHolder.radioButton.setChecked(i == selectedPosition);

        //Set the position tag to both radio button and label
        viewHolder.radioButton.setTag(i);
        viewHolder.label.setTag(i);

        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }
        });

        viewHolder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }
        });

        return view;
    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
        sel_position = selectedPosition;
    }

    private class ViewHolder {
        private TextView label;
        private RadioButton radioButton;
    }

    // Return the selectedPosition item
    public Branch getSelectedItem() {
        if (selectedPosition != -1) {
            return branches.get(sel_position);
        }
        return null;
    }

    public int getCurrent(){
        if (selectedPosition != -1) {
            return sel_position;
        }else{
            //Toasty.info(context, "กรุณาเลือกรายการใดรายการหนึ่ง", Toast.LENGTH_SHORT).show();
        }
        return -1;
    }
}
