package com.animebracket.android.Util.adapters;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.animebracket.android.R;
import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.beans.Bracket;
import com.animebracket.android.Util.beans.CharacterInfo;
import com.animebracket.android.Util.callbacks.BracketCardActionCallback;
import com.animebracket.android.fragments.RunningBracketsFragment;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Noah Pederson on 1/24/2015.
 */
public class CurrentBracketsAdapter extends RecyclerView.Adapter<CurrentBracketsAdapter.ViewHolder> {
    ArrayList<Bracket> currentBrackets;
    BracketCardActionCallback callback;
    RunningBracketsFragment fragment;

    public CurrentBracketsAdapter(RunningBracketsFragment fragment, BracketCardActionCallback callback, ArrayList<Bracket> currentBrackets) {
        this.currentBrackets = currentBrackets;
        this.callback = callback;
        this.fragment = fragment;
    }

    @Override
    public CurrentBracketsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_bracket_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(CurrentBracketsAdapter.ViewHolder holder, int position) {
        final int finalPosition = position; //This is required to access the position in the onClickListener
        Bracket cBracket = currentBrackets.get(position);
        holder.bracketTitle.setText(cBracket.getName());
        holder.actionButton.setText(Constants.BRACKET_ACTION_STATE[cBracket.getState()]);
        holder.infoText.setText(Constants.BRACKET_INFO_STATE[cBracket.getState()]);

        CharacterInfo[] cRandomCharacterInfos = cBracket.getRandomCharacterInfos();
        for(int i = 0; i < cRandomCharacterInfos.length; i++) {
            Picasso.with(holder.characterImages[i].getContext()).load(Uri.parse(cRandomCharacterInfos[i].getImage())).into(holder.characterImages[i]);
        }


        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onActionButtonClick(fragment, currentBrackets.get(finalPosition));
            }
        });
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return currentBrackets.size();
    }


    public void updateDataset(ArrayList<Bracket> newDataset) {
        currentBrackets = newDataset;
        for (int i = 0; i < currentBrackets.size(); i++) {
            RandomCharacterTask rcTask = new RandomCharacterTask(i);
            rcTask.execute();
        }
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView[] characterImages;
        public TextView bracketTitle;
        public Button actionButton;
        public TextView infoText;

        public ViewHolder(View v) {
            super(v);
            bracketTitle = (TextView) v.findViewById(R.id.current_bracket_title_text_view);
            actionButton = (Button) v.findViewById(R.id.action_button);
            infoText = (TextView) v.findViewById(R.id.info_text_view);

            characterImages = new ImageView[6];
            //Uhg... all the 9 NINE *NIIIINNEEEE* image views
            characterImages[0] = (ImageView) v.findViewById(R.id.contestant_image_view_0);
            characterImages[1] = (ImageView) v.findViewById(R.id.contestant_image_view_1);
            characterImages[2] = (ImageView) v.findViewById(R.id.contestant_image_view_2);
            characterImages[3] = (ImageView) v.findViewById(R.id.contestant_image_view_3);
            characterImages[4] = (ImageView) v.findViewById(R.id.contestant_image_view_4);
            characterImages[5] = (ImageView) v.findViewById(R.id.contestant_image_view_5);
        }


    }

    private class RandomCharacterTask extends AsyncTask<Void, Void, Void> {
        private int bracketIndex;
        public RandomCharacterTask(int bracketIndex) {
            this.bracketIndex = bracketIndex;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Webb webb = Webb.create();
                webb.setBaseUri(Constants.BASE_URL);
                Gson gson = new Gson();

                Response<String> response = webb
                        .get(Constants.CHARACTERS_LIST_URL)
                        .param("bracketId", currentBrackets.get(bracketIndex).getId())
                        .param("count", 6)
                        .ensureSuccess()
                        .asString();
                CharacterInfo[] randomCharacters = gson.fromJson(response.getBody(), CharacterInfo[].class);
                if(randomCharacters != null) {
                    currentBrackets.get(bracketIndex).setRandomCharacterInfos(randomCharacters);;
                }
            } catch (Exception e) {
                Log.e("E", "Error: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notifyDataSetChanged();
        }
    }
}
