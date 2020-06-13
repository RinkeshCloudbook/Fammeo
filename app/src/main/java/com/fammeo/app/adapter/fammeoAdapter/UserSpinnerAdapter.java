package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.fammeo.app.R;
import com.fammeo.app.fragment.FammeoFragment.ConversationsFragment;

public class UserSpinnerAdapter extends BaseAdapter {
    FragmentActivity context;
    int[] userIcon;
    String[] userName;
    LayoutInflater inflater;

    public UserSpinnerAdapter(FragmentActivity activity, int[] userIcon, String[] userName) {
        this.context = activity;
        this.userIcon = userIcon;
        this.userName = userName;
        inflater = (LayoutInflater.from(activity));
    }

    @Override
    public int getCount() {
        return userIcon.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_user_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(userIcon[i]);
        names.setText(userName[i]);
        return view;
    }
}
