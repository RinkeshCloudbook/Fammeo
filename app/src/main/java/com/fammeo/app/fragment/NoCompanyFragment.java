package com.fammeo.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fammeo.app.R;

public class NoCompanyFragment extends Fragment {

	public static NoCompanyFragment newInstance() {
		return new NoCompanyFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_nocompany, container, false);

		return rootView;
	}
}
