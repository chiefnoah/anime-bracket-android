package com.animebracket.android.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import com.animebracket.android.R;
import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.adapters.EliminationAdapter;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.models.Round;
import com.animebracket.android.Util.tasks.BasicRequestTask;

import java.util.ArrayList;


public class EliminateFragment extends Fragment implements JsonStringCallback{
    public static final String BRACKET_ID_KEY = "bracket_id_key";
    public static final String ROUNDS_SAVED_INSTANCE_KEY = "rounds_saved_instance_key";
    private int bracketId;
    private EliminationAdapter cAdapter;
    private EliminationAdapter.EliminationCallback callback;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Round> rounds;

    private BasicRequestTask roundLoader;

    RecyclerView rootView;

    public EliminateFragment() {
        // Required empty public constructor
        rounds = new ArrayList<>();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            rounds = (ArrayList<Round>) savedInstanceState.getSerializable(ROUNDS_SAVED_INSTANCE_KEY);
        }

        if(rounds.isEmpty()) {
            String cookie = CookieManager.getInstance().getCookie(Constants.BASE_URL) + "";
            roundLoader = new BasicRequestTask(this);
            roundLoader.execute(Constants.BRACKET_ACTIVE_ROUND_URL, cookie);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (RecyclerView) inflater.inflate(R.layout.recycled_list, container, false);
        cAdapter = new EliminationAdapter(this, callback, rounds);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onJsonStringReceived(String jsonString) {

    }
}
