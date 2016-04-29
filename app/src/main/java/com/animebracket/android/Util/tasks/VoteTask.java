package com.animebracket.android.Util.tasks;

import android.os.AsyncTask;

import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.goebl.david.Webb;

import java.util.Map;

/**
 * Created by noah on 4/18/2016.
 */
public class VoteTask extends AsyncTask<VoteTask.Request, Void, String> {

    JsonStringCallback callback;
    private Webb webb;

    public VoteTask(JsonStringCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        webb = Webb.create();
        webb.setBaseUri(Constants.BASE_URL);
    }

    @Override
    protected String doInBackground(Request... params) {
        Request r = params[0];

        String response = webb.post(Constants.SUBMIT_URL + "?action=vote") //I put this here becuase this is a post request, but the server checks the GET parameters for the action
                .header("Cookie", r.getCookie())
                .param("bracketId", r.getBracketID())
                .params(r.getRoundsAndCharacters())
                .ensureSuccess()
                .asString()
                .getBody();
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onJsonStringReceived(s);
    }


    public class Request {
        private Map<String, Object> roundsAndCharacters;
        private String cookie;
        private String bracketID;

        public Map<String, Object> getRoundsAndCharacters() {
            return roundsAndCharacters;
        }

        public void setRoundsAndCharacters(Map<String, Object> roundsAndCharacters) {
            this.roundsAndCharacters = roundsAndCharacters;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getBracketID() {
            return bracketID;
        }

        public void setBracketID(String bracketID) {
            this.bracketID = bracketID;
        }
    }

    public interface VoteCallback {
        void onVoteSubmitted(String json);
    }
}
