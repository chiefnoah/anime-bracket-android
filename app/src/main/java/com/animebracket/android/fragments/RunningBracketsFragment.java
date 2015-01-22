package com.animebracket.android.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animebracket.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunningBracketsFragment extends Fragment {


    public RunningBracketsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_running_brackets, container, false);
    }


}
