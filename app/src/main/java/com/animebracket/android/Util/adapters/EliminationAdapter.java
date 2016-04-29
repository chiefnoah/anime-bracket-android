package com.animebracket.android.Util.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.animebracket.android.R;
import com.animebracket.android.Util.models.Round;
import com.animebracket.android.fragments.EliminateFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by noah on 4/19/2016.
 */
public class EliminationAdapter extends RecyclerView.Adapter<EliminationAdapter.ViewHolder> {

    public static final int CHARACTER = 0;
    public static final int SUBMIT = 1;

    ArrayList<Round> rounds;
    EliminationCallback callback;
    EliminateFragment fragment;

    public EliminationAdapter(EliminateFragment fragment, EliminationCallback callback, ArrayList<Round> rounds) {
        this.fragment = fragment;
        this.callback = callback;
        this.rounds = rounds;
    }

    @Override
    public EliminationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        if (viewType == SUBMIT) {
            vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.submit_button, parent, false));
            vh.submitButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: start submit task
                }
            });

        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_view, parent, false);
             vh = new ViewHolder(v);

            vh.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Set state to checked

                }
            });
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position >= getItemCount()) {
            //TODO: add submit button
            //Or do nothing, cause the button is static and has already been initialized
        } else {
            Round cRound = rounds.get(position);
            holder.characterName.setText(cRound.getCharacter1().getName());
            holder.characterShow.setText(cRound.getCharacter1().getSource());

            Picasso.with(holder.characterImage.getContext()).load(Uri.parse(cRound.getCharacter1().getImage())).into(holder.characterImage);
        }


    }

    @Override
    public int getItemCount() {
        return rounds.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount()) {
            return CHARACTER;
        } else {
            return SUBMIT;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View root;
        public ImageView characterImage;
        public TextView characterName;
        public TextView characterShow;

        public Button submitButton = null;


        public ViewHolder(View v) {
            super(v);
            if (v instanceof Button) {
                submitButton = (Button) v;
            } else {
                root = v;
                characterImage = (ImageView) v.findViewById(R.id.character_image_view);
                characterName = (TextView) v.findViewById(R.id.character_name);
                characterShow = (TextView) v.findViewById(R.id.character_show);
            }
        }
    }


    public interface EliminationCallback {
        void onEliminationSubmitted(String json);
        void onSubmitButtonClicked();
    }
}
