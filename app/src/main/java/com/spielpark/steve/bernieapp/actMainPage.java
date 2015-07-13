package com.spielpark.steve.bernieapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;

import com.spielpark.steve.bernieapp.fragments.BottomNavFragment;
import com.spielpark.steve.bernieapp.fragments.EventFragment;
import com.spielpark.steve.bernieapp.fragments.IssuesFragment;
import com.spielpark.steve.bernieapp.fragments.NavigationDrawerFragment;
import com.spielpark.steve.bernieapp.fragments.NewsFragment;
import com.spielpark.steve.bernieapp.fragments.SingleIssueFragment;
import com.spielpark.steve.bernieapp.wrappers.Event;
import com.spielpark.steve.bernieapp.wrappers.Issue;


public class actMainPage extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, BottomNavFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    
    private BottomNavFragment mBottomNavFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

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
        mBottomNavFragment = (BottomNavFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_navigation);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#147FD7")));
        mTitle = "News";
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment replacement;
        switch(position) {
            case 0 : {
                replacement = new NewsFragment();
                break;
            }
            case 1 : {
                replacement = new IssuesFragment();
                break;
            }
            default:  {
                replacement = new NewsFragment();
            }
        }
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
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void loadEvent(Event e) {
        Fragment f = EventFragment.newInstance(e);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().addToBackStack(null).replace(R.id.container, f).commit();
    }

    public void loadIssue(Issue i) {
        Fragment f = SingleIssueFragment.newInstance(i);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().addToBackStack(null).replace(R.id.container, f).commit();
    }

}
