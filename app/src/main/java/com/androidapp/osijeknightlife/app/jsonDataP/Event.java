package com.androidapp.osijeknightlife.app.jsonDataP;

/**
 * Created by Toni P on 6/5/2015.
 */

import com.google.gson.annotations.Expose;


import java.util.ArrayList;
import java.util.List;

public class Event {

    @Expose
    private String Day;
    @Expose
    private String Date;
    @Expose
    private String Club;
    @Expose
    private String Title;
    @Expose
    private String Text;
    @Expose
    private String Music;
    @Expose
    private List<String> Glazba = new ArrayList<String>();
    @Expose
    private String Extra;
    @Expose
    private List<String> pics = new ArrayList<String>();

    /**
     *
     * @return
     * The Day
     */
    public String getDay() {
        return Day;
    }

    /**
     *
     * @param Dan
     * The Day
     */
    public void setDay(String Dan) {
        this.Day = Dan;
    }

    /**
     *
     * @return
     * The Date
     */
    public String getDate() {
        return Date;
    }

    /**
     *
     * @param Datum
     * The Date
     */
    public void setDate(String Datum) {
        this.Date = Datum;
    }

    /**
     *
     * @return
     * The Club
     */
    public String getClub() {
        return Club;
    }

    /**
     *
     * @param Klub
     * The Club
     */
    public void setClub(String Klub) {
        this.Club = Klub;
    }

    /**
     *
     * @return
     * The Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     *
     * @param Naslov
     * The Title
     */
    public void setTitle(String Naslov) {
        this.Title = Naslov;
    }

    /**
     *
     * @return
     * The Text
     */
    public String getText() {
        return Text;
    }

    /**
     *
     * @param Tekst
     * The Text
     */
    public void setText(String Tekst) {
        this.Text = Tekst;
    }

    /**
     *
     * @return
     * The Music
     */
    public String getMusic() {
        return Music;
    }

    /**
     *
     * @param VrstaGlazbe
     * The Music
     */
    public void setMusic(String VrstaGlazbe) {
        this.Music = VrstaGlazbe;
    }

    /**
     *
     * @return
     * The Glazba
     */
    public List<String> getGlazba() {
        return Glazba;
    }

    /**
     *
     * @param Glazba
     * The Glazba
     */
    public void setGlazba(List<String> Glazba) {
        this.Glazba = Glazba;
    }

    /**
     *
     * @return
     * The Extra
     */
    public String getExtra() {
        return Extra;
    }

    /**
     *
     * @param extra
     * The Extra
     */
    public void setExtra(String extra) {
        this.Extra = extra;
    }

    /**
     *
     * @return
     * The pics
     */
    public List<String> getPics() {
        return pics;
    }

    /**
     *
     * @param pics
     * The pics
     */
    public void setPics(List<String> pics) {
        this.pics = pics;
    }

}