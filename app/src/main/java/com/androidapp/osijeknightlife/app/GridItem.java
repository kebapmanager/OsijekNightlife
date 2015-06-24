package com.androidapp.osijeknightlife.app;

/**
 * Created by Ivan on 20/06/2015.
 */
public class GridItem
{
    String name;
    int imageId;
    public GridItem(){}

    public void setName(String name){this.name = name;}
    public void setImageId(int id)
    {
        this.imageId = id;
    }

    public String getName()
    {
        return name;
    }
    public int getImageId()
    {
        return imageId;
    }
}
