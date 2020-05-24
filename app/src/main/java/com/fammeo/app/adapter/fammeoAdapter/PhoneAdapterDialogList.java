package com.fammeo.app.adapter.fammeoAdapter;

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
    public PhoneAdapterDialogList(SettingEdit settingEdit, ArrayList<CommonModel> phoneList) {
        this.contex = settingEdit;
        this.phoneList = phoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_dailog_item_list,parent,false);
        return new PhoneAdapterDialogList.ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.edt_Dtype.setText(phoneList.get(i).phcType);
        holder.edt_DcCode.setText(phoneList.get(i).phcCode);
        holder.edt_Dphone.setText(phoneList.get(i).phNumber);

        holder.edt_Dtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Adapter Type");
                contex.ShowPhoneType(v);
            }
        });
        phoneList.get(i).phNumber = holder.edt_Dphone.getText().toString();
        phoneList.get(i).phcType = holder.edt_Dtype.getText().toString();
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
}
