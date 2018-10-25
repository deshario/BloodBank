package com.deshario.bloodbank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Fragments.BloodReqNavigateDialog;
import com.deshario.bloodbank.Models.BloodRequest;
import com.deshario.bloodbank.R;

import java.util.List;

public class BloodRequestdapter extends RecyclerView.Adapter<BloodRequestdapter.ContactViewHolder>{

    private List<BloodRequest> requests_lists;
    Context context;

    public BloodRequestdapter(List<BloodRequest> contactList) {
        this.requests_lists = contactList;
    }

    @Override
    public int getItemCount() {
        return requests_lists.size();
    }

    public BloodRequest getItem(int position) {
        return requests_lists.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, final int position) {
        final BloodRequest bloodRequest = requests_lists.get(position);
        contactViewHolder.Mtitle.setText(bloodRequest.getReq_key());

        String totaltext = manageCaption(bloodRequest);
        SpannableString data = new SpannableString(totaltext);
        //data.setSpan(new StyleSpan(Typeface.BOLD), text1.length(), totaltext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_place_black_24dp);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM){
            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint){
                Drawable b = getDrawable();
                canvas.save();
                int transY = bottom - b.getBounds().bottom;
                transY -= paint.getFontMetricsInt().descent / 2; // this is the key
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            }
        };
        //data.setSpan(imageSpan, text1.length(), text1.length()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        String Mdatetime = Deshario_Functions.getCustomDate(bloodRequest.getCreated(),3); // 15 Jan, 13:45
        contactViewHolder.Mdesc.setText(data);
        contactViewHolder.Mdate.setText(Mdatetime);
        contactViewHolder.Mdonors.setText(bloodRequest.getNum_donors()+" Donors");

        contactViewHolder.Mnavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                BloodReqNavigateDialog awesomeDialogFragment = new BloodReqNavigateDialog.Builder()
                        .setBloodRequest(bloodRequest)
                        .build();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                awesomeDialogFragment.show(fragmentManager, "MTAG");
            }
        });

        contactViewHolder.Mshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String data = bloodRequest.getReq_key()+" needs "+bloodRequest.getBlood_group()+
                            " blood group at "+bloodRequest.getLocation_name()+".";
                    String info = " Please share this to your friends to help him/her.";
            }
        });
    }

    private String manageCaption(BloodRequest bloodRequest){

        // I need 3 amounts of AB+ bloodgroup for reason at HospitalName || BOTH TRUE
        // I need 3 amounts of AB+ bloodgroup at HospitalName
        // I need AB+ bloodgroup at HospitalName

        int temp_b_amount = bloodRequest.getBlood_amount();
        int temp_p_amount = bloodRequest.getPaid_amount();
        int temp_left_amount = temp_b_amount-temp_p_amount;
        String temp_b_group = bloodRequest.getBlood_group();
        String temp_reason = bloodRequest.getReason();
        String temp_location = bloodRequest.getLocation_name();
        String text = null;

        //Log.i("MYLOG","place :: "+temp_location+" || bloodamount :: "+temp_p_amount+" || reason :: "+temp_reason);

        String left_amount = "";
        String reason = "";
        String blood_group = temp_b_group+" blood group";

        if(temp_b_amount != 0)
            left_amount = temp_left_amount+" units of ";

        if(!TextUtils.isEmpty(temp_reason)){
            reason = " for "+temp_reason;
        }
        text = "I need "+left_amount+blood_group+reason+" at "+temp_location;
        return text;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bloodrequest_cardview, viewGroup, false);
        context = viewGroup.getContext();
        return new ContactViewHolder(itemView);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView Mtitle;
        private TextView Mdesc;
        private TextView Mdate;
        private TextView Mdonors;
        private Button Mnavigate;
        private Button Mshare;

        public ContactViewHolder(View view) {
            super(view);
            Mtitle =  (TextView) view.findViewById(R.id.Mtitle);
            Mdesc =  (TextView) view.findViewById(R.id.description);
            Mdate =  (TextView) view.findViewById(R.id.date);
            Mdonors =  (TextView) view.findViewById(R.id.num_donors);
            Mnavigate =  (Button) view.findViewById(R.id.btn_navigate);
            Mshare =  (Button) view.findViewById(R.id.btn_share);
            //Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            //Mtitle.setTypeface(boldTypeface);
        }
    }

    private void share(View view, String data){
        System.out.println("data :: "+data);
        //Uri imageUri = Uri.parse("http://eofdreams.com/data_images/dreams/face/face-03.jpg");
        Uri imageUri = Uri.parse("android.resource://" + view.getContext().getPackageName() + "/drawable/" + "blood_launcher");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, data);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        view.getContext().startActivity(Intent.createChooser(shareIntent, "send"));
    }

}