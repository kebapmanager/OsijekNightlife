package com.androidapp.osijeknightlife.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidapp.osijeknightlife.app.ListItem;
import com.androidapp.osijeknightlife.app.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ivan on 20/06/2015.
 */
public class ListItemAdapter extends BaseAdapter
{
    private static ArrayList<ListItem> eventList;

    private LayoutInflater mInflater;
    Activity mainActivity;

    public ListItemAdapter(Context photosFragment, ArrayList<ListItem> eventList){
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
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.listitem_layout, null);
            holder = new ViewHolder();

            holder.eventName = (TextView) convertView.findViewById(R.id.naslov_listitem);
            holder.date = (TextView) convertView.findViewById(R.id.datum_listitem);
            holder.day = (TextView) convertView.findViewById(R.id.dan_listitem);
            holder.ev_image = (ImageView) convertView.findViewById(R.id.event_img_listitem);
            holder.image = (ImageView) convertView.findViewById(R.id.club_img_listitem);


            //holder.name = (TextView) convertView.findViewById(R.id.name);
            //holder.peopleComing = (TextView) convertView.findViewById(R.id.peopleComing);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.name.setText(eventList.get(position).getName());
        holder.date.setText(eventList.get(position).getDate());
        holder.eventName.setText(eventList.get(position).getEventName());
        holder.date.setText(eventList.get(position).getDate());
        holder.day.setText(eventList.get(position).getDate());
        if(eventList.get(position).getev_image() != null)holder.ev_image.setImageBitmap(eventList.get(position).getev_image());

        switch (eventList.get(position).getName())
        {
            case "Bastion":
                holder.image.setImageResource(R.mipmap.bastion);
                break;
            case "Tufna":
                holder.image.setImageResource(R.mipmap.tufna);
                break;
            case "Old Bridge Pub":
                holder.image.setImageResource(R.mipmap.obp);
                break;
            case "Matrix":
                holder.image.setImageResource(R.mipmap.matrix);
                break;
        }

        //holder.peopleComing.setText(eventList.get(position).getPeopleComing());

        //holder.image.setImageBitmap(eventList.get(position).getImage());
        //holder.ev_image.setImageBitmap(eventList.get(position).getImage());


        return convertView;
    }

    static class ViewHolder{
        //TextView name;
        TextView eventName;
        TextView date;
        TextView day;
        //TextView peopleComing;
        ImageView image;
        ImageView ev_image;
    }
}
