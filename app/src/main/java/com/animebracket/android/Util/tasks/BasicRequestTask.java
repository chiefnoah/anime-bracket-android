package com.animebracket.android.Util.tasks;

import android.os.AsyncTask;

import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.goebl.david.Response;
import com.goebl.david.Webb;

/**
 * Created by Noah Pederson on 1/22/2015.
 */
public class BasicRequestTask extends AsyncTask<String, Void, String> {

    Webb webb;
    JsonStringCallback callback;

    public BasicRequestTask(JsonStringCallback callback){
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
        try {
            String endpoint = params[0];
            String cookie = params[1];
            Response<String> response = webb
                    .get(endpoint)
                    .header("Cookie", cookie)
                    .ensureSuccess()
                    .asString();

            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param s The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onJsonStringReceived(s);
    }
}
