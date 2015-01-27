package com.animebracket.android.Util.callbacks;

import android.app.Fragment;

import com.animebracket.android.Util.beans.Bracket;

/**
 * Created by Noah Pederson on 1/25/2015.
 */
public interface BracketCardActionCallback {
    public void onActionButtonClick(Fragment fragment, Bracket bracket);
}
