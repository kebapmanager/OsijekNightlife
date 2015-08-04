package com.androidapp.osijeknightlife.app;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.view.*;
import android.widget.*;
import com.androidapp.osijeknightlife.app.Adapters.ListItemAdapter;
import com.androidapp.osijeknightlife.app.PageTransformers.*;
import com.androidapp.osijeknightlife.app.TabFragments.GridFragment;
import com.androidapp.osijeknightlife.app.TabFragments.ListFragment;
import com.androidapp.osijeknightlife.app.Tasks.OnListLoaded_Task;
import com.androidapp.osijeknightlife.app.jsonDataP.Event;
import com.androidapp.osijeknightlife.app.jsonDataP.GetData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.MapFragment;
import com.parse.*;
import retrofit.RetrofitError;

public class MainActivity extends ActionBarActivity
        implements ActionBar.TabListener,ListFragment.Listener,GridFragment.Listener,View.OnClickListener,OnMapReadyCallback {
    ///edit
    ArrayList<Integer> KlubEv_nums = new ArrayList<>();
    TextView report;
    //SupportMapFragment mapFragment;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    GetData DW = new GetData(this);
    Fragment event,grid;
    Calendar c = Calendar.getInstance();
    String Datum;
    View loading,event_ispis,club_ispis;
    LayoutInflater li;
    Map<Integer,String> Klubovi = new HashMap<Integer,String>();
    ImageButton ClubButton,Settings;

    List<ParseObject> Events = new ArrayList<>();
    List<ParseObject> Klubs = new ArrayList<>();
    public void GridClicked(int id)
    {
        getSupportActionBar().hide();
        ParseQuery query = new ParseQuery("Klub");
        try{
            List<ParseObject> parseObjects = query.fromLocalDatastore().find();
            setKlubIspis((String)parseObjects.get(id).get("Ime"));
        } catch (Exception e) {
        }

    }
    public void Clicked(int position,long id)
    {
        getSupportActionBar().hide();
        setEventIspis((int)id);
    }
    public void setPager()
    {
        if(!(getWindow().getDecorView().getRootView() == mViewPager.getRootView())) {
            DW.info = true;
            event = ListFragment.newInstance(0, Events);
            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mSectionsPagerAdapter);
            setContentView(mViewPager.getRootView());
            //loading.setVisibility(View.INVISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            DW.CheckDate = 0;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+(c.get(Calendar.DAY_OF_MONTH));


        LayoutInflater li=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loading = li.inflate(R.layout.loading_main,null);
        event_ispis = li.inflate(R.layout.event_layout,null);
        club_ispis = li.inflate(R.layout.club_layout,null);


        ClubButton = (ImageButton) findViewById(R.id.club_img_button);
        Settings = (ImageButton) findViewById(R.id.settings_button);
        Settings.setOnClickListener(this);
        //ClubButton.setOnClickListener(this);

        //mapFragment = GMapsFragment.newInstance(2);
        //mapFragment.getMapAsync(this);

        //keytool -list -v -keystore ToniP\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android

        //event = ListFragment.newInstance(0, null);
        ListFragment.registerListener(this);
        //grid = GridFragment.newInstance(1);
        GridFragment.registerListener(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
                .setText("Eventi")
                .setIcon(R.drawable.eventi_icon)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab()
                .setText("Klubovi")
                .setIcon(R.drawable.klubovi_icon)
                .setTabListener(this));
        //actionBar.addTab(actionBar.newTab()
                //.setCustomView(R.layout.tab_layout)
                //.setText("Mapa")
                //.setTabListener(this));
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle(" ");
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_background01));
        actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.blue_background));
        actionBar.setHideOnContentScrollEnabled(true);
        actionBar.hide();


        setContentView(loading);

        ImageView img = (ImageView) findViewById(R.id.loading_img);
        img.setImageResource(R.drawable.most);
        report = (TextView) findViewById(R.id.errorReport);
        report.setText(" ");

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "yd8rPOEKL418mHf5Avu1cN13oT3Qdjz197r8BtnR", "mMVBhcFIIUJRMTME0ZBOhR6vTz0mRu7lF63dtH8o");
        getKlubs();
        getEvents();




    }
    public void getEvents()
    {
        Date date = new Date();
        date.getTime();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.whereGreaterThanOrEqualTo("Datum", date).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                Events = list;
                setPager();
            }
        });
    }
    public void getKlubs()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Klub");

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> Klublist, ParseException e) {
                    Klubs = Klublist;
                    for(int i = 0;i<Klublist.size();i++) Klublist.get(i).pinInBackground();
                }
            });
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //report.setText(" ");
        Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+(c.get(Calendar.DAY_OF_MONTH));
        //DW.Start(Datum,c.get(Calendar.DAY_OF_MONTH));
    }
    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(sydney), 10);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.club_img_button:
                TextView club =(TextView)findViewById(R.id.event_club);
                setKlubIspis((String)club.getText());
                break;
            case R.id.settings_button:
                setContentView(R.layout.settings_layout);
                break;
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
    public ArrayList<ListItem> getClubEvents(String Club)
    {
        ArrayList<ListItem> eventList = new ArrayList<>();
        KlubEv_nums = new ArrayList<>();



        ListItem event = new ListItem();


            for(int i = 0;i<Events.size();i++)
            {
                if(Events.get(i).getParseObject("Klub").get("Ime").equals(Club))
                {
                    SimpleDateFormat df = new SimpleDateFormat("EE - kk:mm");
                    Date d = (Date) Events.get(i).get("Datum");

                    KlubEv_nums.add(i);
                    event = new ListItem();
                    event.setName(Club);
                    event.setEventName((String) Events.get(i).get("Naslov"));
                    event.setDay(df.format(d));
                    try {
                        byte[] bitmapdata = ((ParseFile) Events.get(i).get("Slika")).getData();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                        event.setev_image(bitmap);

                        byte[] bitmapdatas = ((ParseFile) Events.get(i).getParseObject("Klub").get("Slika")).getData();
                        Bitmap bmp = BitmapFactory.decodeByteArray(bitmapdatas, 0, bitmapdatas.length);
                        event.setImage(bmp);

                    }catch(Exception e){}
                    eventList.add(event);
                }
            }



        return eventList;
    }
    public void setEventIspis(int evNum)
    {

        int num = evNum;
        setContentView(event_ispis);
        ClubButton = (ImageButton)findViewById(R.id.club_img_button);
        ClubButton.setOnClickListener(this);
        TextView Naslov = (TextView)findViewById(R.id.event_title);
        TextView Klub = (TextView)findViewById(R.id.event_club);
        TextView Vrijeme = (TextView)findViewById(R.id.event_music);
        TextView Text = (TextView)findViewById(R.id.event_text);
        ImageView img = (ImageView)findViewById(R.id.img_event_ispis);



        if(Events.size() != 0)
        {
            final ParseObject ev = Events.get(evNum);
            Date d = (Date)ev.get("Datum");
            SimpleDateFormat df = new SimpleDateFormat("dd.M | E | kk:mm");

            Naslov.setText((String)ev.get("Naslov"));
            Klub.setText((String)ev.getParseObject("Klub").get("Ime"));
            Vrijeme.setText(df.format(d));
            Text.setText((String)ev.get((String)ev.get("Tekst")));
            try {
                byte[] bitmapdata = ((ParseFile) ev.get("Slika")).getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                img.setImageBitmap(bitmap);
            }catch(Exception e){}
            ///////////////////////////////////////RADIIIIIIIIIIIIIIIIIIIIIIIIIII
            try
            {
                byte[] bitmapdata = ((ParseFile) ev.getParseObject("Klub").get("Slika")).getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                ClubButton.setImageBitmap(bitmap);
            }catch(Exception m)
            {}

        }
    }
    public void setKlubIspis(String id)
    {
        setContentView(club_ispis);
        TextView Naslov = (TextView)findViewById(R.id.club);
        TextView Text = (TextView)findViewById(R.id.text);
        ImageView img = (ImageView)findViewById(R.id.logo);
        ParseQuery query = new ParseQuery("Klub");
        try {
            query.fromLocalDatastore().whereEqualTo("Ime", id);
            List<ParseObject> parseObject = query.find();
            byte[] bitmapdata = ((ParseFile) parseObject.get(0).get("Slika")).getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            img.setImageBitmap(bitmap);
        }catch (Exception e)
        {}

        //Prepare events
                ListView lv = (ListView) findViewById(R.id.listView_club_layout);
                lv.setAdapter(new ListItemAdapter(this,getClubEvents(id)));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setEventIspis(KlubEv_nums.get((int)id));
            }
        });


        Naslov.setText(id);

        Text.setText("Tekst kluba\nkoko os\ntako je");
    }
    private ShareActionProvider share;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if(!(getWindow().getDecorView().getRootView() == mViewPager.getRootView())) {
            //getSupportActionBar().show();
            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setCurrentItem(getSupportActionBar().getSelectedTab().getPosition());
            ListFragment.registerListener(this);
            setContentView(mViewPager.getRootView());
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
            setPager();
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
                    fragment = ListFragment.newInstance(0,Events);
                    break;
                case 1:
                    fragment = GridFragment.newInstance(1);
                    break;
                case 2:
                    fragment = new Fragment();//mapFragment;
                    break;
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