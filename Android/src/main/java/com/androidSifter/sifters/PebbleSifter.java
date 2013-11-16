package com.androidSifter.sifters;

public abstract class PebbleSifter {

    public abstract void connect();

    public abstract String sift();

    public abstract String getFullName();

    public String getPebbleName() {
        return getFullName();
    }

}
