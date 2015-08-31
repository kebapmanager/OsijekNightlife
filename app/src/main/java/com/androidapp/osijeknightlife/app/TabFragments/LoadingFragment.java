package com.androidapp.osijeknightlife.app.TabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.androidapp.osijeknightlife.app.R;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Toni P on 8/31/2015.
 */
public class LoadingFragment extends Fragment {
    public static LoadingFragment newInstance(int sectionNumber)
    {
        LoadingFragment fragment = new LoadingFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.loading_main, container, false);
        ImageView img = (ImageView)rootView.findViewById(R.id.loading_img);
        img.setImageResource(R.drawable.most);
        return rootView;
    }
}
