import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Where would you like to start?");
        String start = (new Scanner(System.in)).nextLine();
        Crawler crawl = new Crawler(start);
        crawl.findPhilosophy();
    }
}
