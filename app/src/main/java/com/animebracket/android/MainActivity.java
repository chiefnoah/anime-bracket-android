package com.animebracket.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.beans.UserInfo;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.tasks.UserInfoTask;
import com.animebracket.android.fragments.LoginFragment;
import com.animebracket.android.fragments.RunningBracketsFragment;
import com.animebracket.android.fragments.StartupFragment;
import com.google.gson.Gson;


public class MainActivity extends Activity implements JsonStringCallback, LoginFragment.LoginFragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize singleton cookie store
        CookieSyncManager.createInstance(this); //Deprecated in API 21, but because we're supporting
        //back to API 14, we need to use this.

        UserInfoTask userInfoTask = new UserInfoTask(this);
        String cookie = CookieManager.getInstance().getCookie(Constants.BASE_URL) + "";
        userInfoTask.execute(cookie);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new StartupFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //TODO: Open settings activity
            return true;
        } else if(id ==R.id.action_logout) {
            //Clear cookies and switch to login fragment
            CookieManager.getInstance().removeAllCookie();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment())
                    .commit();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onJsonStringReceived(String jsonString) {
        UserInfo userInfo = null;
        try {
            Gson gson = new Gson();
            userInfo = gson.fromJson(jsonString, UserInfo.class);
            Log.d(Constants.JSON, "Loaded json from http request");
        } catch (Exception e) {}

        //If userInfo is null, you aren't logged in and it should show the login fragment
        if(userInfo == null) {
            CookieManager.getInstance().removeAllCookie();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment())
                    .commit();
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
        } else {
            //This means you're logged in. Load up the brackets :D
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new RunningBracketsFragment())
                    .commit();
        }
    }

    @Override
    public void onLoginFinish() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new RunningBracketsFragment())
                .commit();
    }
}
