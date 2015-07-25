package com.androidapp.osijeknightlife.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Toni P on 7/22/2015.
 */
public class GMapsFragment extends SupportMapFragment {
    private SupportMapFragment mapFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gmaps_main, container, false);

        return rootView;
    }
    public static SupportMapFragment newInstance(int sectionNumber)
    {
        GMapsFragment fragment = new GMapsFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);

        fragment.setArguments(args);
        return fragment;
    }
    public Fragment getFragment(){return mapFragment;}
}
