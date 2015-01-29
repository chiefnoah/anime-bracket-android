package com.animebracket.android.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.animebracket.android.R;
import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.adapters.CurrentBracketsAdapter;
import com.animebracket.android.Util.beans.Bracket;
import com.animebracket.android.Util.callbacks.BracketCardActionCallback;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.tasks.BasicRequestTask;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunningBracketsFragment extends Fragment implements JsonStringCallback {

    BracketCardActionCallback callback;

    private RecyclerView rootView;
    private CurrentBracketsAdapter cAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Bracket> currentBrackets;

    private BasicRequestTask bracketsLoader;

    public RunningBracketsFragment() {
        // Required empty public constructor
        currentBrackets = new ArrayList<Bracket>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String cookie = CookieManager.getInstance().getCookie(Constants.BASE_URL) + "";
        bracketsLoader = new BasicRequestTask(this);
        bracketsLoader.execute(Constants.BRACKETS_LIST_URL, cookie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (RecyclerView) inflater.inflate(R.layout.brackets_card_list, container, false);
        cAdapter = new CurrentBracketsAdapter(this, callback, currentBrackets);

        rootView.setHasFixedSize(false); //We don't know the size of the list

        layoutManager = new LinearLayoutManager(getActivity());
        rootView.setLayoutManager(layoutManager);
        rootView.setAdapter(cAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (BracketCardActionCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement RunningBracketsFragmentCallback");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        bracketsLoader.cancel(true);
    }

    @Override
    public void onJsonStringReceived(String jsonString) {
        if (jsonString == null) {
            //TODO: Show dialogue box
        }
        Bracket[] cBrackets = null;
        try {
            Gson gson = new Gson();
            cBrackets = gson.fromJson(jsonString, Bracket[].class);
        } catch (Exception e) {
        }
        if (cBrackets == null) {
            //TODO: No currently running brackets. What do?
            Toast.makeText(getActivity(), "No running brackets", Toast.LENGTH_LONG);
        } else {

            //iterate through array list and remove those that aren't running
            for (Bracket b : cBrackets) {
                switch (b.getState()) {
                    case Constants.BRACKET_STATE_NOMINATIONS:
                    case Constants.BRACKET_STATE_ELIMINATIONS:
                    case Constants.BRACKET_STATE_VOTING:
                    case Constants.BRACKET_STATE_WILDCARD:
                        currentBrackets.add(b);
                        break;
                    case Constants.BRACKET_STATE_FINAL:
                    case Constants.BRACKET_STATE_HIDDEN:
                        break;
                }
            }

            if(cAdapter != null) {
                cAdapter.updateDataset(currentBrackets);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    //Quick reference to self so I can access it from inner classes
    public RunningBracketsFragment getInstance() {
        return this;
    }
}
