package com.spielpark.steve.bernieapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.spielpark.steve.bernieapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrganizeFragment extends Fragment {

    private static OrganizeFragment mInstance;
    private static WebView browser;

    public static OrganizeFragment getInstance() {
        if (mInstance == null) {
            mInstance = new OrganizeFragment();
            return mInstance;
        } else {
            return mInstance;
        }
    }

    public static boolean canGoBack() {
        if (browser.canGoBack()) {
            browser.goBack();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        browser = (WebView) getView().findViewById(R.id.o_webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setAppCachePath(getActivity().getCacheDir().getPath());
        browser.getSettings().setAppCacheEnabled(true);
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                Toast.makeText(getActivity(), "Logging in..", Toast.LENGTH_SHORT);
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
                Toast.makeText(getActivity(), "Logging in -HTTP..", Toast.LENGTH_SHORT);
            }
        });
        browser.loadUrl("https://berniecrowd.org/?utm_source=bernie-app&utm_medium=android&utm_campaign=bernie-app");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_organize, container, false);
    }
}
