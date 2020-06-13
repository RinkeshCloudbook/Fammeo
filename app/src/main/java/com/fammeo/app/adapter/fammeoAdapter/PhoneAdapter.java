package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.EditActivity.EditPhone;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.fragment.VewProfileFragment;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {
    VewProfileFragment context;
    ArrayList<CommonModel> phoneList;
    boolean img;
    Context mfragment;
    public PhoneAdapter(VewProfileFragment fragment, Context mcontext, ArrayList<CommonModel> phoneList) {
        this.context = fragment;
        this.phoneList = phoneList;
        this.mfragment= mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_row_list,parent,false);
        return new PhoneAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.edt_type.setText(phoneList.get(i).phcType);
        holder.edt_cCode.setText("+"+phoneList.get(i).phcCode);
        holder.edt_phone.setText(phoneList.get(i).phNumber);
        if(img == false){
            holder.img_edt_phone.setVisibility(View.VISIBLE);
            if(i == 0){
                holder.img_edt_phone.setVisibility(View.GONE);
            }
            holder.img_edt_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(mfragment, v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String itemName = String.valueOf(item.getTitle());
                            if(itemName.equalsIgnoreCase("Edit")){
                                Intent intent = new Intent(mfragment, EditPhone.class);
                                intent.putExtra("PT",phoneList.get(i).phcType);
                                intent.putExtra("PN",phoneList.get(i).phNumber);
                                intent.putExtra("PC",phoneList.get(i).phcCode);
                                intent.putExtra("PId",phoneList.get(i).phId);
                                context.startActivity(intent);
                            }else {
                                context.deletePhone(phoneList.get(i).phId);
                            }
                            return false;
                        }
                    });
                    popupMenu.inflate(R.menu.menu_people_more);
                    popupMenu.show();
                }
            });
        }else {
            holder.img_edt_phone.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView edt_type,edt_phone,edt_cCode;
        ImageButton img_edt_phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_cCode = itemView.findViewById(R.id.edt_cCode);
            edt_type = itemView.findViewById(R.id.edt_type);
            edt_phone = itemView.findViewById(R.id.edt_phone);
            img_edt_phone = itemView.findViewById(R.id.img_edt_phone);
        }
    }
    public void getShowImage(boolean getimg){
        Log.e("TEST","Image Show :"+getimg);
        img = getimg;
        notifyDataSetChanged();
    }
}
