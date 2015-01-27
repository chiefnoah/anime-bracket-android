package com.animebracket.android.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.animebracket.android.R;
import com.animebracket.android.Util.Constants;

/**
 * Created by Noah Pederson on 1/22/2015.
 */
public class LoginFragment extends Fragment {

    LoginFragmentCallback callback;
    WebView rootView;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (WebView) inflater.inflate(R.layout.fragment_login, container, false);
        rootView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.equals(Constants.BASE_URL + "/") && callback != null) {
                    CookieSyncManager.getInstance().sync(); //Force sync cookies
                    //The null check is probably redundant, but who cares
                    String cookie = CookieManager.getInstance().getCookie(url);
                    callback.onLoginFinish();
                }
            }

        });

        rootView.loadUrl(Constants.BASE_URL + Constants.REDDIT_OAUTH_URL);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (LoginFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginFragmentCallback");
        }
    }

    public boolean onBackPressed() {
        if(rootView.canGoBack()) {
            rootView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface LoginFragmentCallback {
        public void onLoginFinish();
    }
}
