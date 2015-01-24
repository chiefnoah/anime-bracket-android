package com.animebracket.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.animebracket.android.Util.Constants;
import com.animebracket.android.Util.beans.UserInfo;
import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.tasks.UserInfoTask;
import com.animebracket.android.fragments.LoginFragment;
import com.animebracket.android.fragments.RunningBracketsFragment;
import com.animebracket.android.fragments.StartupFragment;
import com.google.gson.Gson;


public class MainActivity extends ActionBarActivity implements JsonStringCallback, LoginFragment.LoginFragmentCallback {

    private String[] navBarTextItems;
    private ListView navBarListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBarTextItems = getResources().getStringArray(R.array.nav_drawer_buttons_text);
        navBarListView = (ListView) findViewById(R.id.nav_bar_list_view);

        //Set up the navbar
        navBarListView.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, navBarTextItems));
        navBarListView.setOnItemClickListener(new DrawerItemClickListener());

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
    public void onBackPressed() {
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
        //If the string is null, it means the device couldn't reach the animebracket servers
        if(jsonString == null) {
            Log.d(Constants.JSON, "Unable to connect to animebracket.com");
            //TODO: Show dialogue box
        }
        UserInfo userInfo = null;
        try {
            Gson gson = new Gson();
            userInfo = gson.fromJson(jsonString, UserInfo.class);
            Log.d(Constants.JSON, "Loaded json from http request: " + jsonString);
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

    //Click listener for the navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position) {
        //TODO: Switch fragments when an item is clicked
    }
}
