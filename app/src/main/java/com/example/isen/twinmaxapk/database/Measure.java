package com.example.isen.twinmaxapk.database;


/**
 * Created by isen on 23/02/2016.
 */
public class Measure {

    private int c1;
    private int c2;
    private int c3;
    private int c4;

    public Measure(int c4, int c1, int c2, int c3) {
        this.c4 = c4;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    public int getC1() {
        return c1;
    }

    public void setC1(int c1) {
        this.c1 = c1;
    }

    public int getC2() {
        return c2;
    }

    public void setC2(int c2) {
        this.c2 = c2;
    }

    public int getC3() {
        return c3;
    }

    public void setC3(int c3) {
        this.c3 = c3;
    }

    public int getC4() {
        return c4;
    }

    public void setC4(int c4) {
        this.c4 = c4;
    }
}
