package com.androidapp.osijeknightlife.app.TabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.androidapp.osijeknightlife.app.ListItem;
import com.androidapp.osijeknightlife.app.ListItemAdapter;
import com.androidapp.osijeknightlife.app.R;
import com.androidapp.osijeknightlife.app.jsonDataP.Event;

import java.util.ArrayList;

/**
 * Created by Ivan on 20/06/2015.
 */

public class ListFragment extends Fragment
{
    private ListView lv;

    public static ListFragment newInstance(int sectionNumber)
    {
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
        View rootView = inflater.inflate(R.layout.list_layout, container, false);

        ArrayList<ListItem> eventList = GetEventList();
        ListView lv = (ListView)rootView.findViewById(R.id.list_layout);
        lv.setAdapter(new ListItemAdapter(getActivity(), eventList));

        return rootView;
    }
    private ArrayList<ListItem> GetEventList(){
        ArrayList<ListItem> eventList = new ArrayList<ListItem>();

        ListItem event = new ListItem();

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

        return eventList;
    }

    public static void changeLayoutProperties(Event event)
    {
        //Tu cu loadati iz Jsona u svoje objekte , zamjeniti ovo gore ^ sa ovom funkcijom koja
        // se poziva tek kad je sve skinuto sa servera
    }
}