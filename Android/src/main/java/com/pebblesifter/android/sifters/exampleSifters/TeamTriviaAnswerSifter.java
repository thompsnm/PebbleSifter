package com.pebblesifter.android.sifters.exampleSifters;

import com.pebblesifter.android.sifters.PebbleSifter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TeamTriviaAnswerSifter extends PebbleSifter {

    private final String URL = "http://www.triviaofcolorado.com/page.asp?subject=5";
    private final String ELEMENT_PATH = "#main .content";
    private final String SIFTER_FULL_NAME = "Team Trivia Free Answer";
    private final String SIFTER_PEBBLE_NAME = "Team Trivia";

    private Document doc;

    public TeamTriviaAnswerSifter() {
        connect();
    }

    @Override
    public String sift() {
        Elements content = doc.select(ELEMENT_PATH);
        return content.html().replaceAll("<(.*?)>", "").trim();
    }

    @Override
    public String getFullName() {
        return SIFTER_FULL_NAME;
    }

    @Override
    public String getPebbleName() {
        return SIFTER_PEBBLE_NAME;
    }

    @Override
    public void refresh() {
        connect();
    }

    private void connect() {
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
            doc = null;
        }
    }
}
