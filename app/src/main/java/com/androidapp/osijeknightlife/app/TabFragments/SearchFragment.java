package com.androidapp.osijeknightlife.app.TabFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.androidapp.osijeknightlife.app.R;

/**
 * Created by Ivan on 20/06/2015.
 */
public class SearchFragment extends android.support.v4.app.Fragment
{
    private static EditText editText;

    public static SearchFragment newInstance(int sectionNumber)
    {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("onCreateView");
        View rootView = inflater.inflate(R.layout.search_layout, container, false);
        editText =  (EditText) rootView.findViewById(R.id.edit_message);
        return rootView;
    }
    public static void changeLayoutProperties()
    {
        editText.setText("Search for event");
    }
}