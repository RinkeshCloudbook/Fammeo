package com.fammeo.app.adapter.fammeoAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.EditActivity.Skills;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;

public class SkillsListAdapter extends RecyclerView.Adapter<SkillsListAdapter.ViewHolder> {
    Skills context;
    ArrayList<CommonModel> skillList;
    public SkillsListAdapter(Skills skills, ArrayList<CommonModel> skillList) {
        this.context = skills;
        this.skillList = skillList;
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
        holder.txt_langname.setText(skillList.get(position).lName);

        holder.delete_lag.setVisibility(View.VISIBLE);
        holder.delete_lag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skillList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, skillList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return skillList.size();
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
