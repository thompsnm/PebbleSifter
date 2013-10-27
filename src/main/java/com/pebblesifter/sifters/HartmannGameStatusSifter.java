package com.pebblesifter.sifters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HartmannGameStatusSifter extends PebbleSifter {

    private final String URL = "http://inlinerink.com/Home";
    private final String ELEMENT_PATH = "#Game_Status p";
    private final String SIFTER_NAME = "Hartmann Game Status";

    private Document doc;

    public HartmannGameStatusSifter() {
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sift() {
        return doc.select(ELEMENT_PATH).text();
    }

    public String getName() {
        return SIFTER_NAME;
    }
}
