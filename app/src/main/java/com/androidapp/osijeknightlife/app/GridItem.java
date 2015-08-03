package com.androidapp.osijeknightlife.app;

import android.graphics.Bitmap;

/**
 * Created by Ivan on 20/06/2015.
 */
public class GridItem
{
    String name;
    int imageId;
    Bitmap bmp;
    public GridItem(){}

    public void setName(String name){this.name = name;}
    public void setImageId(int id)
    {
        this.imageId = id;
    }
    public void setImageBitmap(Bitmap bitmap)
    {
        this.bmp = bitmap;
    }

    public String getName()
    {
        return name;
    }
    public int getImageId()
    {
        return imageId;
    }
    public Bitmap getImageBitmap()
    {
        return bmp;
    }
}
