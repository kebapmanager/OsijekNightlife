package com.androidapp.osijeknightlife.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidapp.osijeknightlife.app.ImageHelper;
import com.androidapp.osijeknightlife.app.ListItem;
import com.androidapp.osijeknightlife.app.R;

import java.util.ArrayList;

/**
 * Created by Ema on 12.12.2015..
 */
public class MovieItemAdapter extends BaseAdapter
{
    private static ArrayList<ListItem> eventList;

    private LayoutInflater mInflater;
    Activity mainActivity;
    public int section;
    public ImageHelper imghelper;

    public MovieItemAdapter(Context photosFragment, ArrayList<ListItem> eventList, int section){
        this.section = section;
        this.eventList = eventList;
        mInflater = LayoutInflater.from(photosFragment);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return eventList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return eventList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        imghelper = new ImageHelper();
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.movieitem_layout, null);
            holder = new ViewHolder();

            holder.eventName = (TextView) convertView.findViewById(R.id.naslov_movieitem);
            holder.day = (TextView) convertView.findViewById(R.id.dan_movieitem);
            holder.ev_image = (ImageView) convertView.findViewById(R.id.event_img_movieitem);
            holder.image = (ImageView) convertView.findViewById(R.id.club_img_movieitem);
            holder.klubName = (TextView) convertView.findViewById(R.id.klub_movieitem);


            //holder.name = (TextView) convertView.findViewById(R.id.name);
            //holder.peopleComing = (TextView) convertView.findViewById(R.id.peopleComing);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.eventName.setText(eventList.get(position).getEventName());
        holder.day.setText(eventList.get(position).getDay());
        holder.klubName.setText(eventList.get(position).getKlubName());
        if(eventList.get(position).getev_image() != null)holder.ev_image.setImageBitmap(imghelper.getRoundedCornerBitmap(eventList.get(position).getev_image(),50));
        if(eventList.get(position).getImage() != null)holder.image.setImageBitmap(imghelper.getRoundedCornerBitmap(eventList.get(position).getImage(),25));

        return convertView;
    }

    static class ViewHolder{
        TextView klubName;
        TextView eventName;
        TextView day;
        ImageView image;
        ImageView ev_image;
    }
}
