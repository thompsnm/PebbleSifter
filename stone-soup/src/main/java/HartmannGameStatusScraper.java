import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HartmannGameStatusScraper {

    private Document doc;

    private final String ELEMENT_PATH = "#Game_Status p";

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
}
