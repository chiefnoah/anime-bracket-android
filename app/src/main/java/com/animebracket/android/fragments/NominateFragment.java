package com.animebracket.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.animebracket.android.R;
import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.adapters.AutoCompleteNameAdapter;
import com.animebracket.android.Util.beans.BasicResponse;
import com.animebracket.android.Util.beans.Bracket;
import com.animebracket.android.Util.beans.CharacterInfo;
import com.animebracket.android.Util.callbacks.BracketCardActionCallback;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.tasks.NominateTask;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.google.gson.Gson;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class NominateFragment extends Fragment implements JsonStringCallback {

    private BracketCardActionCallback callback;
    Bracket bracket;

    AutoCompleteTextView characterNameEditText;
    EditText characterSourceEditText;
    EditText linkToPicEditText;
    TextView bracketTitleTextView;
    TextView nameLabelTextView;
    TextView sourceLabelTextView;
    TextView verifiedHiddenTextView;

    AutoCompleteDataTask autocompleteTask;
    AutoCompleteNameAdapter autocompleteAdapter;
    Button nominateButton;

    NominateTask nominateTask;


    public NominateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.FLAGS.BRACKET_ARG, bracket);
        if(characterNameEditText.getText() != null) {
            outState.putString(Constants.FLAGS.NOMINATE_CHARACTER_NAME_TEXT, characterNameEditText.getText().toString());
        }
        if(characterSourceEditText.getText() != null) {
            outState.putString(Constants.FLAGS.NOMINATE_CHARACTER_SOURCE_TEXT, characterSourceEditText.getText().toString());
        }
        if(linkToPicEditText.getText() != null) {
            outState.putString(Constants.FLAGS.NOMINATE_CHARACTER_IMAGE_TEXT, linkToPicEditText.getText().toString());
        }
        if(verifiedHiddenTextView.getText() != null) {
            outState.putString(Constants.FLAGS.NOMINATE_CHARACTER_VERIFIED_TEXT, verifiedHiddenTextView.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Assign views
        View rootView = inflater.inflate(R.layout.fragment_nominate, container, false);

        characterNameEditText = (AutoCompleteTextView) rootView.findViewById(R.id.character_name_autocomplete_edit_text);
        characterSourceEditText = (EditText) rootView.findViewById(R.id.character_source_edit_text);
        linkToPicEditText = (EditText) rootView.findViewById(R.id.link_to_picture_edit_text);
        bracketTitleTextView = (TextView) rootView.findViewById(R.id.bracket_title_text_view);
        nameLabelTextView = (TextView) rootView.findViewById(R.id.name_label_text_view);
        sourceLabelTextView = (TextView) rootView.findViewById(R.id.source_label_text_view);
        nominateButton = (Button) rootView.findViewById(R.id.nominate_button);
        verifiedHiddenTextView = (TextView) rootView.findViewById(R.id.verified_hidden_text_view);

        //Load bracket from saved instance state
        if(savedInstanceState != null) {
            bracket = (Bracket) savedInstanceState.getSerializable(Constants.FLAGS.BRACKET_ARG);
            String nameText = savedInstanceState.getString(Constants.FLAGS.NOMINATE_CHARACTER_NAME_TEXT, characterNameEditText.getText().toString());
            String sourceText = savedInstanceState.getString(Constants.FLAGS.NOMINATE_CHARACTER_SOURCE_TEXT, characterSourceEditText.getText().toString());
            String linkText = savedInstanceState.getString(Constants.FLAGS.NOMINATE_CHARACTER_IMAGE_TEXT, linkToPicEditText.getText().toString());
            String verifiedText = savedInstanceState.getString(Constants.FLAGS.NOMINATE_CHARACTER_VERIFIED_TEXT, linkToPicEditText.getText().toString());
            if(nameText != null) {
                characterNameEditText.setText(nameText);
            }
            if(sourceText != null) {
                characterSourceEditText.setText(sourceText);
            }
            if(linkText != null) {
                linkToPicEditText.setText(linkText);
            }
            if(verifiedText != null) {
                verifiedHiddenTextView.setText(verifiedText);
            }
        } else {
            bracket = (Bracket) getArguments().getSerializable(Constants.FLAGS.BRACKET_ARG);
        }

        //Throws error if bracket is null;
        if(bracket == null) {
            throw new InvalidParameterException("Must put bracket parameter in intent bundle");
        }

        //Set textview texts from bracket values (if they exists)
        bracketTitleTextView.setText(bracket.getName());
        if(bracket.getNameLabel() != null) {
            nameLabelTextView.setText(bracket.getNameLabel() + ":");
        }

        if(bracket.getSourceLabel() != null) {
            sourceLabelTextView.setText(bracket.getSourceLabel() + ":");
        }


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
                verifiedHiddenTextView.setText("false");
                if(shouldAutoComplete) {
                    autocompleteTask = new AutoCompleteDataTask();
                    autocompleteTask.execute(s.toString());
                }
            }
        });

        characterNameEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                characterSourceEditText.setText(autocompleteAdapter.getItem(position).getSource());
                linkToPicEditText.setText(autocompleteAdapter.getItem(position).getImage());
                verifiedHiddenTextView.setText(String.valueOf(autocompleteAdapter.getItem(position).isVerified()));
                hideKeyboard();
            }
        });

        nominateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check if the textviews are empty and react if the task fails
                String cookie = CookieManager.getInstance().getCookie(Constants.BASE_URL) + "";
                String bracketId = String.valueOf(bracket.getId());
                String nomineeName = characterNameEditText.getText().toString();
                String nomineeSource = characterSourceEditText.getText().toString();
                String imageLink = linkToPicEditText.getText().toString();
                String verified = verifiedHiddenTextView.getText().toString();
                nominateTask = new NominateTask(getInstance());
                nominateTask.execute(cookie, bracketId, nomineeName, nomineeSource, imageLink, verified);
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

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onJsonStringReceived(String jsonString) {
        //TODO: Show dialogue box instead of toast on success or failure
        Gson gson = new Gson();
        BasicResponse response = gson.fromJson(jsonString, BasicResponse.class);
        if(response.isSuccess()) {
            Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
            characterNameEditText.setText("");
            characterSourceEditText.setText("");
            linkToPicEditText.setText("");
            verifiedHiddenTextView.setText("false");
        } else {
            Toast.makeText(getActivity(), "Failed: " + response.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    //Quick workaround to get a reference to this fragment for inner classes
    public NominateFragment getInstance() {
        return this;
    }
}
