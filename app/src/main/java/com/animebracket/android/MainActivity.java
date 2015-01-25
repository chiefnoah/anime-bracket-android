package com.animebracket.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.animebracket.android.Util.CONSTANTS;
import com.animebracket.android.Util.beans.UserInfo;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.tasks.BasicRequestTask;
import com.animebracket.android.fragments.LoginFragment;
import com.animebracket.android.fragments.RunningBracketsFragment;
import com.animebracket.android.fragments.StartupFragment;
import com.google.gson.Gson;


public class MainActivity extends ActionBarActivity implements JsonStringCallback, LoginFragment.LoginFragmentCallback {

    private LinearLayout navDrawerLayout;
    private DrawerLayout navDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private TextView redditUsernameTextView;

    private SharedPreferences globalSharedPreferences;
    private SharedPreferences.Editor globalSharedEditor;

    BasicRequestTask basicRequestTask;

    private boolean loggedIn = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerLayout = (LinearLayout) findViewById(R.id.nav_drawer);
        redditUsernameTextView = (TextView) findViewById(R.id.reddit_username_text_view);

        drawerToggle = new ActionBarDrawerToggle(this, navDrawer, R.string.app_name, R.string.app_name) {};
        globalSharedPreferences = getSharedPreferences(CONSTANTS.FLAGS.GLOBAL_PREFERENCES, MODE_MULTI_PROCESS);
        globalSharedEditor = globalSharedPreferences.edit();

        //Set up the navbar
        navDrawer.setDrawerListener(drawerToggle);

        //Set up actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Initialize singleton cookie store
        CookieSyncManager.createInstance(this); //Deprecated in API 21, but because we're supporting
        //back to API 14, we need to use this.

        basicRequestTask = new BasicRequestTask(this);
        String cookie = CookieManager.getInstance().getCookie(CONSTANTS.BASE_URL) + "";
        basicRequestTask.execute(CONSTANTS.USER_DETAILS_URL, cookie);

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
    protected void onStop() {
        super.onStop();
        basicRequestTask.cancel(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if(navDrawer.isDrawerOpen(navDrawerLayout)) {
            navDrawer.closeDrawer(navDrawerLayout);
            drawerToggle.syncState();
            return;
        }
        //If the current fragment is a login fragment, call that fragments onBackPressed method
        //If not, call the default onBackPressed method
        try {
            LoginFragment cFragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.container);
            cFragment.onBackPressed();
        } catch (ClassCastException ex) {
            super.onBackPressed();
        }
    }

    @Override
    public void onJsonStringReceived(String jsonString) {
        //If the string is null, it means the device couldn't reach the animebracket servers
        if(jsonString == null) {
            Log.d(CONSTANTS.FLAGS.JSON, "Unable to connect to animebracket.com");
            //TODO: Show dialogue box
            loggedIn = false;
        }
        UserInfo userInfo = null;
        try {
            Gson gson = new Gson();
            userInfo = gson.fromJson(jsonString, UserInfo.class);
            Log.d(CONSTANTS.FLAGS.JSON, "Loaded json from http request: " + jsonString);
        } catch (Exception e) {}

        //If userInfo is null, you aren't logged in and it should show the login fragment
        if(userInfo == null) {
            CookieManager.getInstance().removeAllCookie();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment())
                    .commit();
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            loggedIn = false;
            globalSharedEditor.putBoolean(CONSTANTS.FLAGS.LOGGEDIN, false).commit();
        } else {
            //This means you're logged in. Load up the brackets :D
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new RunningBracketsFragment())
                    .commit();
            globalSharedEditor.putBoolean(CONSTANTS.FLAGS.LOGGEDIN, true).commit();
            redditUsernameTextView.setText("/u/" + userInfo.getName());
            loggedIn = true;
        }
    }

    @Override
    public void onLoginFinish() {
        //Double check to make sure the cookie worked
        CookieSyncManager.getInstance().sync();
        basicRequestTask = new BasicRequestTask(this);
        String cookie = CookieManager.getInstance().getCookie(CONSTANTS.BASE_URL) + "";
        basicRequestTask.execute(CONSTANTS.USER_DETAILS_URL, cookie);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new StartupFragment())
                .commit();
    }

    public void logOut(View v) {
        //Clear cookies and switch to login fragment
        CookieManager.getInstance().removeAllCookie();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        loggedIn = false;
        redditUsernameTextView.setText("Log in");
        navDrawer.closeDrawer(navDrawerLayout);
        drawerToggle.syncState();
    }

    public void openReddit(View v) {
        if(!loggedIn) {
            logOut(v);
            return;
        }
        String url = CONSTANTS.REDDIT_URL + redditUsernameTextView.getText();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
