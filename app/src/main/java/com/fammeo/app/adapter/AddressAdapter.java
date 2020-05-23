package com.fammeo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    SettingEdit context;
    ArrayList<CommonModel> mAddressList;
    private List<String> mCityList = new ArrayList<>();
    public AddressAdapter(SettingEdit settingEdit, ArrayList<CommonModel> mAddressList) {
        this.context = settingEdit;
        this.mAddressList = mAddressList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_row_list,parent,false);
        return new AddressAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        String address = mAddressList.get(i).cAddress+", "+ mAddressList.get(i).cN+", "+ mAddressList.get(i).cState+", "+ mAddressList.get(i).cCountry;
        holder.txt_addstype.setText(mAddressList.get(i).cType);
        holder.txt_address.setText(address);
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_addstype,txt_address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_addstype = itemView.findViewById(R.id.txt_addstype);
            txt_address = itemView.findViewById(R.id.txt_address);
        }
    }
}
