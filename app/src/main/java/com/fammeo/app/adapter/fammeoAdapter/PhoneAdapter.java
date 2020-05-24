package com.fammeo.app.adapter.fammeoAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {
    SettingEdit context;
    ArrayList<CommonModel> phoneList;

    public PhoneAdapter(SettingEdit settingEdit, ArrayList<CommonModel> phoneList) {
        this.context = settingEdit;
        this.phoneList = phoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_row_list,parent,false);
        return new PhoneAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.edt_type.setText(phoneList.get(i).phcType);
        holder.edt_cCode.setText("+"+phoneList.get(i).phcCode);
        holder.edt_phone.setText(phoneList.get(i).phNumber);
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView edt_type,edt_phone,edt_cCode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_cCode = itemView.findViewById(R.id.edt_cCode);
            edt_type = itemView.findViewById(R.id.edt_type);
            edt_phone = itemView.findViewById(R.id.edt_phone);
        }
    }
}
