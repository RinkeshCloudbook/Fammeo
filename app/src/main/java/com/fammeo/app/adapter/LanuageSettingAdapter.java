package com.fammeo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.fragment.FammeoFragment.AboutFragment;
import com.fammeo.app.fragment.VewProfileFragment;
import com.fammeo.app.model.CommonModel;

import java.util.List;

public class LanuageSettingAdapter extends RecyclerView.Adapter<LanuageSettingAdapter.ViewHolder> {
    AboutFragment context;
    List<CommonModel> profileLangList;
    Context mfragment;
    public LanuageSettingAdapter(AboutFragment fragment, Context mcontext, List<CommonModel> profileLangList) {
        this.context = fragment;
        this.profileLangList = profileLangList;
        this.mfragment = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.laguage_fron_item_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_langname.setText(profileLangList.get(position).lName);
    }

    @Override
    public int getItemCount() {
        return profileLangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_langname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_langname = itemView.findViewById(R.id.txt_langname);
        }
    }
}
