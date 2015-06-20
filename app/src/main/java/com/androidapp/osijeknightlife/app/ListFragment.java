package com.androidapp.osijeknightlife.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ivan on 20/06/2015.
 */
public class ListFragment extends Fragment
{

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
        System.out.println("onCreateView list layout");
        View rootView = inflater.inflate(R.layout.list_layout, container, false);

        return rootView;
    }
    public static void changeLayoutProperties()
    {

    }
}