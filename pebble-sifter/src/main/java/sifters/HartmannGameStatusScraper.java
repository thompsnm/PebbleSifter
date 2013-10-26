package sifters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HartmannGameStatusScraper extends PebbleSifter {

    private Document doc;

    private final String ELEMENT_PATH = "#Game_Status p";
    private final String SCRAPER_NAME = "Hartmann Game Status";

    public HartmannGameStatusScraper() {
        try {
            doc = Jsoup.connect("http://inlinerink.com/Home").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String scrape() {
        return doc.select(ELEMENT_PATH).text().toString();
    }

    public String getName() {
        return SCRAPER_NAME;
    }
}
