import sifters.HartmannGameStatusScraper;
import sifters.PebbleSifter;

public class main {
    public static void main(String [] args) {
        PebbleSifter pebbleSifter = new HartmannGameStatusScraper();
        System.out.println(pebbleSifter.getName());
        System.out.println(pebbleSifter.scrape());
    }
}
