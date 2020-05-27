package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.EditActivity.EditLanguage;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.model.CommonModel;

import java.util.List;

public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.ViewHolder> {
    EditLanguage context;
    List<CommonModel> allLangList;
    boolean flag = true;

    public LanguageListAdapter(EditLanguage settingEdit, List<CommonModel> allLangList, boolean flag) {
        this.context = settingEdit;
        this.allLangList = allLangList;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.laguage_item_list_design,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txt_langname.setText(allLangList.get(position).lName);
        if(flag == true){
            holder.delete_lag.setVisibility(View.VISIBLE);
            holder.delete_lag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allLangList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, allLangList.size());
                    context.enableSaveBottn(true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return allLangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_langname;
        ImageButton delete_lag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_langname = itemView.findViewById(R.id.txt_langname);
            delete_lag = itemView.findViewById(R.id.delete_lag);
        }
    }
}
