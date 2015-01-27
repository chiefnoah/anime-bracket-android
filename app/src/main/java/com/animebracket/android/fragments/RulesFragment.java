package com.animebracket.android.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.animebracket.android.R;
import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.beans.Bracket;
import com.animebracket.android.Util.callbacks.BracketCardActionCallback;
import com.animebracket.android.Util.tasks.BasicRequestTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RulesFragment extends Fragment {

    Bracket bracket;
    LinearLayout rootView;
    TextView rulesTextView;
    Button actionButton;
    BracketCardActionCallback callback;

    public RulesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bracket = (Bracket) getArguments().getSerializable(Constants.FLAGS.BRACKET_ARG);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (BracketCardActionCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement BracketCardActionCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (LinearLayout) inflater.inflate(R.layout.rules_card, container, false);
        rulesTextView = (TextView) rootView.findViewById(R.id.rules_text_view);
        actionButton = (Button) rootView.findViewById(R.id.action_button);

        rulesTextView.setText(bracket.getRules().replace("\n\r", System.getProperty("line.separator")));
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onActionButtonClick(getInstance(), bracket);
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    //Quick reference to self so I can access it from inner classes
    private RulesFragment getInstance() {
        return this;
    }
}
