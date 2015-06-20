package com.androidapp.osijeknightlife.app;

import java.util.Locale;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.androidapp.osijeknightlife.app.jsonDataP.GetData;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,GetData.Listener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    GetData DW = new GetData();

    public void dataRecieved(boolean state)
    {
        if(state)
            setData(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                .setText("Hot")
                .setIcon(R.mipmap.ic_launcher)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab()
                .setText("Events")
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab()
                .setText("Search")
                .setTabListener(this));
    }

    public void setData(int position)
    {
        if(position == 0)
        {
            getSupportFragmentManager().findFragmentById(R.id.club_layout);
            ClubFragment.changeLayoutProperties(DW.data.getEvents().get(0), DW.Slike, position);
        }
        else
        {
            getSupportFragmentManager().findFragmentById(R.id.list_layout);
            ListFragment.changeLayoutProperties(DW.data.getEvents().get(0));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        if (DW.done)
        {
            System.out.println("DW done");
            setData(tab.getPosition());
        }else System.out.println("Not done");

        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = ClubFragment.newInstance(position);
            switch(position)
            {
                case 0:
                    fragment = ListFragment.newInstance(position);
                    break;
                case 1:
                    fragment = ClubFragment.newInstance(position);
                    break;
                case 2:
                    fragment = ListFragment.newInstance(position);
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
            return 3;
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
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}