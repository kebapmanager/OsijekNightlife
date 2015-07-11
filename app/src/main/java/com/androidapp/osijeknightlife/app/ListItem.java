package com.androidapp.osijeknightlife.app;

import android.graphics.Bitmap;
import org.apache.commons.lang.builder.ToStringBuilder;

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
    Bitmap ev_image;

    public void setName(String name){this.name = name;}
    public void setEventName(String eventName){this.eventName = eventName;}
    public void setDate(String date){this.date = date;}
    public void setPeopleComing(String peopleComing){this.peopleComing = peopleComing;}
    public void setImageName(String imageName){this.imageName = imageName;}
    public void setImage(Bitmap image)
    {
        this.image = image;
    }
    public void setev_image(Bitmap ev_image)
    {
        this.ev_image = ev_image;
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
    public Bitmap getev_image(){return ev_image;}
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
