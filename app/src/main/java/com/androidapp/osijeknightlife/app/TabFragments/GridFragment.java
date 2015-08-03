package com.androidapp.osijeknightlife.app.TabFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 20/06/2015.
 */

public class GridFragment extends com.androidapp.osijeknightlife.app.GridFragment
{
    private GridView lv;
    private String[] KlubList = {"Old Bridge Pub","Tufna","Matrix","Caddilac","Bastion"};
    ArrayList<GridItem> clubList = new ArrayList<>();
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

        lv = (GridView)rootView.findViewById(android.R.id.list);
        updateClubList();

        return rootView;
    }
    public void setAdapter()
    {
        this.setGridAdapter(new GridItemAdapter(getActivity(), clubList));
    }
    private void updateClubList(){
        clubList = new ArrayList<>();

        ParseQuery query = new ParseQuery("Klub");
        try {
            List<ParseObject> parseObjects = query.fromLocalDatastore().find();

            for (int i = 0;i<parseObjects.size();i++)
            {
                GridItem klub = new GridItem();

                byte[] bitmapdatas = ((ParseFile) parseObjects.get(i).get("Slika")).getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdatas, 0, bitmapdatas.length);
                klub.setImageBitmap(bitmap);
                clubList.add(klub);
            }

        }catch (Exception e){
            System.out.println("EEE");
        }



        setAdapter();
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