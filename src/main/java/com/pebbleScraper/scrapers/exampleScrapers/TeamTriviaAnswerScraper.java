package com.pebbleScraper.scrapers.exampleScrapers;

import com.pebbleScraper.scrapers.PebbleSiteScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TeamTriviaAnswerScraper extends PebbleSiteScraper {

    private final String URL = "http://www.triviaofcolorado.com/page.asp?subject=5";
    private final String ELEMENT_PATH = "#main .content";
    private final String SIFTER_NAME = "Team Trivia Free Answer";

    private Document doc;

    public TeamTriviaAnswerScraper() {
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String scrape() {
        Elements content = doc.select(ELEMENT_PATH);
        return content.html().replaceAll("<(.*?)>", "").trim();
    }

    @Override
    public String getName() {
        return SIFTER_NAME;
    }
}
