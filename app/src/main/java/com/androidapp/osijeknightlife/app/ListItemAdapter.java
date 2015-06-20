package com.androidapp.osijeknightlife.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ivan on 20/06/2015.
 */
public class ListItemAdapter extends BaseAdapter
{
    private static ArrayList<ListItem> listContact;

    private LayoutInflater mInflater;

    public ListItemAdapter(Context photosFragment, ArrayList<ListItem> results){
        listContact = results;
        mInflater = LayoutInflater.from(photosFragment);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listContact.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listContact.get(arg0);
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
            holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
            holder.txtphone = (TextView) convertView.findViewById(R.id.txtphone);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.txtname.setText(listContact.get(position).GetName());
//        holder.txtphone.setText(listContact.get(position).GetPhone());
        holder.txtname.setText("txtname1");
        holder.txtphone.setText("txtphone2");

        return convertView;
    }

    static class ViewHolder{
        TextView txtname, txtphone;
    }
}
