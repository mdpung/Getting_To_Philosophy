import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class Crawler {
    /**
     * Constants
     */
    private static final String DESTINATION = "https://en.wikipedia.org/wiki/Philosophy";
    private static final String BASE = "https://en.wikipedia.org/wiki/";
    static final String P_TAG = "<p>";
    static final String END_TAG = "</p>";
    static final String A_TAG = "<a";

    /**
     * Instance Variables
     */
    private HashMap<Integer, String> hashMap;
    private String start;

    /**
     * Constructor
     * @param page
     */
    public Crawler(String page) {
        this.start = page;
        this.hashMap = new HashMap<>();
    }

    /**
     * Adds a page name to the hash table with the given hashcode and name
     * @param hashCode
     * @param page
     */
    private void addPage(int hashCode, String page) {
        hashMap.put(hashCode, page);
    }

    /**
     * Method that initiates the search for 'Philosophy' from the given
     * start location in constructor.
     */
    public void findPhilosophy() {
        String tempPage = start;
        System.out.println(tempPage);

        // Keep advancing to the first article link till we find 'Philosophy'
        while(!tempPage.equals("Philosophy")) {
            tempPage = scrapeWebPage(tempPage);
        }
        System.out.println("Done");
    }

    /**
     * Gets the web page of wikipedia article and returns the first non-visited
     * article name.
     * @param webPage
     * @return First non-visited article name
     */
    private String scrapeWebPage(String webPage) {
        URL url;
        try {
            url = new URL(BASE + webPage);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            String article;

            // Keep searching while there are lines in the webpage
            while ((line = br.readLine()) != null) {
                // Fetches first non-visited article page name
                article = getArticle(line);

                // Checks if such and article page name exists
                if (article != null) {
                    // Print out name
                    System.out.println(article);
                    return article;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Searches for the first non-visited article page name
     * @param line
     * @return first non-visited article page name
     */
    private String getArticle(String line) {
        Scanner s;

        // We advance if the line contains opening '<p>' and closing '</p>' paragraph tags
        if ((line.contains(P_TAG) && line.contains(END_TAG))) {
            s = new Scanner(line);

            int numParenthesis = 0;

            // Iterate through each word
            while (s.hasNext()) {
                String sub = s.next();

                // Keep track if we are in parenthesis or not
                for (int i = 0; i < sub.length(); i++) {
                    if (sub.charAt(i) == '(') {
                        numParenthesis++;
                    } else if (sub.charAt(i) == ')') {
                        numParenthesis--;
                    }
                }

                // Checks that we are not in any parenthesis
                if (numParenthesis == 0) {

                    // Checks if there is an '<a>' tag, where hyper links are located (article pages)
                    if (sub.equals(A_TAG)) {
                        // Remove 'wiki/' and other html content from the string so we can just have the word
                        String modifiedString = modifyString(s.next());

                        // Checks to see the article is a valid page
                        if (!modifiedString.contains("#")
                                && !modifiedString.contains(":") && !modifiedString.contains(".org")) {
                            int stringHashCode = modifiedString.hashCode();

                            // Checks if article name is not already visited
                            if (!hashMap.containsKey(stringHashCode)) {
                                // Adds page name to hash map so we know it has already been visited
                                addPage(stringHashCode, modifiedString);
                                return modifiedString;
                            }
                        }

                    }
                }
            }
            s.close();
        }
        return null;
    }


    //Cuts out href="\wiki" from the substring
    private String modifyString(String s) {
        String modifedString;
        modifedString = s.substring(12, s.length() - 1);

        return modifedString;
    }
}
