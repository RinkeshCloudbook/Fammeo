package com.fammeo.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.CreateUser;
import com.fammeo.app.activity.MainActivity;
import com.fammeo.app.fragment.FragmentCreate;

import java.util.ArrayList;

public class UserSuggestion extends RecyclerView.Adapter<UserSuggestion.ViewHolder> {
        Fragment mFragment;
        ArrayList<String> suglist;
        Context context;


    public UserSuggestion(FragmentCreate fragmentCreate, Context context, ArrayList<String> suglist) {
        this.suglist = suglist;
        this.mFragment = fragmentCreate;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestion_row_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.card_title.setText(suglist.get(i));
        holder.card_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Selected Name :"+suglist.get(i));
                String getName = suglist.get(i).toString();
                //if (mFragment instanceof FragmentCreate)
               // CreateUser.getSugName(getName);
                //FragmentCreate.getSugName(getName);
                ((FragmentCreate) mFragment).getSugName(getName,false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suglist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView card_title,txt_cSearvice;
        FrameLayout card_container;
        ImageView img_cLogo;
        LinearLayout lin_csearch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_title =itemView.findViewById(R.id.card_title);
            card_container =itemView.findViewById(R.id.card_container);
        }
    }
}
