package com.fammeo.app.common;



import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.fammeo.app.R;

public class AlertDailogBox {
 AlertDialog dialog;

    public AlertDailogBox(Context context, String header) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.laguage_dialog_event, null);

        TextView dailog_header = layout.findViewById(R.id.dailog_header);
        LinearLayout lin_dailog_edit = layout.findViewById(R.id.lin_dailog_edit);

        dialog.setView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.e("TEST","Header :"+header);
        if(header.equalsIgnoreCase("Edit Language")){
            lin_dailog_edit.setVisibility(View.VISIBLE);
        }
    }
    public void show() {
        dialog.show();
    }
    public void hide() {
        dialog.dismiss();
    }
}
