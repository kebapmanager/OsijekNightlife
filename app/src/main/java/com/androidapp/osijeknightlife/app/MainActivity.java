package com.androidapp.osijeknightlife.app;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.gesture.GestureUtils;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.androidapp.osijeknightlife.app.Adapters.ListItemAdapter;
import com.androidapp.osijeknightlife.app.TabFragments.GridFragment;
import com.androidapp.osijeknightlife.app.TabFragments.ListFragment;
import com.androidapp.osijeknightlife.app.jsonDataP.Event;
import com.androidapp.osijeknightlife.app.jsonDataP.GetData;
import retrofit.RetrofitError;

public class MainActivity extends ActionBarActivity
        implements ActionBar.TabListener,GetData.Listener,ListFragment.Listener,GridFragment.Listener,View.OnClickListener {
///edit
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    GetData DW = new GetData(this);
    int TryCounter = 0;
    Fragment event,grid;
    Calendar c = Calendar.getInstance();
    String Datum;
    int addMonth = 1,pamtilo = 0;
    FooThread fooThread;
    View loading,event_ispis,club_ispis;
    LayoutInflater li;
    Map<Integer,String> Klubovi = new HashMap<Integer,String>();
    String[] KlubList = {"Old Bridge Pub","Tufna","Matrix","Caddilac","Bastion"};
    Button error_button;
    ImageButton ClubButton;

    String[] tjedan = {"Ponedjeljak","Utorak","Srijeda","Cetvrtak","Petak","Subota","Nedjelja"};

    public void GridClicked(int id)
    {
        getSupportActionBar().hide();
        setKlubIspis(Klubovi.get(id));
    }
    public void Clicked(int position,long id)
    {
        getSupportActionBar().hide();
        setEventIspis((int)id);
    }
    public void dataRecieved(boolean state)
    {
        if(state)
        {
            DW.info = true;
            event = ListFragment.newInstance(0,DW.data.getEvents(),DW.Slike);
            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mSectionsPagerAdapter);
            DW.registerListener(this);
            setContentView(mViewPager);
            //loading.setVisibility(View.INVISIBLE);
            mViewPager.setVisibility(View.VISIBLE);

        }
        else {
            TryCounter++;
            DW.registerListener(this);
            Calendar c = Calendar.getInstance();
            String Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
            DW.Start(Datum);
        }
    }
    public void errorReport(RetrofitError.Kind kind)
    {
        DW.registerListener(this);
        TextView report = (TextView)findViewById(R.id.errorReport);
        error_button = (Button)findViewById(R.id.button_error);
        error_button.setOnClickListener(this);
        switch(kind)
        {
            case NETWORK:
                report.setText("Network error\n check internet connection \n app will try again when it detects connection");
                dataRecieved(false);
                break;
            case CONVERSION:
                report.setText("Coversion error\nPlease update your app\nIf updated contact support");
                break;
            case HTTP:
                report.setText("Error with HTTP interaction:\n"+DW.Status);
                dataRecieved(false);
                break;
            case UNEXPECTED:
                report.setText("Unexpected error:" + DW.Status);
                break;
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

        LayoutInflater li=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loading = li.inflate(R.layout.loading_main,null);
        event_ispis = li.inflate(R.layout.event_layout,null);
        club_ispis = li.inflate(R.layout.club_layout,null);

        error_button = (Button) findViewById(R.id.button_error);
        //error_button.setOnClickListener(this);
        ClubButton = (ImageButton) findViewById(R.id.club_img_button);
        //ClubButton.setOnClickListener(this);


        event = ListFragment.newInstance(0, DW.data.getEvents(),DW.Slike);
        ListFragment.registerListener(this);
        grid = GridFragment.newInstance(1);
        GridFragment.registerListener(this);
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
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
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
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mushroom_forest));
        actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.pink));
        for(int i = 0;i<KlubList.length;i++)
            Klubovi.put(i,KlubList[i]);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.button_error: {
                dataRecieved(false);
                break;
            }

            case R.id.club_img_button: {
                TextView club =(TextView)findViewById(R.id.event_club);
                setKlubIspis((String)club.getText());
                break;
            }
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
    public void setEventIspis(int evNum)
    {
        int num = evNum;
        setContentView(event_ispis);
        ClubButton = (ImageButton)findViewById(R.id.club_img_button);
        ClubButton.setOnClickListener(this);
        TextView Naslov = (TextView)findViewById(R.id.event_title);
        TextView Klub = (TextView)findViewById(R.id.event_club);
        TextView Glazba = (TextView)findViewById(R.id.event_music);
        TextView Text = (TextView)findViewById(R.id.event_text);
        ImageView img = (ImageView)findViewById(R.id.img_event_ispis);
        final Event ev = DW.data.getEvents().get(num);
        Naslov.setText(ev.getTitle());
        Klub.setText(ev.getClub());
        Glazba.setText(ev.getMusic());
        Text.setText(ev.getText());
        img.setImageBitmap(DW.Slike[num][0]);
        if(ev.getClub().equals("Tufna"))
            ClubButton.setImageResource(R.drawable.tufna);

    }
    public void setKlubIspis(String id)
    {
        setContentView(club_ispis);
        TextView Naslov = (TextView)findViewById(R.id.club);
        TextView Text = (TextView)findViewById(R.id.text);
        ImageView img = (ImageView)findViewById(R.id.logo);


        Naslov.setText(id);

        Text.setText("Tekst kluba\nkoko os\ntako je");
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

                setContentView(loading);
                //mViewPager.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);

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
    public void onBackPressed() {

        getSupportActionBar().show();
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(getSupportActionBar().getSelectedTab().getPosition());
        ListFragment.registerListener(this);
        setContentView(mViewPager);
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
                    fragment = ListFragment.newInstance(0,DW.data.getEvents(),DW.Slike);
                    break;
                case 1:
                    fragment = GridFragment.newInstance(1);
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
    private class FooThread extends Thread {
        Handler mHandler;

        FooThread(Handler h) {
            mHandler = h;
        }

        public void run() {
            //Do all my work here....you might need a loop for this

            Message msg = mHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("state", 1);
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }
}