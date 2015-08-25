package com.spielpark.steve.bernieapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.fragments.BernRateFragment;
import com.spielpark.steve.bernieapp.fragments.ConnectFragment;
import com.spielpark.steve.bernieapp.fragments.SingleNewsFragment;
import com.spielpark.steve.bernieapp.fragments.IssuesFragment;
import com.spielpark.steve.bernieapp.fragments.NavigationDrawerFragment;
import com.spielpark.steve.bernieapp.fragments.NewsFragment;
import com.spielpark.steve.bernieapp.fragments.OrganizeFragment;
import com.spielpark.steve.bernieapp.fragments.SingleIssueFragment;
import com.spielpark.steve.bernieapp.tasks.IssuesTask;
import com.spielpark.steve.bernieapp.tasks.NewsTask;
import com.spielpark.steve.bernieapp.wrappers.Issue;
import com.spielpark.steve.bernieapp.wrappers.NewsArticle;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static SharedPreferences preferences;
    private static Fragment curFrag;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onStop() {
        super.onStop();
        IssuesTask.clear();
        NewsTask.clear();
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
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack("base", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment replacement;
        switch(position) {
            case 0 : {
                replacement = NewsFragment.getInstance();
                break;
            }
            case 1 : {
                replacement = IssuesFragment.getInstance();
                break;
            }
            case 2 : {
                replacement = OrganizeFragment.getInstance();
                break;
            }
            case 3 : {
                replacement = ConnectFragment.getInstance();
                break;
            }
            case 4 : {
                replacement = BernRateFragment.getInstance();
                break;
            }
            default:  {
                replacement = NewsFragment.getInstance();
            }
        }
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
    public void onBackPressed() {
        if (curFrag instanceof ConnectFragment) {
            ((ConnectFragment) curFrag).backPressed();
            return;
        } else if (curFrag instanceof NewsFragment) {

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
    }

    public void loadIssue(Issue i) {
        Fragment f = SingleIssueFragment.newInstance(i);
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        }
        manager.beginTransaction().addToBackStack("base").replace(R.id.container, f).commit();
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
        TextView[] views = new TextView[] {
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
}
