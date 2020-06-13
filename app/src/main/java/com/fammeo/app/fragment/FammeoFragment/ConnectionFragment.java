package com.fammeo.app.fragment.FammeoFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.fammeo.app.R;
import com.fammeo.app.adapter.fammeoAdapter.Connectionadapter;
import com.fammeo.app.util.Tools;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment {

    View mView;
    private ViewPager view_pager;
    private TabLayout tab_layout;

    public ConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_connection, container, false);

//        initToolbar();
        initComponent();

        return mView;
    }
    private void initComponent() {
        view_pager = mView.findViewById(R.id.view_pager);
        tab_layout = mView.findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setText("Friends"));
        tab_layout.addTab(tab_layout.newTab().setText("Received requests"));
        tab_layout.addTab(tab_layout.newTab().setText("Sent request"));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        Connectionadapter adapter = new Connectionadapter(getActivity(),getFragmentManager(),tab_layout.getTabCount());
        view_pager.setAdapter(adapter);

        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
