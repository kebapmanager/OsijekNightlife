package com.androidapp.osijeknightlife.app.TabFragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidapp.osijeknightlife.app.R;
import com.androidapp.osijeknightlife.app.jsonDataP.Event;

import java.util.List;

/**
 * Created by Ivan on 20/06/2015.
 */
public class ClubFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static TextView club;
    private static TextView title;
    private static TextView text;
    private static TextView music;
    private static ImageView logo;

    public static ClubFragment newInstance(int sectionNumber)
    {
        ClubFragment fragment = new ClubFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ClubFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("onCreateView");
        View rootView = inflater.inflate(R.layout.club_layout, container, false);
        club = (TextView) rootView.findViewById(R.id.club);
        title = (TextView) rootView.findViewById(R.id.title);
        text = (TextView) rootView.findViewById(R.id.text);
        music = (TextView) rootView.findViewById(R.id.music);
        logo = (ImageView) rootView.findViewById(R.id.logo);
        return rootView;
    }
    public static void changeLayoutProperties(Event event, List<Bitmap> pictures)
    {
        club.setText(event.getClub());
        title.setText(event.getTitle());
        text.setText(event.getText());
        music.setText("Vrsta glazbe: "+event.getMusic());
        logo.setImageBitmap(pictures.get(1));
    }
}