package com.animebracket.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import com.animebracket.android.R;
import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.adapters.VoteAdapter;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.models.Round;
import com.animebracket.android.Util.tasks.BasicRequestTask;
import com.animebracket.android.Util.tasks.VoteTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class VoteFragment extends Fragment implements VoteAdapter.VoteSubmitCallback, VoteTask.VoteCallback, JsonStringCallback {

    public static final String VOTE_ROUNDS_KEY = "vote_rounds_key";

    private int bracketID;
    private RecyclerView rootView;
    private VoteAdapter voteAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Round> rounds;

    private BasicRequestTask roundsLoader;

    public VoteFragment() {
        // Initialize ArrayList
        rounds = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load the bracketID from the arguments
        bracketID = getArguments().getInt(Constants.FLAGS.BRACKET_ID_ARG);
        if (savedInstanceState != null) {
            rounds = (ArrayList<Round>) savedInstanceState.getSerializable(VOTE_ROUNDS_KEY);
        }

        if (rounds.isEmpty()) {
            String cookie = CookieManager.getInstance().getCookie(Constants.BASE_URL) + "";
            roundsLoader = new BasicRequestTask(this);
            roundsLoader.execute(Constants.BRACKET_ACTIVE_ROUND_URL + "?bracketId=" + bracketID, cookie);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (RecyclerView) inflater.inflate(R.layout.recycled_list, container, false);
        voteAdapter = new VoteAdapter(this, this, rounds);

        rootView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getActivity());
        rootView.setLayoutManager(layoutManager);
        rootView.setAdapter(voteAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Save the rounds arrayList
        if (rounds != null) {
            outState.putSerializable(VOTE_ROUNDS_KEY, rounds);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Cancel loading if we're detaching
        if (roundsLoader != null && roundsLoader.isCancelled()) {
            roundsLoader.cancel(true);
        }
    }

    //Called when we get the rounds for the provided bracket ID
    @Override
    public void onJsonStringReceived(String jsonString) {
        if (jsonString == null) {
            //TODO: show error dialog box
        }
        Round[] roundArray = null;
        try {
            Gson gson = new Gson();
            roundArray = gson.fromJson(jsonString, Round[].class);
        } catch (JsonSyntaxException je) {
            //uh oh :(
        }
        if (roundArray == null) {
            //TODO: there was some error, figure out how to handle it
        } else {
            rounds = new ArrayList<>(Arrays.asList(roundArray));
            if (voteAdapter != null) {
                voteAdapter.updateDataset(rounds);
            }
        }

    }

    //Called when the submit button is clicked
    @Override
    public void onSubmitButtonClicked(HashMap<String, String> votedCharacters) {
        //TODO: start submit vote task
    }

    //Called after the submit task has completed
    @Override
    public void onVoteSubmitted(String json) {

    }
}
