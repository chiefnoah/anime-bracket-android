package com.animebracket.android.Util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.animebracket.android.R;
import com.animebracket.android.Util.beans.CharacterInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Noah Pederson on 1/27/2015.
 */
public class AutoCompleteNameAdapter extends ArrayAdapter<CharacterInfo> implements Filterable {
    Context context;
    int resource;
    ArrayList<CharacterInfo> suggestedCharacters;


    public AutoCompleteNameAdapter(Context context, int resource, ArrayList<CharacterInfo> suggestedCharacters) {
        super(context, resource, suggestedCharacters);
        this.context = context;
        this.resource = resource;
        this.suggestedCharacters = suggestedCharacters;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Bind views!
        ViewHolder viewHolder;
        View rootView = convertView;
        final CharacterInfo character = suggestedCharacters.get(position);
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

        Picasso.with(context).load(character.getImage()).into(viewHolder.characterImage);
        viewHolder.characterName.setText(character.getName());
        viewHolder.characterSource.setText(character.getSource());

        return rootView;
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    @Override
    public void add(CharacterInfo object) {
        //super.add(object);
        suggestedCharacters.add(object);
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    @Override
    public void addAll(Collection<? extends CharacterInfo> collection) {
        //super.addAll(collection);
        suggestedCharacters.addAll(collection);
    }

    /**
     * Remove all elements from the list.
     */
    @Override
    public void clear() {
        //super.clear();
        suggestedCharacters.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return suggestedCharacters.size();
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     */
    @Override
    public CharacterInfo getItem(int position) {
        return suggestedCharacters.get(position);
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                if(suggestedCharacters == null) {
                    suggestedCharacters = new ArrayList<CharacterInfo>();
                }

                final FilterResults filterResults = new FilterResults();
                //filterResults.values = suggestedCharacters;
                //filterResults.count = suggestedCharacters.size();
                return filterResults;
            }

            @Override
            protected void publishResults(final CharSequence constraint, final FilterResults results) {
                /*clear();
                for(CharacterInfo characterInfo : (ArrayList<CharacterInfo>) results.values) {
                    add(characterInfo);
                }
                if(results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    otifyDataSetInvalidated();
                } */
            }

            @Override
            public CharSequence convertResultToString(final Object resultValue) {
                return resultValue == null ? "" : ((CharacterInfo) resultValue).getName();
            }

        };
        return filter;
    }





    private static class ViewHolder {
        ImageView characterImage;
        TextView characterName;
        TextView characterSource;
    }

}


