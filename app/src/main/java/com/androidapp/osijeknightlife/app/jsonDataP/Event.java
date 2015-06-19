package com.androidapp.osijeknightlife.app.jsonDataP;

/**
 * Created by Toni P on 6/5/2015.
 */

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Event {

    @Expose
    private String club;
    @Expose
    private String title;
    @Expose
    private String text;
    @Expose
    private String music;
    @Expose
    private String extra;
    @Expose
    private List<String> pics = new ArrayList<String>();

    /**
     *
     * @return
     * The club
     */
    public String getClub() {
        return club;
    }

    /**
     *
     * @param club
     * The club
     */
    public void setClub(String club) {
        this.club = club;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The tekst
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The tekst
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The VrstaGlazbe
     */
    public String getMusic() {
        return music;
    }

    /**
     *
     * @param music
     * The VrstaGlazbe
     */
    public void setMusic(String music) {
        this.music = music;
    }

    /**
     *
     * @return
     * The dodatno
     */
    public String getExtra() {
        return extra;
    }

    /**
     *
     * @param extra
     * The dodatno
     */
    public void setExtra(String extra) {
        this.extra = extra;
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