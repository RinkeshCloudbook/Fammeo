package com.fammeo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.EditActivity.EditHobbies;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.ViewHolder> {
    EditHobbies context;
    ArrayList<CommonModel> hobbyLang;
    public HobbyAdapter(EditHobbies editHobbies, ArrayList<CommonModel> hobbyLang) {
        this.context = editHobbies;
        this.hobbyLang = hobbyLang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.laguage_row_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.card_title.setText(hobbyLang.get(i).lName);
        holder.card_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(context instanceof SettingEdit)
                context.getLanName(hobbyLang.get(i).lName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hobbyLang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView card_title;
        FrameLayout card_container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_title =itemView.findViewById(R.id.card_title);
            card_container =itemView.findViewById(R.id.card_container);
        }
    }
}
