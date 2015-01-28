package com.animebracket.android.Util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.animebracket.android.R;
import com.animebracket.android.Util.beans.CharacterInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Noah Pederson on 1/27/2015.
 */
public class AutoCompleteNameAdapter extends ArrayAdapter<CharacterInfo> implements Filterable {
    Context context;
    int resource;

    public AutoCompleteNameAdapter(Context context, int resource, ArrayList<CharacterInfo> suggestedCharacters) {
        super(context, resource, suggestedCharacters);
        this.context = context;
        this.resource = resource;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Bind views!
        ViewHolder viewHolder;
        View rootView = convertView;
        if(rootView == null) {
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(inflater);
            rootView = layoutInflater.inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.characterImage = (ImageView) rootView.findViewById(R.id.suggested_character_image_view);
            viewHolder.characterName = (TextView) rootView.findViewById(R.id.suggested_character_name_text_view);
            viewHolder.characterSource = (TextView)rootView.findViewById(R.id.suggested_character_source_text_view);

            rootView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rootView.getTag();
        }

        //Set the character info values to the
        CharacterInfo character = getItem(position);
        Picasso.with(context).load(character.getImage()).into(viewHolder.characterImage);
        viewHolder.characterName.setText(character.getName());
        viewHolder.characterSource.setText(character.getSource());

        return rootView;
    }

    private static class ViewHolder {
        ImageView characterImage;
        TextView characterName;
        TextView characterSource;
    }

}


