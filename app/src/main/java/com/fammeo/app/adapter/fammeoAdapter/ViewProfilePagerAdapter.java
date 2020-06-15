package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fammeo.app.fragment.FammeoFragment.AboutFragment;
import com.fammeo.app.fragment.FammeoFragment.FollowersFragment;
import com.fammeo.app.fragment.FammeoFragment.FollowingFragment;
import com.fammeo.app.fragment.VewProfileFragment;

public class ViewProfilePagerAdapter extends FragmentPagerAdapter {

    Context context;
    int tabCount;
    public Fragment[] mfragment;

    public ViewProfilePagerAdapter(FragmentActivity activity, FragmentManager fm, int tabCount) {
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
                AboutFragment AF = new AboutFragment();
                mfragment[i] = AF;
                return AF;
            case 1:
                FollowersFragment followers = new FollowersFragment();
                mfragment[i] = followers;
                return followers;
            case 2:
                FollowingFragment following = new FollowingFragment();
                mfragment[i] = following;
                return following;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
