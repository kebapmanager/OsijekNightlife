package com.androidapp.osijeknightlife.app.TabFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.androidapp.osijeknightlife.app.Adapters.ListItemAdapter;
import com.androidapp.osijeknightlife.app.Adapters.MovieItemAdapter;
import com.androidapp.osijeknightlife.app.ListItem;
import com.androidapp.osijeknightlife.app.R;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ema on 12.12.2015..
 */
public class MovieFragment extends android.support.v4.app.ListFragment
{
    public ListView lv;
    public static List<ParseObject> events;

    Bitmap bmp;

    public static MovieFragment newInstance(int sectionNumber,List<ParseObject> data)
    {
        if(data.size() != 0)
            events = data;
        else events = null;
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.movie_layout, container, false);
        ArrayList<ListItem> eventList = getEventList();
        lv = (ListView) rootView.findViewById(R.id.movie_layout);
        this.setListAdapter(new MovieItemAdapter(getActivity(), eventList,1));


        return rootView;
    }
    private ArrayList<ListItem> getEventList(){
        ArrayList<ListItem> eventList = new ArrayList<ListItem>();

        ListItem event = new ListItem();

        if(events == null ) {
        }
        else
        {
            for(int i = 0;i<events.size();i++)
            {

                event = new ListItem();
                SimpleDateFormat df = new SimpleDateFormat("EE - kk:mm");
                Date krece = (Date)events.get(i).get("Datum");


                event.setEventName((String)events.get(i).get("Naslov"));
                event.setDay(df.format(krece));
                event.setPeopleComing("Nepoznato");
                event.setKlubName((String)events.get(i).getParseObject("Klub").get("Ime"));
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
                    bmp=null;
                }catch(Exception e){}

                eventList.add(event);
            }
        }

        return eventList;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.ClickedM(position,id);
    }

    public interface Listener{
        public void ClickedM(int position,long id);
    }
    private static Listener mListener = null;
    public static void registerListener (Listener listener) {
        mListener = listener;
    }
}
