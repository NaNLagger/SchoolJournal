package com.nanlagger.schooljournal.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import com.nanlagger.schooljournal.app.R;
import com.nanlagger.schooljournal.app.entities.JournalEntities;
import com.nanlagger.schooljournal.app.entities.LessonEntities;
import com.nanlagger.schooljournal.app.entities.MessageEntities;
import com.nanlagger.schooljournal.app.entities.ScopeEntities;
import com.nanlagger.schooljournal.app.fragments.MessagesFragment;
import com.nanlagger.schooljournal.app.fragments.NavigationDrawerFragment;
import com.nanlagger.schooljournal.app.fragments.ProfileFragment;
import com.nanlagger.schooljournal.app.fragments.ScopesFragment;
import com.nanlagger.schooljournal.app.utils.*;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    private DBHelper dbHelper;

    private int fragmentID = 0;
    private TaskCallback taskCallback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = DBHelper.getHelper(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SharedPreferences preferences = getSharedPreferences("com.nanlagger.schooljournal", MODE_PRIVATE);
        AsyncLoader loader;
        fragmentID = position;
        switch (position) {
            case 0:
                ProfileFragment fragment = ProfileFragment.newInstance(position + 1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                taskCallback = fragment;
                loader = new AsyncProfileLoader(fragment);
                loader.execute(preferences.getString("login",""),preferences.getString("password",""));
                break;
            case 1:
                ScopesFragment fragment1 = ScopesFragment.newInstance(position + 1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment1)
                        .commit();
                taskCallback = fragment1;
                JournalEntities.getInstance().save();
                loader = new AsyncJournalsLoader(fragment1);
                loader.execute(preferences.getString("login",""),preferences.getString("password",""));
                LessonEntities.getInstance().save();
                AsyncLessonsLoader loader1 = new AsyncLessonsLoader(fragment1);
                loader1.execute(preferences.getString("login",""),preferences.getString("password",""));
                ScopeEntities.getInstance().save();
                AsyncScopesLoader loader2 = new AsyncScopesLoader(fragment1);
                loader2.execute(preferences.getString("login",""),preferences.getString("password",""));
                break;
            case 2:
                MessagesFragment fragment2 = MessagesFragment.newInstance(position + 1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment2)
                        .commit();
                taskCallback = fragment2;
                MessageEntities.getInstance().save();
                loader = new AsyncMessageLoader(fragment2);
                loader.execute(preferences.getString("login",""),preferences.getString("password",""));
                break;
            case 3:
                logout();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;
        }

    }

    public void updateFragment() {
        SharedPreferences preferences = getSharedPreferences("com.nanlagger.schooljournal", MODE_PRIVATE);
        AsyncLoader loader;
        switch (fragmentID) {
            case 0:
                loader = new AsyncProfileLoader(taskCallback);
                loader.execute(preferences.getString("login",""),preferences.getString("password",""));
                break;
            case 1:
                JournalEntities.getInstance().save();
                loader = new AsyncJournalsLoader(taskCallback);
                loader.execute(preferences.getString("login",""),preferences.getString("password",""));
                LessonEntities.getInstance().save();
                AsyncLessonsLoader loader1 = new AsyncLessonsLoader(taskCallback);
                loader1.execute(preferences.getString("login",""),preferences.getString("password",""));
                ScopeEntities.getInstance().save();
                AsyncScopesLoader loader2 = new AsyncScopesLoader(taskCallback);
                loader2.execute(preferences.getString("login",""),preferences.getString("password",""));
                break;
            case 2:
                MessageEntities.getInstance().save();
                loader = new AsyncMessageLoader(taskCallback);
                loader.execute(preferences.getString("login",""),preferences.getString("password",""));
                break;
        }
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
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void logout() {
        SharedPreferences preferences = getSharedPreferences("com.nanlagger.schooljournal", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("profile", null, null);
        db.delete("messages", null, null);
        db.delete("lessons", null, null);
        db.delete("journals", null, null);
        db.delete("scopes", null, null);
        db.close();
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
