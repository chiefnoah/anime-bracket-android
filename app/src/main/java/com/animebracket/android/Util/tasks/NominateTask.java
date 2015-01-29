package com.animebracket.android.Util.tasks;

import android.os.AsyncTask;

import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.goebl.david.Response;
import com.goebl.david.Webb;

/**
 * Created by Noah Pederson on 1/28/2015.
 */
public class NominateTask extends AsyncTask<String, Void, String> {

    Webb webb;
    JsonStringCallback callback;

    public NominateTask(JsonStringCallback callback) {
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
        String cookie = params[0];
        String bracketId = params[1];
        String name = params[2];
        String source = params[3];
        String imageLink = params[4];
        String verified = params[5];
        try {
            Response<String> response = webb.post(Constants.SUBMIT_URL + "?action=nominate")
                    .header("Cookie", cookie)
                    .param("bracketId", bracketId)
                    .param("nomineeName", name)
                    .param("nomineeSource", source)
                    .param("image", imageLink)
                    .param("verified", verified)
                    .asString();

            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onJsonStringReceived(s);
    }
}
