package com.fammeo.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.activity.EditActivity.EditAddress;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.common.CommomInterface;
import com.fammeo.app.fragment.FammeoFragment.AboutFragment;
import com.fammeo.app.fragment.VewProfileFragment;
import com.fammeo.app.model.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    AboutFragment context;
    ArrayList<CommonModel> mAddressList;
    private List<String> mCityList = new ArrayList<>();
    boolean img;
    Context mfragment;
    public AddressAdapter(AboutFragment fragment, Context mcontext, ArrayList<CommonModel> mAddressList) {
        this.context = fragment;
        this.mAddressList = mAddressList;
        this.mfragment = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_row_list,parent,false);
        return new AddressAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        final CommonModel o = mAddressList.get(i);
        String address = mAddressList.get(i).cAddress+", "+ mAddressList.get(i).cN+", "+ mAddressList.get(i).cState+", "+ mAddressList.get(i).cCountry;
        holder.txt_addstype.setText(mAddressList.get(i).cType);
        holder.txt_address.setText(address);

        if(img == false){
            holder.img_edt_address.setVisibility(View.VISIBLE);

            holder.img_edt_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mfragment, v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String itemName = String.valueOf(item.getTitle());
                            if(itemName.equalsIgnoreCase("Edit")){
                                Intent intent = new Intent(mfragment, EditAddress.class);
                                intent.putExtra("T",mAddressList.get(i).cType);
                                intent.putExtra("A",mAddressList.get(i).cAddress);
                                intent.putExtra("C",mAddressList.get(i).cN);
                                intent.putExtra("S",mAddressList.get(i).cState);
                                intent.putExtra("ID",mAddressList.get(i).addsId);
                                intent.putExtra("Con",mAddressList.get(i).cCountry);
                                context.startActivity(intent);
                            }else if(itemName.equalsIgnoreCase("Delete")){
                                /*mAddressList.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i, mAddressList.size());*/
                                context.deleteAddress(mAddressList.get(i).addsId);

                            }
                            return false;
                        }
                    });
                    popupMenu.inflate(R.menu.menu_people_more);
                    popupMenu.show();
                }
            });

        }else{
            //holder.img_edt_address.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_addstype,txt_address;
        ImageButton img_edt_address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_addstype = itemView.findViewById(R.id.txt_addstype);
            txt_address = itemView.findViewById(R.id.txt_address);
            img_edt_address = itemView.findViewById(R.id.img_edt_address);

        }
    }
    public void getShowImage(boolean getimg){
        Log.e("TEST","Image Show :"+getimg);
         img = getimg;
         notifyDataSetChanged();
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, CommonModel obj, MenuItem item);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, CommonModel obj, int pos);
    }
}
