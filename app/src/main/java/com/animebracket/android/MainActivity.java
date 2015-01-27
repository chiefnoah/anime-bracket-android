package com.animebracket.android;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.beans.Bracket;
import com.animebracket.android.Util.beans.UserInfo;
import com.animebracket.android.Util.callbacks.BracketCardActionCallback;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.tasks.BasicRequestTask;
import com.animebracket.android.fragments.LoginFragment;
import com.animebracket.android.fragments.NominateFragment;
import com.animebracket.android.fragments.RulesFragment;
import com.animebracket.android.fragments.RunningBracketsFragment;
import com.animebracket.android.fragments.StartupFragment;
import com.google.gson.Gson;


public class MainActivity extends ActionBarActivity implements JsonStringCallback,
        LoginFragment.LoginFragmentCallback, BracketCardActionCallback {

    private LinearLayout navDrawerLayout;
    private DrawerLayout navDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private TextView redditUsernameTextView;

    BasicRequestTask basicRequestTask;

    UserInfo user;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerLayout = (LinearLayout) findViewById(R.id.nav_drawer);
        redditUsernameTextView = (TextView) findViewById(R.id.reddit_username_text_view);

        drawerToggle = new ActionBarDrawerToggle(this, navDrawer, R.string.app_name, R.string.app_name) {
        };

        //Set up the navbar
        navDrawer.setDrawerListener(drawerToggle);

        //Set up actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Initialize singleton cookie store
        CookieSyncManager.createInstance(this); //Deprecated in API 21, but because we're supporting
        //back to API 14, we need to use this.




        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new StartupFragment())
                    .commit();
            if(user == null) {
                basicRequestTask = new BasicRequestTask(this);
                String cookie = CookieManager.getInstance().getCookie(Constants.BASE_URL) + "";
                basicRequestTask.execute(Constants.USER_DETAILS_URL, cookie);
            }
        } else {
            //Set user if it the instance state has been saved
            user = (UserInfo) savedInstanceState.getSerializable(Constants.FLAGS.USER_INFO_BUNDLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(user != null) {
            outState.putSerializable(Constants.FLAGS.USER_INFO_BUNDLE, user);
        }
        super.onSaveInstanceState(outState);
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

        //Close the nav drawer if open
        if (navDrawer.isDrawerOpen(navDrawerLayout)) {
            navDrawer.closeDrawer(navDrawerLayout);
            drawerToggle.syncState();
            return;
        }
        //If the current fragment is a login fragment, call that fragments onBackPressed method
        //If not, call the default onBackPressed method
        try {
            LoginFragment cFragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.container);
            cFragment.onBackPressed();
            return;
        } catch (ClassCastException ex) {
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager fm = getFragmentManager();
            getFragmentManager().popBackStackImmediate();
            return;
        }

        super.onBackPressed();
    }


    /*
    ===============================================
    ************* Interface Callbacks *************
    ===============================================
     */
    @Override
    public void onJsonStringReceived(String jsonString) {
        //If the string is null, it means the device couldn't reach the animebracket servers
        if (jsonString == null) {
            Log.d(Constants.FLAGS.JSON, "Unable to connect to animebracket.com");
            //TODO: Show dialogue box
            user = null;
        }
        try {
            Gson gson = new Gson();
            user = gson.fromJson(jsonString, UserInfo.class);
            Log.d(Constants.FLAGS.JSON, "Loaded json from http request: " + jsonString);
        } catch (Exception e) {
            user = null;
        }

        //If userInfo is null, you aren't logged in and it should show the login fragment
        if (user == null) {
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
            redditUsernameTextView.setText("/u/" + user.getName());
        }
    }

    @Override
    public void onLoginFinish() {
        //Double check to make sure the cookie worked
        CookieSyncManager.getInstance().sync();
        basicRequestTask = new BasicRequestTask(this);
        String cookie = CookieManager.getInstance().getCookie(Constants.BASE_URL) + "";
        basicRequestTask.execute(Constants.USER_DETAILS_URL, cookie);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new StartupFragment())
                .commit();
    }

    @Override
    public void onActionButtonClick(Fragment fragment, Bracket bracket) {
        //Check bracket state. If it's in nominations, switch to the rules fragment. If it's in voting, open voting bracket, if final open completed brackets fragment
        Fragment newFragment = null;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (bracket.getState()) {
            case Constants.BRACKET_STATE_NOMINATIONS:
                //switch to rules fragment if fragment is instance of RunningBracketsFragment, else switch to nomination fragment
                if (fragment.getClass() == RunningBracketsFragment.class) {
                    newFragment = new RulesFragment();

                } else {
                    newFragment = new NominateFragment();

                }
                Bundle args = new Bundle();
                args.putSerializable(Constants.FLAGS.BRACKET_ARG, bracket);
                newFragment.setArguments(args);
                break;
            case Constants.BRACKET_STATE_ELIMINATIONS:
                //TODO: switch to eliminations fragment
                break;
            case Constants.BRACKET_STATE_VOTING:
                //TODO: Switch to voting fragment
                break;
            case Constants.BRACKET_STATE_WILDCARD:
                //This is basically the same as eliminations. It won't be used anymore... probably
                break;
            case Constants.BRACKET_STATE_FINAL:
                break;
            default:
                Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
        }
        FragmentManager fm = getFragmentManager();
        int derp = 9;

        //TODO: have a boolean determine if the fragment transaction should be added to the backstack
        if (newFragment != null) {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.container, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    /*
    ===============================================
    ************** View Click Events **************
    ===============================================
     */

    public void logOut(View v) {
        //Clear cookies and switch to login fragment
        CookieManager.getInstance().removeAllCookie();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        user = null;
        redditUsernameTextView.setText("Log in");
        //Close the nav drawer if open
        if (navDrawer.isDrawerOpen(navDrawerLayout)) {
            navDrawer.closeDrawer(navDrawerLayout);
            drawerToggle.syncState();
        }
    }

    public void openReddit(View v) {
        if (user == null) {
            logOut(v); //This will open the login fragment
            return;
        }
        String url = Constants.REDDIT_URL + redditUsernameTextView.getText();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void openSettings(View v) {
        //TODO: open setting activity

        //Close the nav drawer if open
        if (navDrawer.isDrawerOpen(navDrawerLayout)) {
            navDrawer.closeDrawer(navDrawerLayout);
            drawerToggle.syncState();
        }
    }

    public void onCurrentBracketClick(View v) {

        //Clear the back stack
        getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new RunningBracketsFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        //Close the nav drawer if open
        if (navDrawer.isDrawerOpen(navDrawerLayout)) {
            navDrawer.closeDrawer(navDrawerLayout);
            drawerToggle.syncState();
        }
    }

    public void onPastBracketClick(View v) {
        //TODO: switch to past bracket fragment

        //Close the nav drawer if open
        if (navDrawer.isDrawerOpen(navDrawerLayout)) {
            navDrawer.closeDrawer(navDrawerLayout);
            drawerToggle.syncState();
        }
    }
}
