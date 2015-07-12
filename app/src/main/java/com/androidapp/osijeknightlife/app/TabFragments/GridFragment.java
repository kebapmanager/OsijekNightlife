package com.androidapp.osijeknightlife.app.TabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import com.androidapp.osijeknightlife.app.Adapters.GridItemAdapter;
import com.androidapp.osijeknightlife.app.GridItem;
import com.androidapp.osijeknightlife.app.R;

import java.util.ArrayList;

/**
 * Created by Ivan on 20/06/2015.
 */

public class GridFragment extends com.androidapp.osijeknightlife.app.GridFragment
{
    private GridView lv;
    private String[] KlubList = {"Old Bridge Pub","Tufna","Matrix","Caddilac","Bastion"};
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
        lv = (GridView)rootView.findViewById(android.R.id.list);

        this.setGridAdapter(new GridItemAdapter(getActivity(), clubList));
        //lv.setAdapter(new GridItemAdapter(getActivity(), clubList));

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
    @Override
    public void onGridItemClick(GridView g, View v, int position, long id) {
        mListener.GridClicked((int)id);
    }
    public interface Listener{
        public void GridClicked(int id);
    }
    private static Listener mListener = null;
    public static void registerListener (Listener listener) {
        mListener = listener;
    }
}