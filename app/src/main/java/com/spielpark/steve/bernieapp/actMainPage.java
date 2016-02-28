package com.spielpark.steve.bernieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.spielpark.steve.bernieapp.fragments.BernRateFragment;
import com.spielpark.steve.bernieapp.fragments.ConnectFragment;
import com.spielpark.steve.bernieapp.fragments.DonateFragment;
import com.spielpark.steve.bernieapp.fragments.FeedbackFragment;
import com.spielpark.steve.bernieapp.fragments.IssuesFragment;
import com.spielpark.steve.bernieapp.fragments.NavigationDrawerFragment;
import com.spielpark.steve.bernieapp.fragments.NewsFragment;
import com.spielpark.steve.bernieapp.fragments.OrganizeFragment;
import com.spielpark.steve.bernieapp.fragments.SingleIssueFragment;
import com.spielpark.steve.bernieapp.fragments.SingleNewsFragment;
import com.spielpark.steve.bernieapp.tasks.NewsTask;
import com.spielpark.steve.bernieapp.wrappers.Issue;
import com.spielpark.steve.bernieapp.wrappers.NewsArticle;


public class actMainPage extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static SharedPreferences preferences;
    private static Fragment curFrag;

    private CharSequence mTitle;

    private GoogleApiClient client;

    @Override
    protected void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "actMainPage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.spielpark.steve.bernieapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_page);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#147FD7")));
        mTitle = "News";
        preferences = getApplicationContext().getSharedPreferences("bernie_app_prefs", 0);
        adjustNavBarText(0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (curFrag instanceof ConnectFragment) {
            ConnectFragment.cancelTask(); //cancel loading the map.
        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack("base", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment replacement;
        switch (position) {
            case 0: {
                replacement = NewsFragment.getInstance();
                break;
            }
            case 1: {
                replacement = IssuesFragment.getInstance();
                break;
            }
            case 2: {
                replacement = OrganizeFragment.getInstance();
                break;
            }
            case 3: {
                replacement = ConnectFragment.getInstance();
                break;
            }
            case 4: {
                replacement = BernRateFragment.getInstance();
                break;
            }
            case 5: {
                replacement = DonateFragment.getInstance();
                break;
            }
            case 6: {
                replacement = FeedbackFragment.getInstance();
                break;
            }
            default: {
                replacement = NewsFragment.getInstance();
            }
        }
        if (replacement.isAdded()) return;
        adjustNavBarText(position);
        curFrag = replacement;
        onSectionAttached(++position);
        fragmentManager.beginTransaction()
                .replace(R.id.container, replacement)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            //return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (curFrag instanceof ConnectFragment) {
            ((ConnectFragment) curFrag).backPressed();
            return;
        } else if (curFrag instanceof OrganizeFragment) {
            if (((OrganizeFragment) curFrag).canGoBack()) {
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack("base", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void loadEvent(NewsArticle e) {
        Fragment f = SingleNewsFragment.getInstance(e);
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        }
        manager.beginTransaction().addToBackStack("base").replace(R.id.container, f).commit();
        curFrag = f;
    }

    public void loadIssue(Issue i) {
        Fragment f = SingleIssueFragment.newInstance(i);
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        }
        manager.beginTransaction().addToBackStack("base").replace(R.id.container, f).commit();
        curFrag = f;
    }

    public SharedPreferences getPrefs() {
        return this.preferences;
    }

    public void loadHeaderArticle(View view) {
        if (NewsTask.getData() != null) {
            for (NewsArticle a : NewsTask.getData()) {
                if (a.getUrl().contains("press-release")) {
                    this.loadEvent(a);
                    break;
                }
            }
        }
    }

    public void adjustNavBarText(int selected) {
        TextView[] views = new TextView[]{
                (TextView) findViewById(R.id.newsTxt),
                (TextView) findViewById(R.id.issuesTxt),
                (TextView) findViewById(R.id.organizeTxt),
                (TextView) findViewById(R.id.connectTxt)
        };
        for (int i = 0; i < views.length; i++) {
            TextView t = views[i];
            if (t == null) {
                return;
            }
            if (i == selected) {
                t.setTextColor(Color.parseColor("#FFC207"));
            } else {
                t.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

    public void switchPage(View view) {
        int selected = Integer.parseInt((String) view.getTag());
        onNavigationDrawerItemSelected(selected);
        onSectionAttached(selected + 1);
        restoreActionBar();
        adjustNavBarText(selected);
        NavigationDrawerFragment.setSelected(selected);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "actMainPage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.spielpark.steve.bernieapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
}
