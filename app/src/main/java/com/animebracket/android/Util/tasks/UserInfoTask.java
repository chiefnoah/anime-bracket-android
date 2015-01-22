package com.animebracket.android.Util.tasks;

import android.os.AsyncTask;

import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONObject;

/**
 * Created by Noah Pederson on 1/22/2015.
 */
public class UserInfoTask extends AsyncTask<String, Void, String> {

    Webb webb;
    JsonStringCallback callback;

    public UserInfoTask(JsonStringCallback callback){
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

        Response<JSONObject> response = webb
                .post(Constants.REDDIT_LOGGED_IN_USER_DETAILS_URL)
                .param("Cookie", cookie)
                .ensureSuccess()
                .asJsonObject();

        String jsonString = response.toString();

        return null;
    }
}
