package com.animebracket.android.Util.adapters;

/**
 * Created by noah on 10/30/2015.
 */

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
import com.animebracket.android.fragments.VoteFragment;
import com.animebracket.android.views.CharacterView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by noah on 4/19/2016.
 */
public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {

    //We use the IDs for character view 1 and 2 cause otherwise View.setTag will throw a hissy fit
    public static final int ROUNDID = R.id.character_view_1;
    public static final int CHARACTER = R.id.character_view_2;

    ArrayList<Round> rounds;
    HashMap<String, String> votedRounds;
    VoteSubmitCallback callback;
    VoteFragment fragment;

    public VoteAdapter(VoteFragment fragment, VoteSubmitCallback callback, ArrayList<Round> rounds) {
        this.fragment = fragment;
        this.callback = callback;
        this.rounds = rounds;
        votedRounds = new HashMap<>();
    }

    @Override
    public VoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vote_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        //Set the on click listeners for the character views
        vh.character1View.setOnClickListener(new CharacterViewOnClickListener());

        vh.character2View.setOnClickListener(new CharacterViewOnClickListener());
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Round cRound = rounds.get(position);

        holder.root.setTag(cRound.getId()); //this is probably unnecessary

        //We set tags containing the round ID and the character ID for the character represented in this view
        //used on the onclicklistener set for each characterview so we can keep track of which character is voted for
        holder.character1View.setTag(ROUNDID, cRound.getId());
        holder.character1View.setTag(CHARACTER, cRound.getCharacter1Id());
        holder.character1Name.setText(cRound.getCharacter1().getName());
        holder.character1Show.setText(cRound.getCharacter1().getSource());
        Picasso.with(holder.character1Image.getContext()).load(Uri.parse(cRound.getCharacter1().getImage())).into(holder.character1Image);

        holder.character2View.setTag(ROUNDID, cRound.getId());
        holder.character2View.setTag(CHARACTER, cRound.getCharacter2Id());
        holder.character2Name.setText(cRound.getCharacter2().getName());
        holder.character2Show.setText(cRound.getCharacter2().getSource());
        Picasso.with(holder.character2Image.getContext()).load(Uri.parse(cRound.getCharacter2().getImage())).into(holder.character2Image);
    }

    @Override
    public int getItemCount() {
        return rounds.size();
    }

    public void updateDataset(ArrayList<Round> newDataset) {
        rounds = newDataset;
        notifyDataSetChanged();
    }

    private class CharacterViewOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //Retrieve the character and round IDs
            int roundID = (int) v.getTag(ROUNDID);
            int characterID = (int) v.getTag(CHARACTER);

            //Insert character and round IDs into hashmap
            votedRounds.put("round:" + roundID, Integer.toString(characterID));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View root;
        public CharacterView character1View;
        public CharacterView character2View;
        public ImageView character1Image;
        public ImageView character2Image;
        public TextView character1Name;
        public TextView character2Name;
        public TextView character1Show;
        public TextView character2Show;

        public Button submitButton = null;


        public ViewHolder(View v) {
            super(v);
                root = v;
                character1View = (CharacterView) v.findViewById(R.id.character_view_1);
                character2View = (CharacterView) v.findViewById(R.id.character_view_2);

                character1Image = (ImageView) character1View.findViewById(R.id.character_image_view);
                character1Name = (TextView) character1View.findViewById(R.id.character_name);
                character1Show = (TextView) character1View.findViewById(R.id.character_show);

                character2Image = (ImageView) character2View.findViewById(R.id.character_image_view);
                character2Name = (TextView) character2View.findViewById(R.id.character_name);
                character2Show = (TextView) character2View.findViewById(R.id.character_show);
            }
        }


        public interface VoteSubmitCallback {
            void onSubmitButtonClicked(HashMap<String, String> votedCharacters);
        }
    }

