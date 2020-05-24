package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.common.PassDataInterface;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    SettingEdit context;
    ArrayList<CommonModel> cityList;
    PassDataInterface dataInterface;

    public CityAdapter(SettingEdit settingEdit, ArrayList<CommonModel> cityList,PassDataInterface dataInterface) {
        this.context = settingEdit;
        this.cityList = cityList;
        this.dataInterface = dataInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_row_list,parent,false);
        return new CityAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.txt_city.setText( cityList.get(i).cN+", "+ cityList.get(i).cState+", "+ cityList.get(i).cCountry);

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_city,txt_state,txt_country;
        FrameLayout fra_city;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_city = itemView.findViewById(R.id.txt_city);
            //txt_state = itemView.findViewById(R.id.txt_state);
            //txt_country = itemView.findViewById(R.id.txt_country);
            fra_city = itemView.findViewById(R.id.fra_city);
            fra_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataInterface.CityData(cityList.get(getAdapterPosition()));
                /*Intent intent = new Intent(context,AddressDailogeAdapter.class);
                intent.putExtra("C",cityList.get(i));
                context.startActivity(intent);*/
                    //context.setCityName(cityList.get(i));
               /* if (context instanceof AddressDailogeAdapter)
                    ((DocumentInside)context).getDirInside();
            ((AddressDailogeAdapter) context).getEditTextValue();*/
                }
            });
        }
    }
}
