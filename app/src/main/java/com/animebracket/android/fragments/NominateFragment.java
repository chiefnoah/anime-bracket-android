package com.animebracket.android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.animebracket.android.R;
import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.adapters.AutoCompleteNameAdapter;
import com.animebracket.android.Util.beans.Bracket;
import com.animebracket.android.Util.beans.CharacterInfo;
import com.animebracket.android.Util.callbacks.BracketCardActionCallback;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.google.gson.Gson;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class NominateFragment extends Fragment {

    private BracketCardActionCallback callback;
    Bracket bracket;

    AutoCompleteTextView characterNameEditText;
    EditText characterSourceEditText;
    EditText linkToPicEditText;
    TextView bracketTitleTextView;

    AutoCompleteDataTask autocompleteTask;
    AutoCompleteNameAdapter autocompleteAdapter;



    public NominateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bracket = (Bracket) getArguments().getSerializable(Constants.FLAGS.BRACKET_ARG);

        //Throws error if bracket is null;
        if(bracket == null) {
            throw new InvalidParameterException("Must put bracket parameter in intent bundle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nominate, container, false);

        characterNameEditText = (AutoCompleteTextView) rootView.findViewById(R.id.character_name_autocomplete_edit_text);
        characterSourceEditText = (EditText) rootView.findViewById(R.id.character_source_edit_text);
        linkToPicEditText = (EditText) rootView.findViewById(R.id.link_to_picture_edit_text);
        bracketTitleTextView = (TextView) rootView.findViewById(R.id.bracket_title_text_view);

        bracketTitleTextView.setText(bracket.getName());


        //Set adapter
        autocompleteAdapter = new AutoCompleteNameAdapter(getActivity(), R.layout.suggested_character_item, new ArrayList<CharacterInfo>());
        characterNameEditText.setAdapter(autocompleteAdapter);
        characterNameEditText.setThreshold(1);
        characterNameEditText.addTextChangedListener(new TextWatcher() {
            private boolean shouldAutoComplete;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shouldAutoComplete = true;
                if(autocompleteTask != null) {
                    autocompleteTask.cancel(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(shouldAutoComplete) {
                    autocompleteTask = new AutoCompleteDataTask();
                    autocompleteTask.execute(s.toString());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (BracketCardActionCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BracketCardActionCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    private class AutoCompleteDataTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String q = params[0];
            Webb webb = Webb.create();
            webb.setBaseUri(Constants.BASE_URL);
            Response<String> response = webb.get(Constants.TYPEAHEAD_URL)
                    .param("q", q)
                    .param("bracketId", bracket.getId())
                    .asString();

            return response.getBody();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Gson gson = new Gson();
                CharacterInfo suggestedCharactersArray[] = gson.fromJson(s, CharacterInfo[].class);
                if (suggestedCharactersArray.length > 0) {
                    ArrayList<CharacterInfo> suggestedCharacters = new ArrayList<>(Arrays.asList(suggestedCharactersArray));
                    autocompleteAdapter.clear();
                    autocompleteAdapter.addAll(suggestedCharacters);
                    autocompleteAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {

            }
        }
    }

}
