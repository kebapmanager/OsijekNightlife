package com.androidapp.osijeknightlife.app;

import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.androidapp.osijeknightlife.app.TabFragments.GridFragment;
import com.androidapp.osijeknightlife.app.TabFragments.ListFragment;
import com.androidapp.osijeknightlife.app.jsonDataP.GetData;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,GetData.Listener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    GetData DW = new GetData(this);

    Fragment events;
    Fragment clubs;

    public void dataRecieved(boolean state)
    {
        if(state)
            switchTab(0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        events = ListFragment.newInstance(0);
        clubs = GridFragment.newInstance(1);

        DW.registerListener(this);
        DW.Start("/15/6/","13");

        // Set up the action bar.
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        // Set up the ViewPager with the sections adapter.
        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // For each of the sections in the app, add a tab to the action bar.
        // Create a tab with text corresponding to the page title defined by
        // the adapter. Also specify this Activity object, which implements
        // the TabListener interface, as the callback (listener) for when
        // this tab is selected.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab()
                .setText("Eventovi")
//                .setIcon(R.mipmap.ic_launcher)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab()
                .setText("Klubovi")
                .setTabListener(this));
//        actionBar.addTab(actionBar.newTab()
//                .setText("Search")
//                .setTabListener(this));
    }
    public void switchTab(int position)
    {
        switch(position)
        {
            case 0:
                getSupportFragmentManager().findFragmentById(R.id.list_layout);
                ListFragment.changeLayoutProperties();
                break;
            case 1:
                GridFragment.changeLayoutProperties();
                getSupportFragmentManager().findFragmentById(R.id.grid_layout);
//                ClubFragment.changeLayoutProperties(DW.data.getEvents().get(0), DW.pictures);
                break;
//            case 3:
//                getSupportFragmentManager().findFragmentById(R.id.search_layout);
//                SearchFragment.changeLayoutProperties();
//                break;
        }
    }
    private ShareActionProvider share;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.share);
        share = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        MenuItemCompat.getActionProvider(menuItem);

        return true;
    }
    // Call to update the share intent
    private void setShareIntent(Intent shareIntent)
    {
        if (share != null) {
            share.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        System.out.println("onTabSelected");
        if (DW.info)
        {
            System.out.println("DW done");
            switchTab(tab.getPosition());
        }else System.out.println("Not done");

        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}


    public void searchButton(View view)
    {
        // template of opening new activity when button is pressed
/*        Intent intent = new Intent(this, DisplayMessageActivity.class);

        // This is how to get text from the input I think
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);*/
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment;
            switch(position)
            {
                case 0:
                    fragment = events;
                    break;
                case 1:
                    fragment = clubs;
                    break;
//                case 2:
//                    fragment = SearchFragment.newInstance(position);
//                    break;
                default:
                    fragment = new Fragment();
                    break;
            }
            return fragment;
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            Locale l = Locale.getDefault();
            switch (position)
            {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
//                case 2:
//                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}