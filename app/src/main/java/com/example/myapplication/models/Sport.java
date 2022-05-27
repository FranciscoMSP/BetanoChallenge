package com.example.myapplication.models;

import java.util.ArrayList;
import java.util.List;

public class Sport {
    private String i;
    private String d;
    private List<Event> e;
    private boolean expanded = true;

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public List<Event> getE() {
        return e;
    }

    public void setE(List<Event> e) {
        this.e = e;
    }

    public boolean isExpanded() {return expanded;}

    public void setExpanded(boolean expanded) {this.expanded = expanded;}
}
