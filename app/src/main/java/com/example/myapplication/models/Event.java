package com.example.myapplication.models;

public class Event {
    private String i;
    private String si;
    private String d;
    private String d1;
    private String d2;
    private int tt;
    private boolean favorite;

    public String getI() {return i;}

    public void setI(String i) {
        this.i = i;
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public int getTt() {
        return tt;
    }

    public void setTt(int tt) {
        this.tt = tt;
    }

    public boolean isFavorite() {return favorite;}

    public void setFavorite(boolean favorite) {this.favorite = favorite;}

    public String getD1() {return d1;}

    public void setD1(String d1) {this.d1 = d1;}

    public String getD2() {return d2;}

    public void setD2(String d2) {this.d2 = d2;}
}
