package com.fammeo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fammeo.app.R;
import com.fammeo.app.activity.ContectProfile;
import com.fammeo.app.fragment.ContactFragment;
import com.fammeo.app.model.ContactDetails;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    List<ContactDetails> contactList;
    Context context;
    private Fragment contactFragment;

   /* public ContactAdapter(Fragment mFragment,FragmentActivity activity, Context context, List<ContactDetails> contactList) {
        this.contactList = contactList;
        this.context = activity;
        Log.e("TEST","adapter Size :"+contactList.size());
    }
*/
    public ContactAdapter(ContactFragment contactFragment, Context context, List<ContactDetails> contactList) {
        this.contactList = contactList;
        this.contactFragment = contactFragment;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contect_list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int i) {

        holder.txt_FN.setText(contactList.get(i).FN);
        holder.txt_MN.setText(contactList.get(i).MN);
        holder.txt_LN.setText(contactList.get(i).LN);
        final String name = contactList.get(i).FN +" "+contactList.get(i).LN;
        //holder.icon.setText(list.get(position).getName().substring(0, 1));\
        String imageURL = contactList.get(i).imageUrl;

        if(imageURL == "null"){

            holder.imgCardImage.setImageResource(R.drawable.bg_square);
            holder.imgCardImage.setColorFilter(null);
            holder.icon_text.setText(name.substring(0, 1));
            holder.icon_text.setVisibility(View.VISIBLE);
        }else{
           // Glide.with(context).load(DataText.GetImagePath(imageURL)).into(holder.imgCardImage);
            /*Glide.with(context)
                    .load(DataText.GetImagePath(imageURL))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imgCardImage);*/
            Glide.with(context).load(com.fammeo.app.common.DataText.GetImagePath(imageURL))
                    .thumbnail(0.5f)
                    .transition(withCrossFade())
                    .apply( new RequestOptions().transform(new RoundedCorners(20)).diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.imgCardImage);
            holder.icon_text.setVisibility(View.GONE);
        }
        String email = contactList.get(i).email;
        String phone = contactList.get(i).Ph;
        if(phone == null){
            holder.txt_email.setText(email);
        }else{
            holder.txt_email.setText(phone);
        }

        final String getId = String.valueOf(contactList.get(i).contectId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContectProfile.class);
                intent.putExtra("D",getId);
                intent.putExtra("N",name);
                context.startActivity(intent);
               /* if(contactFragment instanceof ContactFragment){
                    ((ContactFragment)contactFragment).getcontactDetail(id);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_FN,txt_MN,txt_LN,icon_text,txt_email;
        ImageView imgCardImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_FN =itemView.findViewById(R.id.txt_FN);
            txt_MN =itemView.findViewById(R.id.txt_LN);
            txt_LN =itemView.findViewById(R.id.txt_LN);
            imgCardImage =itemView.findViewById(R.id.card_image);
            icon_text =itemView.findViewById(R.id.icon_text);
            txt_email =itemView.findViewById(R.id.txt_email);
        }
    }
}
