package com.fammeo.app.adapter.fammeoAdapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.EditEmail;
import com.fammeo.app.model.EmailModel;

import java.util.ArrayList;

public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.ViewHolder> {
    Context context;
    ArrayList<EmailModel> emailList;
    boolean img;
    public EmailListAdapter(Context applicationContext, ArrayList<EmailModel> emailList) {
        this.context = applicationContext;
        this.emailList = emailList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.email_item_list,parent,false);
        return new EmailListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.txt_emailtype.setText(emailList.get(i).emailType);
        holder.txt_email.setText(emailList.get(i).emailAddress);
        if(img == true){
            holder.img_edt_email.setVisibility(View.VISIBLE);
            holder.img_edt_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emInt = new Intent(context, EditEmail.class);
                    emInt.putExtra("ET",emailList.get(i).emailType);
                    emInt.putExtra("EA",emailList.get(i).emailAddress);
                    emInt.putExtra("eId",emailList.get(i).recordId);
                    emInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(emInt);
                }
            });


        }else {
            holder.img_edt_email.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_emailtype,txt_email;
        ImageButton img_edt_email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_edt_email = itemView.findViewById(R.id.img_edt_email);
            txt_email = itemView.findViewById(R.id.txt_email);
            txt_emailtype = itemView.findViewById(R.id.txt_emailtype);
        }
    }
    public void getShowImage(boolean getimg){
        Log.e("TEST","Image Show :"+getimg);
        img = getimg;
        notifyDataSetChanged();
    }
}
