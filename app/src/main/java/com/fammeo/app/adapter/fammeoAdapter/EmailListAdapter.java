package com.fammeo.app.adapter.fammeoAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.model.EmailModel;

import java.util.ArrayList;

public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.ViewHolder> {
    Context context;
    ArrayList<EmailModel> emailList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.txt_emailtype.setText(emailList.get(i).emailType);
        holder.txt_email.setText(emailList.get(i).emailAddress);
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_emailtype,txt_email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_email = itemView.findViewById(R.id.txt_email);
            txt_emailtype = itemView.findViewById(R.id.txt_emailtype);
        }
    }
}
