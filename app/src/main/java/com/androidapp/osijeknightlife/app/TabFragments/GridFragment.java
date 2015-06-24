package com.androidapp.osijeknightlife.app.TabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.androidapp.osijeknightlife.app.Adapters.GridItemAdapter;
import com.androidapp.osijeknightlife.app.GridItem;
import com.androidapp.osijeknightlife.app.R;

import java.util.ArrayList;

/**
 * Created by Ivan on 20/06/2015.
 */

public class GridFragment extends Fragment
{
    private GridView lv;

    public static GridFragment newInstance(int sectionNumber)
    {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public GridFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.grid_layout, container, false);

        ArrayList<GridItem> clubList = getClubList();
        GridView lv = (GridView)rootView.findViewById(R.id.grid_layout);
        lv.setAdapter(new GridItemAdapter(getActivity(), clubList));

        return rootView;
    }
    private ArrayList<GridItem> getClubList(){
        ArrayList<GridItem> clubList = new ArrayList<GridItem>();

        GridItem event = new GridItem();

        event.setName("Old Bridge Pub");
        event.setImageId(R.drawable.obp);
        clubList.add(event);

        event = new GridItem();
        event.setName("Tufna");
        event.setImageId(R.drawable.tufna);
        clubList.add(event);

        event = new GridItem();
        event.setName("Matrix");
        event.setImageId(R.drawable.matrix);
        clubList.add(event);

        event = new GridItem();
        event.setName("Cadillac");
        event.setImageId(R.drawable.cadillac);
        clubList.add(event);

        event = new GridItem();
        event.setName("Bastion");
        event.setImageId(R.drawable.bastion);
        clubList.add(event);

        return clubList;
    }

    public static void changeLayoutProperties()
    {
        //Tu cu loadati iz Jsona u svoje objekte , zamjeniti ovo gore ^ sa ovom funkcijom koja
        // se poziva tek kad je sve skinuto sa servera
    }
}