package com.androidapp.osijeknightlife.app.jsonDataP;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    @Expose
    private Integer Dan;
    @Expose
    private Integer Mj;
    @Expose
    private Integer God;
    @Expose
    private List<Event> Events = new ArrayList<Event>();

    /**
     *
     * @return
     * The Dan
     */
    public Integer getDan() {
        return Dan;
    }

    /**
     *
     * @param Dan
     * The Dan
     */
    public void setDan(Integer Dan) {
        this.Dan = Dan;
    }

    /**
     *
     * @return
     * The Mj
     */
    public Integer getMj() {
        return Mj;
    }

    /**
     *
     * @param Mj
     * The Mj
     */
    public void setMj(Integer Mj) {
        this.Mj = Mj;
    }

    /**
     *
     * @return
     * The God
     */
    public Integer getGod() {
        return God;
    }

    /**
     *
     * @param God
     * The God
     */
    public void setGod(Integer God) {
        this.God = God;
    }

    /**
     *
     * @return
     * The Events
     */
    public List<Event> getEvents() {
        return Events;
    }

    /**
     *
     * @param Events
     * The Events
     */
    public void setEvents(List<Event> Events) {
        this.Events = Events;
    }

}