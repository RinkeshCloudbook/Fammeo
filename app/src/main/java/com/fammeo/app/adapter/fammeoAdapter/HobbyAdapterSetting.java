package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.fragment.FammeoFragment.AboutFragment;
import com.fammeo.app.fragment.VewProfileFragment;
import com.fammeo.app.model.CommonModel;

import java.util.List;

public class HobbyAdapterSetting extends RecyclerView.Adapter<HobbyAdapterSetting.ViewHolder> {
    AboutFragment context;
    List<CommonModel> hobbyList;
    Context mfragment;
    public HobbyAdapterSetting(AboutFragment fragment, Context mcontext, List<CommonModel> hobbyList) {
        this.context = fragment;
        this.hobbyList = hobbyList;
        this.mfragment = mcontext;
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
        holder.txt_langname.setText(hobbyList.get(position).lName);
        holder.delete_lag.setVisibility(View.GONE);
        holder.delete_lag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hobbyList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, hobbyList.size());
               // context.enableSaveBottn(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hobbyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_langname;
        ImageButton delete_lag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_langname = itemView.findViewById(R.id.txt_langname);
            delete_lag = itemView.findViewById(R.id.delete_lag);
        }
    }
}
