package com.fammeo.app.adapter.fammeoAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.model.EmailModel;

import java.util.ArrayList;


public class EmailDailogeAdapter extends RecyclerView.Adapter<EmailDailogeAdapter.ViewHolder> {
    SettingEdit context;
    ArrayList<EmailModel> emailList;
    String valid_email;
    public EmailDailogeAdapter(SettingEdit applicationContext, ArrayList<EmailModel> emailList) {
        this.context = applicationContext;
        this.emailList = emailList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.email_dailog_item_list,parent,false);
        return new EmailDailogeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.edt_emailType.setText(emailList.get(i).emailType);
        holder.edt_email.setText(emailList.get(i).emailAddress);
        holder.edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailList.get(i).emailAddress = holder.edt_email.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TEST","Adapter Email :"+s.toString());
                String em = s.toString();
                if(em.equals("")){
                    Log.e("TEST","Condition Check");
                    holder.edt_emailType.setText("");
                }
                Is_Valid_Email(holder.edt_email);
            }
        });
        holder.edt_emailType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Click Email type");
                //showEmailType(v);
                context.showEmailTupe(v, i);
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Delete Image");
                emailList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, emailList.size());
                context.enableSaveBottn(true);
            }
        });
    }

    private void Is_Valid_Email(EditText edt) {
        if (edt.getText().toString() == null) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else if (isEmailValid(edt.getText().toString()) == false) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else {
            valid_email = edt.getText().toString();
            Log.e("TEST","valid Email :"+valid_email);
          //  Log.e("TEST","Valid type :"+valid_email);
            //bt_save.setVisibility(View.VISIBLE);
            context.enableSaveBottn(true);
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();

    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText edt_emailType,edt_email;
        ImageButton img_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            edt_email = itemView.findViewById(R.id.edt_email);
            edt_emailType = itemView.findViewById(R.id.edt_emailType);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}

