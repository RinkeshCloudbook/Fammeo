package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.EditActivity.EditLanguage;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.fragment.FragmentCreate;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    EditLanguage context;
    ArrayList<CommonModel> lagText;

    public LanguageAdapter(EditLanguage applicationContext, ArrayList<CommonModel> lagText) {
        this.context = applicationContext;
        this.lagText = lagText;
        Log.e("TEST","Adapter Size :"+lagText.size());
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.laguage_row_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        holder.card_title.setText(lagText.get(i).lName);
        holder.card_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(context instanceof SettingEdit)
                context.getLanName(lagText.get(i).lName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lagText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView card_title,txt_cSearvice;
        FrameLayout card_container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_title =itemView.findViewById(R.id.card_title);
            card_container =itemView.findViewById(R.id.card_container);
        }

    }
}
