package com.androidapp.osijeknightlife.app;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.DataSetObserver;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.androidapp.osijeknightlife.app.TabFragments.GridFragment;
import com.androidapp.osijeknightlife.app.TabFragments.ListFragment;
import com.androidapp.osijeknightlife.app.jsonDataP.GetData;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,GetData.Listener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    GetData DW = new GetData(this);
    int TryCounter = 0;
    Fragment event,grid;
    Calendar c = Calendar.getInstance();
    String Datum;
    int addMonth = 1;

    String[] tjedan = {"Ponedjeljak","Utorak","Srijeda","Cetvrtak","Petak","Subota","Nedjelja"};

    public void dataRecieved(boolean state)
    {
        if(state)
        {
            DW.info = true;//da
            event = ListFragment.newInstance(0,DW.data.getEvents());
            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mSectionsPagerAdapter);
            DW.registerListener(this);

        }else {

            TryCounter++;
            DW.registerListener(this);
            Calendar c = Calendar.getInstance();
            String Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
            if(TryCounter < 30)
                DW.Start(Datum);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);

        DW.registerListener(this);
        DW.Start(Datum);

        event = ListFragment.newInstance(0, DW.data.getEvents());
        grid = GridFragment.newInstance(1);
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
        mSectionsPagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
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
    public void onCreateHelper()
    {

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
    public String[] getDates()
    {
        int j = 1;
        String[] list = new String[5];
        c = Calendar.getInstance();
        for(int i = 0;i<5;i++) {

            String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
            if(day.equals("1")&&i!=0)
                j+=1;
            int month = c.get(Calendar.MONTH)+j;

            c.roll(Calendar.DAY_OF_MONTH,true);
            list[i] = day+"."+month+".";
        }


        return list;
    }
    private ShareActionProvider share;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(menuItem);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,getDates());
        adapter.setDropDownViewResource(R.layout.spinner_dropdow_item);
        spinner.setAdapter(adapter); // set the adapter to provide layout of rows and content
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c = Calendar.getInstance();
                for(int i = 0;i<position;i++){
                    c.roll(Calendar.DAY_OF_MONTH, true);
                    if(Integer.toString(c.get(Calendar.DAY_OF_MONTH)).equals("1") && i!=0)
                        c.roll(Calendar.MONTH,true);
                }
                Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);

                DW.Start(Datum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            Fragment fragment;
            switch(position)
            {
                case 0:
                    fragment = event;
                    break;
                case 1:
                    fragment = grid;
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