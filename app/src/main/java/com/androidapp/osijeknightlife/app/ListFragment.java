package com.androidapp.osijeknightlife.app;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.androidapp.osijeknightlife.app.jsonDataP.Event;

import java.util.ArrayList;

/**
 * Created by Ivan on 20/06/2015.
 */

public class ListFragment extends Fragment
{
    private ListView lv;
    private static TextView text;

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
//        System.out.println("onCreateView list layout");
        View rootView = inflater.inflate(R.layout.list_layout, container, false);

        ArrayList<ListItem> listContact = GetlistContact();
//        ListView lv = (ListView)getActivity().findViewById(R.id.listitem_layout);
        ListView lv = (ListView)rootView.findViewById(R.id.list_layout);
        lv.setAdapter(new ListItemAdapter(getActivity(), listContact));
        return rootView;
    }
    private ArrayList<ListItem> GetlistContact(){
        ArrayList<ListItem> contactlist = new ArrayList<ListItem>();

        ListItem contact = new ListItem();

        contact.SetName("Topher");
        contact.SetPhone("01213113568");
        contactlist.add(contact);

        contact = new ListItem();
        contact.SetName("Jean");
        contact.SetPhone("01213869102");
        contactlist.add(contact);

        contact = new ListItem();
        contact.SetName("Andrew");
        contact.SetPhone("01213123985");
        contactlist.add(contact);

        return contactlist;
    }

    public static void changeLayoutProperties(Event event)
    {
        text.setText(event.getText());
    }
}