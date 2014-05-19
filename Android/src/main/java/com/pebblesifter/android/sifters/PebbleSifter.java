package com.pebblesifter.android.sifters;

public abstract class PebbleSifter {

    public abstract String sift();

    public abstract String getFullName();

    public String getPebbleName() {
        return getFullName();
    }

    public abstract void refresh();

}
