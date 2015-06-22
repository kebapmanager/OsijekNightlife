package com.androidapp.osijeknightlife.app;

import android.graphics.Bitmap;

/**
 * Created by Ivan on 20/06/2015.
 */
public class ListItem
{
    String name;
    String eventName;
    String date;
    String peopleComing;
    String imageName;
    Bitmap image;
    public ListItem(){}

    public void setName(String name){this.name = name;}
    public void setEventName(String eventName){this.eventName = eventName;}
    public void setDate(String date){this.date = date;}
    public void setPeopleComing(String peopleComing){this.peopleComing = peopleComing;}
    public void setImageName(String imageName){this.imageName = imageName;}
    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public String getName()
    {
        return name;
    }
    public String getEventName()
    {
        return eventName;
    }
    public String getDate()
    {
        return date;
    }
    public String getPeopleComing()
    {
        return peopleComing;
    }
    public String getImageName()
    {
        return imageName;
    }
    public Bitmap getImage()
    {
        return image;
    }
}
