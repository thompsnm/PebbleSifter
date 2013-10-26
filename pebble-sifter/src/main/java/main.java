public class main {
    public static void main(String [] args) {
        StoneSoupScraper stoneSoupScraper = new HartmannGameStatusScraper();
        System.out.println(stoneSoupScraper.getName());
        System.out.println(stoneSoupScraper.scrape());
    }
}
