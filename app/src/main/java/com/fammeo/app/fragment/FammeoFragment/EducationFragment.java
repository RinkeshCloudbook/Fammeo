package com.fammeo.app.fragment.FammeoFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fammeo.app.R;
import com.fammeo.app.activity.WorkEducation;

/**
 * A simple {@link Fragment} subclass.
 */
public class EducationFragment extends Fragment {
    View mView;
    public EducationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_education, container, false);

        ((ImageButton) mView.findViewById(R.id.img_edtEducation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WorkEducation.class);
                startActivity(intent);
            }
        });

        return mView;
    }
}
