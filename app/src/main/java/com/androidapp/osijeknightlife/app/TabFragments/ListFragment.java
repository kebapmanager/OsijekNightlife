package com.androidapp.osijeknightlife.app.TabFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.androidapp.osijeknightlife.app.ListItem;
import com.androidapp.osijeknightlife.app.Adapters.ListItemAdapter;
import com.androidapp.osijeknightlife.app.R;
import com.androidapp.osijeknightlife.app.jsonDataP.Event;
import com.parse.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 20/06/2015.
 */
///edit
public class ListFragment extends android.support.v4.app.ListFragment
{
    public ListView lv;///da
    public static List<ParseObject> events;
    Bitmap bmp;

    public static ListFragment newInstance(int sectionNumber,List<ParseObject> data)
    {
        if(data.size() != 0)
            events = data;
        else events = null;
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);

        fragment.setArguments(args);
        return fragment;
    }

    public ListFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView;
        //if(events != null) {
            rootView = inflater.inflate(R.layout.list_layout, container, false);

            ArrayList<ListItem> eventList = getEventList();
            lv = (ListView) rootView.findViewById(R.id.list_layout);
            this.setListAdapter(new ListItemAdapter(getActivity(), eventList));


            //lv.setAdapter(new ListItemAdapter(getActivity(), eventList));         }

        //else {
        //rootView = inflater.inflate(R.layout.loading_main, container, false);}

        return rootView;
    }
    private ArrayList<ListItem> getEventList(){
        ArrayList<ListItem> eventList = new ArrayList<ListItem>();

        ListItem event = new ListItem();

        if(events == null) {
            event.setName("Old Bridge Pub");
            event.setEventName("Cigani lete u nebo");
            event.setDate("12.6");
            event.setPeopleComing("102");
            eventList.add(event);

            event = new ListItem();
            event.setName("Tufna");
            event.setEventName("DJ Zidov");
            event.setDate("10.7");
            event.setPeopleComing("1024");
            eventList.add(event);

            event = new ListItem();
            event.setName("Matrix");
            event.setDate("30.6");
            event.setEventName("Party - cigan osvetnik");
            event.setPeopleComing("124");
            eventList.add(event);

            event = new ListItem();
            event.setName("Cadillac");
            event.setDate("13.6");
            event.setEventName("Zidov uzvraca udarac");

            event.setPeopleComing("234");
            eventList.add(event);

            event = new ListItem();
            event.setName("Bastion");
            event.setDate("12.7");
            event.setEventName("Njiva bend");
            event.setPeopleComing("134");
            eventList.add(event);
        }
        else
        {
            for(int i = 0;i<events.size();i++)
            {
                event = new ListItem();
                Date d = (Date)events.get(i).get("Datum");
                SimpleDateFormat df = new SimpleDateFormat("EE - kk:mm");



                event.setEventName((String)events.get(i).get("Naslov"));
                event.setDay(df.format(d));
                event.setPeopleComing("Nepoznato");
                try {
                    ////
                    byte[] bitmapdata = ((ParseFile) events.get(i).get("Slika")).getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    event.setev_image(bitmap);

                    ///
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Klub");
                    query.fromLocalDatastore();
                    query.whereEqualTo("objectId", events.get(i).get("Klub"));
                    List<ParseObject> list = query.find();
                    event.setName("POINTER");


                    byte[] bitmapdatas = ((ParseFile) events.get(i).getParseObject("Klub").get("Slika")).getData();
                    bmp = BitmapFactory.decodeByteArray(bitmapdatas, 0, bitmapdatas.length);

                    event.setImage(bmp);
                }catch(Exception e){}

                eventList.add(event);
            }
        }

        return eventList;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.Clicked(position,id);
    }

    public interface Listener{
        public void Clicked(int position,long id);
    }
    private static Listener mListener = null;
    public static void registerListener (Listener listener) {
        mListener = listener;
    }
}