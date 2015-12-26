package com.androidapp.osijeknightlife.app;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.*;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.androidapp.osijeknightlife.app.Adapters.GridItemAdapter;
import com.androidapp.osijeknightlife.app.Adapters.ListItemAdapter;
import com.androidapp.osijeknightlife.app.PageTransformers.*;
import com.androidapp.osijeknightlife.app.TabFragments.GridFragment;
import com.androidapp.osijeknightlife.app.TabFragments.ListFragment;

import com.androidapp.osijeknightlife.app.TabFragments.MovieFragment;
import com.parse.*;

public class MainActivity extends FragmentActivity
        implements ListFragment.Listener,GridFragment.Listener,MovieFragment.Listener,View.OnClickListener {
    ///edit
    ImageView banner;
    String ADURL;
    ArrayList<Integer> KlubEv_nums = new ArrayList<>();
    TextView report;
    //SupportMapFragment mapFragment;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    Fragment event, grid, movies;
    Calendar c = Calendar.getInstance();
    String Datum;
    View loading, event_ispis, club_ispis;
    ImageButton ClubButton, Settings;
    int pamtipavrati = 0;

    List<String> Boje = Arrays.asList("Red", "Purple", "Deep Purple", "Indigo", "Cyan", "Teal", "Lime", "Orange", "Deep Orange", "Brown", "Grey");
    int currentColorIndex = 0, limit = 20;

    List<ParseObject> Movies = new ArrayList<>();
    List<ParseObject> Events = new ArrayList<>();
    List<ParseObject> Klubs = new ArrayList<>();

    public void GridClicked(int id) {
        pamtipavrati=2;
        ParseQuery query = new ParseQuery("Klub");
        try {
            List<ParseObject> parseObjects = query.fromLocalDatastore().find();
            setKlubIspis((String) parseObjects.get(id).get("Ime"));
        } catch (Exception e) {
        }

    }

    public void ClickedM(int position,long id){
        pamtipavrati = 1;
        setEventIspis((int)id,true);
    }

    public void Clicked(int position, long id) {
        pamtipavrati=0;
        setEventIspis((int) id,false);
    }

    public void setPager() {
        if (!(getWindow().getDecorView().getRootView() == mViewPager.getRootView())) {
            //event = ListFragment.newInstance(0,Events);
            //grid = GridFragment.newInstance(1);
            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mSectionsPagerAdapter);
            setContentView(mViewPager.getRootView());
            mViewPager.setCurrentItem(pamtipavrati);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Datum = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + (c.get(Calendar.DAY_OF_MONTH));


        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loading = li.inflate(R.layout.loading_main, null);
        event_ispis = li.inflate(R.layout.event_layout, null);
        club_ispis = li.inflate(R.layout.club_layout, null);


        banner = (ImageView) findViewById(R.id.banner);

        ClubButton = (ImageButton) findViewById(R.id.club_img_button);
        Settings = (ImageButton) findViewById(R.id.settings_button);
        Settings.setOnClickListener(this);
        //ClubButton.setOnClickListener(this);

        ListFragment.registerListener(this);
        MovieFragment.registerListener(this);
        GridFragment.registerListener(this);

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


        setContentView(loading);

        ImageView img = (ImageView) findViewById(R.id.loading_img);
        img.setImageResource(R.drawable.most);
        report = (TextView) findViewById(R.id.errorReport);
        report.setText(" ");

        if (isNetworkAvailable())
            initializeParse();
        else report.setText("No Internet connection");

        getKlubs();

        //debugPlaceholder();


    }

    public void debugPlaceholder() {
        ParseObject obj = new ParseObject("Events");
        obj.put("Naslov", "Naslov dogadjaja");
        obj.put("Datum", new Date());
        //obj.put("Slika",BitmapFactory.decodeResource(getResources(), R.drawable.banner));
        Events.add(0, obj);

        ParseObject obj2 = new ParseObject("Events");
        obj.put("Naslov", "Naslov dogadjaja");
        obj.put("Datum", new Date());
        //obj.put("Slika",BitmapFactory.decodeResource(getResources(), R.drawable.zvezda));
        Events.add(1, obj);

        event = ListFragment.newInstance(0, Events);
        grid = GridFragment.newInstance(1);
        setPager();
    }

    public void initializeParse() {
        //Parse.enableLocalDatastore(this);
        //Parse.initialize(this, "yd8rPOEKL418mHf5Avu1cN13oT3Qdjz197r8BtnR", "mMVBhcFIIUJRMTME0ZBOhR6vTz0mRu7lF63dtH8o");
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "4h2FNshppCgJNcEH6eYDUUNG0tyecE6QTKcJiBhk", "Wh3uPBU7hxRGTSs636iJ4inht1lgGzLu6M6rSRWE");

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void getAd() {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ads");

        //RNG
        Random r = new Random();

        try {
            int count = query.count();
            if (count > 0) {
                int rng = r.nextInt(count);

                ParseObject po = query.whereEqualTo("Num", rng).getFirst();

                ADURL = (String) po.get("Link");

                byte[] bitmapdata = ((ParseFile) po.get("Slika")).getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

                banner = (ImageView) findViewById(R.id.banner);
                banner.setImageBitmap(bitmap);
                banner.setOnClickListener(this);
            } else findViewById(R.id.banner).setVisibility(View.GONE);

        } catch (Exception e) {
        }
    }

    public void getEvents() {
        Date date = new Date();
        date.getTime();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
                query.whereGreaterThanOrEqualTo("Zavrsava", date).setLimit(limit).findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        Events = list;
                        Date date = new Date();
                        date.getTime();
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Movie");
                        query.whereGreaterThanOrEqualTo("Zavrsava", date).setLimit(limit).findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                Movies = list;
                                event = ListFragment.newInstance(0,Events);
                                movies =  ListFragment.newInstance(1,Movies);
                                //movies =  new Fragment();
                                grid = GridFragment.newInstance(2);
                                setPager();
                                getAd();
                            }
                        });

            }
        });
    }


    public void getKlubs() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Klub");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Klubs = list;
                compareToLocal(Klubs);
                for(int i = 0;i<list.size();i++) list.get(i).pinInBackground();
                getEvents();
            }
        });
    }
    public void compareToLocal(List<ParseObject> po)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Klub");
        try{
            int count = query.fromLocalDatastore().count();
            if(count != po.size())
            {
                List<ParseObject> list = query.fromLocalDatastore().find();

                for(int i = 0;i<count;i++)
                {
                    for(int j = 0;j<po.size();j++)
                    {
                        if(list.get(i).equals(po.get(j)))break;
                        else if(j == po.size()-1){list.get(i).unpin();}
                    }
                }
            }
        }catch (Exception e){}
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //report.setText(" ");
        Datum = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+(c.get(Calendar.DAY_OF_MONTH));
        //DW.Start(Datum,c.get(Calendar.DAY_OF_MONTH));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.club_img_button:
                TextView club =(TextView)findViewById(R.id.event_club);
                setKlubIspis((String)club.getText());
                break;
            case R.id.settings_button:
                setSettingsIspis();
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.back_button_club:
                onBackPressed();
                break;
            case R.id.banner:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ADURL));
                startActivity(browserIntent);
                break;
        }
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
                    event.setKlubName((String)Events.get(i).getParseObject("Klub").get("Ime"));
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
    public void setEventIspis(int evNum,boolean koji)
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
        Button back = (Button)findViewById(R.id.back_button);
        back.setOnClickListener(this);

        {
            FrameLayout fl = (FrameLayout)findViewById(R.id.event_frame_light);
            //RelativeLayout rl = (RelativeLayout)findViewById(R.id.event_relative_light);
            //rl.setBackgroundColor(getResources().getColor(getColorByIndex(currentColorIndex).get(1)));
            fl.setBackgroundColor(getResources().getColor(getColorByIndex(currentColorIndex).get(1)));
        }
        List<ParseObject> buffer;
        if(!koji)buffer = Events;
        else buffer = Movies;

        if(buffer.size() != 0)
        {
            final ParseObject ev = buffer.get(evNum);
            Date d = (Date)ev.get("Datum");
            SimpleDateFormat df = new SimpleDateFormat("dd.M | E | kk:mm");

            Naslov.setText((String)ev.get("Naslov"));
            Klub.setText((String)ev.getParseObject("Klub").get("Ime"));
            Vrijeme.setText(df.format(d));
            Text.setText((String)ev.get("Tekst"));
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
        TextView dod = (TextView)findViewById(R.id.dod);
        TextView tip = (TextView)findViewById(R.id.tip_k);
        ImageView img = (ImageView)findViewById(R.id.logo);
        Button back = (Button)findViewById(R.id.back_button_club);
        {
            //RelativeLayout rl = (RelativeLayout) findViewById(R.id.club_relative_light);
            FrameLayout fl = (FrameLayout) findViewById(R.id.club_frame_light);
            //rl.setBackgroundColor(getResources().getColor(getColorByIndex(currentColorIndex).get(1)));
            fl.setBackgroundColor(getResources().getColor(getColorByIndex(currentColorIndex).get(1)));
        }
        back.setOnClickListener(this);
        ParseQuery query = new ParseQuery("Klub");
        try {
            query.fromLocalDatastore().whereEqualTo("Ime", id);
            List<ParseObject> parseObject = query.find();
            byte[] bitmapdata = ((ParseFile) parseObject.get(0).get("Slika")).getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            img.setImageBitmap(bitmap);
            tip.setText("-"+parseObject.get(0).get("Status").toString());
            Text.setText("Radno vrijeme :\n"+parseObject.get(0).get("RV").toString()+"\nAdresa : \n"+parseObject.get(0).get("Adresa").toString()+"\nDodatno:\n"+parseObject.get(0).get("Dodatno").toString());
            dod.setText("Pušači : "+ parseObject.get(0).get("Pusaci").toString());
        }catch (Exception e)
        {}

        //Prepare events
                ListView lv = (ListView) findViewById(R.id.listView_club_layout);
                lv.setAdapter(new ListItemAdapter(this,getClubEvents(id),0));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setEventIspis(KlubEv_nums.get((int)id),false);
            }
        });


        Naslov.setText(id);

    }
    public List<Integer> getColorByIndex(int index)
    {

        switch(index)
        {
            case 0:
                return Arrays.asList(R.color.red_background_normal,R.color.red_background_light,R.color.red_background_dark);
            case 1:
                return Arrays.asList(R.color.purple_background_normal,R.color.purple_background_light,R.color.purple_background_dark);
            case 2:
                return Arrays.asList(R.color.deeppurple_background_normal,R.color.deeppurple_background_light,R.color.deeppurple_background_dark);
            case 3:
                return Arrays.asList(R.color.indigo_background_normal,R.color.indigo_background_light,R.color.indigo_background_dark);
            case 4:
                return Arrays.asList(R.color.cyan_background_normal,R.color.cyan_background_light,R.color.cyan_background_dark);
            case 5:
                return Arrays.asList(R.color.teal_background_normal,R.color.teal_background_light,R.color.teal_background_dark);
            case 6:
                return Arrays.asList(R.color.lime_background_normal,R.color.lime_background_light,R.color.lime_background_dark);
            case 7:
                return Arrays.asList(R.color.orange_background_normal,R.color.orange_background_light,R.color.orange_background_dark);
            case 8:
                return Arrays.asList(R.color.deeporange_background_normal,R.color.deeporange_background_light,R.color.deeporange_background_dark);
            case 9:
                return Arrays.asList(R.color.brown_background_normal,R.color.brown_background_light,R.color.brown_background_dark);
            case 10:
                return Arrays.asList(R.color.grey_background_normal,R.color.grey_background_light,R.color.grey_background_dark);
            default:
                return Arrays.asList(R.color.red_background_normal,R.color.red_background_light,R.color.red_background_dark);
        }
    }
    public ArrayList<GridItem> getGridColors()
    {
        ArrayList<GridItem> Colors = new ArrayList<>();

        for(int i = 0;i<Boje.size();i++)
        {
            GridItem griditem = new GridItem();
            Bitmap bmp = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(getResources().getColor(getColorByIndex(i).get(0)));
            griditem.setImageBitmap(bmp);
            Colors.add(griditem);
        }

        return Colors;
    }
    public void setSettingsIspis()
    {
        setContentView(R.layout.settings_layout);

        GridView gv = (GridView) findViewById(R.id.settings_grid);
        gv.setAdapter(new GridItemAdapter(this,getGridColors()));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentColorIndex = (int)id;
                findViewById(R.id.settings_grid).setBackgroundColor(getResources().getColor(getColorByIndex(currentColorIndex).get(0)));
            }
        });


    }
    private ShareActionProvider share;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        if(!(getWindow().getDecorView().getRootView() == mViewPager.getRootView())) {

            setPager();
            mViewPager.getRootView().findViewById(R.id.pager_title_strip).setBackgroundColor(getResources().getColor(getColorByIndex(currentColorIndex).get(0)));
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
        return super.onOptionsItemSelected(item);
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
                    fragment = ListFragment.newInstance(0,Events);
                    break;
                case 1:
                    fragment = MovieFragment.newInstance(1,Movies);
                    break;
                case 2:
                    fragment = grid;
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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}