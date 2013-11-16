package com.androidSifter.sifters.exampleSifters;

import com.androidSifter.sifters.PebbleSifter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TeamTriviaAnswerSifter extends PebbleSifter {

    private final String URL = "http://www.triviaofcolorado.com/page.asp?subject=5";
    private final String ELEMENT_PATH = "#main .content";
    private final String SIFTER_NAME = "Team Trivia Free Answer";

    private Document doc;

    public TeamTriviaAnswerSifter() {

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
        Elements content = doc.select(ELEMENT_PATH);
        return content.html().replaceAll("<(.*?)>", "").trim();
    }

    @Override
    public String getFullName() {
        return SIFTER_NAME;
    }
}
