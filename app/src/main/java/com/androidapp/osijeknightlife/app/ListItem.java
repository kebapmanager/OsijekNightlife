package com.androidapp.osijeknightlife.app;

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
    public ListItem(){}

    public void setName(String name){this.name = name;}
    public void setEventName(String eventName){this.eventName = eventName;}
    public void setDate(String date){this.date = date;}
    public void setPeopleComing(String peopleComing){this.peopleComing = peopleComing;}
    public void setImageName(String imageName){this.imageName = imageName;}

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
}
