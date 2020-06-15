package com.fammeo.app.fragment.FammeoFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fammeo.app.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {

    private JSONObject fragmentData = null;
    View mView;
    public FollowersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_followers, container, false);

        Toast.makeText(getActivity(), "Followers", Toast.LENGTH_SHORT).show();

        return mView;
    }

    public void setFragmentData(JSONObject data) {
        fragmentData = data;
    }
}
