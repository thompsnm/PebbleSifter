public class main {
    public static void main(String [] args) {
        HartmannGameStatusScraper hartmannGameStatusScraper = new HartmannGameStatusScraper();
        System.out.println(hartmannGameStatusScraper.getName());
        System.out.println(hartmannGameStatusScraper.scrape());
    }
}
