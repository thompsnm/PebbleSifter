package com.pebbleScraper.scrapers.exampleScrapers;

import com.pebbleScraper.scrapers.PebbleSiteScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HartmannGameStatusScraper extends PebbleSiteScraper {

    private final String URL = "http://inlinerink.com/Home";
    private final String ELEMENT_PATH = "#Game_Status p";
    private final String SIFTER_NAME = "Hartmann Game Status";

    private Document doc;

    public HartmannGameStatusScraper() {
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String scrape() {
        return doc.select(ELEMENT_PATH).text();
    }

    public String getName() {
        return SIFTER_NAME;
    }
}
