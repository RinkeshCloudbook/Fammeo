package com.fammeo.app.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.EditActivity.EditAddress;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.common.CommomInterface;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    SettingEdit context;
    ArrayList<CommonModel> mAddressList;
    private List<String> mCityList = new ArrayList<>();
    boolean img;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        String address = mAddressList.get(i).cAddress+", "+ mAddressList.get(i).cN+", "+ mAddressList.get(i).cState+", "+ mAddressList.get(i).cCountry;
        holder.txt_addstype.setText(mAddressList.get(i).cType);
        holder.txt_address.setText(address);

        if(img == true){
            holder.img_edt_address.setVisibility(View.VISIBLE);
            holder.img_edt_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditAddress.class);
                    intent.putExtra("T",mAddressList.get(i).cType);
                    intent.putExtra("A",mAddressList.get(i).cAddress);
                    intent.putExtra("C",mAddressList.get(i).cN);
                    intent.putExtra("S",mAddressList.get(i).cState);
                    intent.putExtra("ID",mAddressList.get(i).addsId);
                    intent.putExtra("Con",mAddressList.get(i).cCountry);
                    context.startActivity(intent);
                }
            });
        }else{
            holder.img_edt_address.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_addstype,txt_address;
        ImageButton img_edt_address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_addstype = itemView.findViewById(R.id.txt_addstype);
            txt_address = itemView.findViewById(R.id.txt_address);
            img_edt_address = itemView.findViewById(R.id.img_edt_address);

        }
    }
    public void getShowImage(boolean getimg){
        Log.e("TEST","Image Show :"+getimg);
         img = getimg;
         notifyDataSetChanged();
    }
}
