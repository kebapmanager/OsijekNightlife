package com.androidapp.osijeknightlife.app;

import java.util.*;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
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
import android.view.*;
import android.widget.*;
import com.androidapp.osijeknightlife.app.PageTransformers.*;
import com.androidapp.osijeknightlife.app.TabFragments.GridFragment;
import com.androidapp.osijeknightlife.app.TabFragments.ListFragment;
import com.androidapp.osijeknightlife.app.jsonDataP.Event;
import com.androidapp.osijeknightlife.app.jsonDataP.GetData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.MapFragment;
import retrofit.RetrofitError;

public class MainActivity extends ActionBarActivity
        implements ActionBar.TabListener,GetData.Listener,ListFragment.Listener,GridFragment.Listener,View.OnClickListener,OnMapReadyCallback {
///edit
    TextView report;
    SupportMapFragment mapFragment;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    GetData DW = new GetData(this);
    int TryCounter = 0,dan;
    Fragment event,grid;
    Calendar c = Calendar.getInstance();
    String Datum;
    View loading,event_ispis,club_ispis;
    LayoutInflater li;
    Map<Integer,String> Klubovi = new HashMap<Integer,String>();
    String[] KlubList = {"Old Bridge Pub","Tufna","Matrix","Caddilac","Bastion"};
    ImageButton ClubButton;
    List<Event> ListaEventa = new ArrayList<>();
    List<Bitmap> SlikeEventa = new ArrayList<>(20);
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
            if(ListaEventa.size() < 20) {
                if (DW.data.getEvents().size() + ListaEventa.size() < 20)
                    for (int i = 0; i < DW.data.getEvents().size(); i++) {
                        ListaEventa.add(DW.data.getEvents().get(i));
                        SlikeEventa.add(DW.Slike[i][0]);
                        //setPager();
                    }
                else
                    for (int i = 0; i < 20-ListaEventa.size(); i++) {
                        ListaEventa.add(DW.data.getEvents().get(i));
                        SlikeEventa.add(DW.Slike[i][0]);
                        //setPager();
                    }
                if(ListaEventa.size() < 20)
                {
                    getNextDay();
                }
            }
            else
            {
                setPager();
            }

        }
    }
    public void setPager()
    {
        DW.info = true;
        event = ListFragment.newInstance(0, ListaEventa, SlikeEventa);
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setContentView(mViewPager);
        //loading.setVisibility(View.INVISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        DW.CheckDate = 0;

    }
    public void getNextDay()
    {
        DW.registerListener(this);
        c.roll(Calendar.DAY_OF_MONTH,true);
        if(Integer.toString(c.get(Calendar.DAY_OF_MONTH)).equals("1"))
            c.roll(Calendar.MONTH,true);
        Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        if( c.get(Calendar.DAY_OF_MONTH)-c2.get(Calendar.DAY_OF_MONTH) < 5)
            DW.Start(Datum,c.get(Calendar.DAY_OF_MONTH));
        else {
            DW.CheckDate = 0;
            setPager();
        }
    }
    public void errorReport(RetrofitError error)
    {

        switch(error.getKind())
        {
            case NETWORK:
                report.setText("Network error\n check internet connection \n after you've reconnected restart the app");
                getNextDay();
                //dataRecieved(false);
                break;
            case CONVERSION:
                report.setText("Coversion error\nPlease update your app\nIf updated contact support");
                break;
            case HTTP:
                report.setText("Error with HTTP interaction:\n"+DW.Status);
                if(error.toString().equals("retrofit.RetrofitError: 404 Not Found"))
                    setPager();
                dataRecieved(false);
                break;
            case UNEXPECTED:
                report.setText("Unexpected error:" + DW.Status);
                //getNextDay();
                break;
        }
        //setPager();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+(c.get(Calendar.DAY_OF_MONTH));
        DW.registerListener(this);
        DW.Start(Datum,c.get(Calendar.DAY_OF_MONTH));

        LayoutInflater li=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loading = li.inflate(R.layout.loading_main,null);
        event_ispis = li.inflate(R.layout.event_layout,null);
        club_ispis = li.inflate(R.layout.club_layout,null);


        ClubButton = (ImageButton) findViewById(R.id.club_img_button);

        //report.setText(" ");

        //ClubButton.setOnClickListener(this);

        //mapFragment = GMapsFragment.newInstance(2);
        //mapFragment.getMapAsync(this);

        //keytool -list -v -keystore ToniP\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android

        event = ListFragment.newInstance(0, ListaEventa,SlikeEventa);
        ListFragment.registerListener(this);
        grid = GridFragment.newInstance(1);
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
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_background03));
        actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.dark_gray_backgorund));
        actionBar.setHideOnContentScrollEnabled(true);
        for(int i = 0;i<KlubList.length;i++)
            Klubovi.put(i,KlubList[i]);


        setContentView(loading);
        report = (TextView) findViewById(R.id.errorReport);
        report.setText(" ");
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        report.setText(" ");
        Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+(c.get(Calendar.DAY_OF_MONTH));
        DW.registerListener(this);
        DW.Start(Datum,c.get(Calendar.DAY_OF_MONTH));
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

        if(ListaEventa.size() != 0)
        {
            final Event ev = ListaEventa.get(evNum);
            Naslov.setText(ev.getTitle());
            Klub.setText(ev.getClub());
            Glazba.setText(ev.getMusic());
            Text.setText(ev.getText());
            img.setImageBitmap(SlikeEventa.get(num));
            ClubButton.setImageResource(getClubResId(ev.getClub()));
        }
        else
        {
            Naslov.setText("Naslov");
            Klub.setText("Klub");
            Glazba.setText("Glazba");
            Text.setText("Tekest\ndogadjaja\n i tak to\n\nda");
            img.setImageResource(R.drawable.main_background01);
            ClubButton.setImageResource(getClubResId("Tufna"));
        }
    }
    public int getClubResId(String id)
    {
        switch(id)
        {
            case"Old Bridge Pub":
                return R.mipmap.obp;
            case"Bastion":
                return R.mipmap.bastion;
            case"Tufna":
                return R.mipmap.tufna;
            case"Matrix":
                return R.mipmap.matrix;
            case"Cadillac":
                return R.mipmap.cadillac;
        }
        return R.mipmap.ic_launcher;
    }
    public void setKlubIspis(String id)
    {
        setContentView(club_ispis);
        TextView Naslov = (TextView)findViewById(R.id.club);
        TextView Text = (TextView)findViewById(R.id.text);
        ImageView img = (ImageView)findViewById(R.id.logo);
        img.setImageResource(getClubResId(id));




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
                    fragment = ListFragment.newInstance(0,ListaEventa,SlikeEventa);
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