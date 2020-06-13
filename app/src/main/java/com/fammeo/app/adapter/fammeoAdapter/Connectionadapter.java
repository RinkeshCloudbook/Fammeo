package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fammeo.app.fragment.FammeoFragment.FriendsFragment;
import com.fammeo.app.fragment.FammeoFragment.Request_rec_Fragment;
import com.fammeo.app.fragment.FammeoFragment.Request_sent_Fragment;

public class Connectionadapter extends FragmentPagerAdapter {

    Context context;
    int tabCount;
    public Fragment[] mfragment;

    public Connectionadapter(FragmentActivity activity, FragmentManager fm, int tabCount) {
        super(fm);
        context = activity;
        this.tabCount = tabCount;
        mfragment = new Fragment[tabCount];

    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                FriendsFragment ff = new FriendsFragment();
                mfragment[i] = ff;
                return ff;
            case 1:
                Request_rec_Fragment rr = new Request_rec_Fragment();
                mfragment[i] = rr;
                return rr;
            case 2:
                Request_sent_Fragment rs = new Request_sent_Fragment();
                mfragment[i] = rs;
                return rs;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
