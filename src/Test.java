import java.io.FileNotFoundException;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        Crawler crawl = new Crawler("food");
        crawl.findPhilosophy();
    }
}
