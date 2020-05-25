package com.fammeo.app.adapter.fammeoAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;

public class PhoneAdapterDialogList extends RecyclerView.Adapter<PhoneAdapterDialogList.ViewHolder> {
    SettingEdit contex;
    ArrayList<CommonModel> phoneList;
    public static ArrayList<CommonModel> tempphoneList;
    public PhoneAdapterDialogList(SettingEdit settingEdit, ArrayList<CommonModel> phoneList) {
        this.contex = settingEdit;
        this.phoneList = phoneList;
        this.tempphoneList = phoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_dailog_item_list,parent,false);
        return new PhoneAdapterDialogList.ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.edt_Dtype.setText(phoneList.get(i).phcType);
        holder.edt_DcCode.setText(phoneList.get(i).phcCode);
        holder.edt_Dphone.setText(phoneList.get(i).phNumber);

        holder.edt_Dtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Adapter Type");
                newShowPhoneType(v,i);
            }
        });

        phoneList.get(i).phNumber = holder.edt_Dphone.getText().toString();
        phoneList.get(i).phcType = holder.edt_Dtype.getText().toString();
        holder.edt_Dphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    Log.e("TEST","Get Number :"+s);
                   // phoneList.get(i).phNumber = holder.edt_Dphone.getText().toString();
                    tempphoneList.get(i).phNumber = s.toString();

                   // contex.updatephonelist(phoneList);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return phoneList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText edt_DcCode,edt_Dphone,edt_Dtype;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            edt_DcCode = itemView.findViewById(R.id.edt_DcCode);
            edt_Dphone = itemView.findViewById(R.id.edt_Dphone);
            edt_Dtype = itemView.findViewById(R.id.edt_Dtype);
        }
    }
    public void newShowPhoneType(final View v, final int pos) {
        final String[] emailType = new String[]{
                "Mobile", "Office", "Home", "Main", "Work Fax", "Home Fax", "other"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(contex);
        builder.setSingleChoiceItems(emailType, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                ((EditText) v).setText(emailType[i]);
                tempphoneList.get(pos).phcType = emailType[i];
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
