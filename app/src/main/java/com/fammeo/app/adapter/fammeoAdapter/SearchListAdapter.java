package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fammeo.app.R;
import com.fammeo.app.activity.CreateUser;
import com.fammeo.app.activity.MainActivity;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataText;
import com.fammeo.app.common.PassDataInterface;
import com.fammeo.app.fragment.FragmentCreate;
import com.fammeo.app.fragment.Search;
import com.fammeo.app.model.SearchUserModel;
import com.google.common.eventbus.EventBus;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    Fragment mfragment;
    Context context;
    ArrayList<SearchUserModel> searchlist;
    PassDataInterface dataInterface;
    String imgUrl;

    public SearchListAdapter(Fragment mfragment, Context applicationContext, ArrayList<SearchUserModel> searchlist,PassDataInterface dataInterface) {
        this.context = applicationContext;
        this.searchlist = searchlist;
        this.mfragment = mfragment;
        this.dataInterface=dataInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        final String fullName = searchlist.get(i).FN+" "+searchlist.get(i).LN;
        final String FN = searchlist.get(i).FN;
        final String LN = searchlist.get(i).LN;
        final String email = searchlist.get(i).Email;
        holder.txt_name.setText(fullName);
        holder.txt_email.setText(email);

        String firstLater = fullName.substring(0,1).toUpperCase();
        imgUrl = searchlist.get(i).url;
        Log.e("TEST","Get Image Url :"+imgUrl);
        if(imgUrl != null){
            Glide.with(context).load(DataText.GetImagePath(imgUrl))
                    .thumbnail(0.5f)
                    .transition(withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.search_image);

        }else {
            Log.e("TEST","Get Image Url NULL:");
            holder.search_image.setImageResource(R.drawable.bg_search_circle);
            holder.search_image.setColorFilter(null);
            holder.search_image_text.setText(firstLater);
        }
        holder.btn_createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Adapter Click :"+fullName);

                dataInterface.userData(fullName,FN,LN,email,imgUrl);
               // ((Search) mfragment).getUerData(fullName,FN,LN,email,imgUrl);

                /*Intent intent = new Intent(context, FragmentCreate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("N",fullName);
                intent.putExtra("FN",FN);
                intent.putExtra("LN",LN);
                intent.putExtra("E",email);
                intent.putExtra("url",imgUrl);
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView search_image;
        TextView search_image_text,txt_name,txt_email;
        ImageView btn_createUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            search_image = itemView.findViewById(R.id.search_image);
            search_image_text = itemView.findViewById(R.id.search_image_text);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_email = itemView.findViewById(R.id.txt_email);
            btn_createUser = itemView.findViewById(R.id.btn_createUser);
        }

    }
}
