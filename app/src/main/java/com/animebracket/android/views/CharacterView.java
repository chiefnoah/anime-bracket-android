package com.animebracket.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.animebracket.android.R;

/**
 * Created by noah on 4/18/2016.
 */
public class CharacterView extends RelativeLayout {

    RelativeLayout root;
    ImageView image;
    TextView name;
    TextView show;

    boolean checked = false;
    int allignment = 0;

    public CharacterView(Context context) {
        this(context, null);
    }

    public CharacterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharacterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.character_view, this);
        root = (RelativeLayout) findViewById(R.id.character_layout);
        image = (ImageView) findViewById(R.id.character_image_view);
        name = (TextView) findViewById(R.id.character_name);
        show = (TextView) findViewById(R.id.character_show);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CharacterView,
                0, 0);

        try {
            checked = a.getBoolean(R.styleable.CharacterView_checked, false);
            allignment = a.getInteger(R.styleable.CharacterView_imageSide, 0);
        } finally {
            a.recycle();
        }

        LayoutParams imageParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
        LayoutParams nameParams = (RelativeLayout.LayoutParams) name.getLayoutParams();
        switch (allignment) {
            case 0:
                imageParams.addRule(ALIGN_PARENT_START, R.id.character_image_view);
                nameParams.addRule(RelativeLayout.END_OF, R.id.character_image_view);
                nameParams.removeRule(RelativeLayout.START_OF);
                break;
            case 1:
                imageParams.addRule(ALIGN_PARENT_END);
                nameParams.addRule(RelativeLayout.START_OF, R.id.character_image_view);
                nameParams.removeRule(RelativeLayout.END_OF);
                break;
        }
        image.setLayoutParams(imageParams);
        name.setLayoutParams(nameParams);
    }

    public void setChecked(boolean checked) {
        //TODO: add check animation/image
        checked = true;
        invalidate();
        requestLayout();
    }

    public boolean isChecked() {
        return checked;
    }

}
