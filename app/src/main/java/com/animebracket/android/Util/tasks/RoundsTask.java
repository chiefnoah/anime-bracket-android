package com.animebracket.android.Util.tasks;

import android.os.AsyncTask;

import com.animebracket.android.Util.Constants;
import com.goebl.david.Webb;

/**
 * Created by noah on 4/28/2016.
 */
public class RoundsTask extends AsyncTask<String, Void, String> {

    Webb webb;
    RoundsCallback callback;

    public RoundsTask(RoundsCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        webb = Webb.create();
        webb.setBaseUri(Constants.BASE_URL);
    }

    @Override
    protected String doInBackground(String... params) {
        String bracketID = params[0];
        String cookie = params[1];
        return webb.get(Constants.BRACKET_ACTIVE_ROUND_URL).header("Cookie", cookie)
                .param("bracketId", bracketID).ensureSuccess().asString().getBody();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onRoundsReceived(s);
    }

    public interface RoundsCallback {
        void onRoundsReceived(String json);
    }
}
