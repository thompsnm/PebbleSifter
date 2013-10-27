package com.pebblesifter.sifters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TeamTriviaAnswerSifter extends PebbleSifter {

    private final String URL = "http://www.triviaofcolorado.com/page.asp?subject=5";
    private final String ELEMENT_PATH = "#main .content";
    private final String SIFTER_NAME = "Team Trivia Free Answer";

    private Document doc;

    public TeamTriviaAnswerSifter() {
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
    public String getName() {
        return SIFTER_NAME;
    }
}
