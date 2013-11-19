package com.androidSifter.sifters.exampleSifters;

import com.androidSifter.sifters.PebbleSifter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HartmannGameStatusSifter extends PebbleSifter {

    private final String URL = "http://inlinerink.com/Home";
    private final String ELEMENT_PATH = "#Game_Status p";
    private final String SIFTER_FULL_NAME = "Hartmann Game Status";
    private final String SIFTER_PEBBLE_NAME = "Hartmann";

    private Document doc;

    public HartmannGameStatusSifter() {

    }

    @Override
    public void connect() {
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String sift() {
        return doc.select(ELEMENT_PATH).text();
    }

    @Override
    public String getFullName() {
        return SIFTER_FULL_NAME;
    }

    @Override
    public String getPebbleName() {
        return SIFTER_PEBBLE_NAME;
    }
}
