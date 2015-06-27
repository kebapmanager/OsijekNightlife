package com.androidapp.osijeknightlife.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.R.drawable;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidapp.osijeknightlife.app.GridItem;
import com.androidapp.osijeknightlife.app.R;

import java.util.ArrayList;

/**
 * Created by Ivan on 20/06/2015.
 */
public class GridItemAdapter extends BaseAdapter
{
    private static ArrayList<GridItem> clubList;

    private LayoutInflater mInflater;
    Activity mainActivity;

    public GridItemAdapter(Context photosFragment, ArrayList<GridItem> clubList){
        this.clubList = clubList;
        mInflater = LayoutInflater.from(photosFragment);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return clubList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return clubList.get(arg0);
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
            convertView = mInflater.inflate(R.layout.griditem_layout, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setImageResource(clubList.get(position).getImageId());

        return convertView;
    }

    static class ViewHolder{
        TextView name;
        ImageView image;
    }
}
