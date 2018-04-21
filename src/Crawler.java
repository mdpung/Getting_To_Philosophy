import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class Crawler {
    /**
     * Constants
     */
    private static final String P_TAG = "<p>";
    private static final String END_TAG = "</p>";
    private static final String A_TAG = "<a";

    /**
     * Instance Variables
     */
    private HashMap<Integer, WebPage> hashMap;
    PrintWriter pw;
    private WebPage start;

    /**
     * Constructor
     * @param page
     */
    public Crawler(String page) throws FileNotFoundException {
        this.start = new WebPage(page);
        this.hashMap = new HashMap<>();
        pw = new PrintWriter("PathToPhilosophy.txt");
    }

    /**
     * Adds a page name to the hash table with the given hashcode and name
     * @param hashCode
     * @param page
     */
    private void addPage(int hashCode, WebPage page) {
        hashMap.put(hashCode, page);
    }

    /**
     * Method that initiates the search for 'Philosophy' from the given
     * start location in constructor.
     */
    public void findPhilosophy() {
        WebPage tempPage = start;

        String pageName = tempPage.getPageName();
//        System.out.println(pageName);
        pw.println(pageName);

        // Keep advancing to the first article link till we find 'Philosophy'
        while(!tempPage.getPageName().equals("Philosophy")) {
            tempPage = scrapeWebPage(tempPage);
        }
        System.out.println("Done");
        pw.close();
    }

    /**
     * Gets the web page of wikipedia article and returns the first non-visited
     * article name.
     * @param webPage
     * @return First non-visited article name
     */
    private WebPage scrapeWebPage(WebPage webPage) {
        URL url;
        try {
            url = new URL(webPage.getURL());
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            WebPage article;
            // Keep searching while there are lines in the webpage
            while ((line = br.readLine()) != null) {
                // Fetches first non-visited article page name
                article = getArticle(line);

                // Checks if such and article page name exists
                if (ifWebPageExists(article)) return article;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if the article is null or not
     * @param article
     * @return
     *      null if the WebPage was found, the WebPage otherwise
     */
    private boolean ifWebPageExists(WebPage article) throws FileNotFoundException {

        if (article != null) {
            // Print out name
            String pageName = article.getPageName();
//            System.out.println(pageName);
            pw.println(pageName);
            return true;
        }

        return false;
    }

    /**
     * Searches for the first non-visited article page name
     * @param line
     * @return first non-visited article page name
     */
    private WebPage getArticle(String line) {
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
                    numParenthesis = getNumParenthesis(numParenthesis, sub, i);
                }

                // Checks that we are not in any parenthesis
                if (numParenthesis == 0) {

                    // Checks if there is an '<a>' tag, where hyper links are located (article pages)
                    if (sub.equals(A_TAG)) {
                        // Remove 'wiki/' and other html content from the string so we can just have the word
                        String modifiedString = modifyString(s.next());

                        // Checks to see the article is a valid page
                        WebPage page = getWebPage(modifiedString);
                        if (page != null) return page;
                    }
                }
            }
            s.close();
        }
        return null;
    }

    /**
     *
     * @param modifiedString
     * @return
     */
    private WebPage getWebPage(String modifiedString) {
        if (isValidArticle(modifiedString)) {
            int stringHashCode = modifiedString.hashCode();

            // Checks if article name is not already visited
            if (!hashMap.containsKey(stringHashCode)) {
                // Adds page name to hash map so we know it has already been visited
                WebPage page = new WebPage(modifiedString);
                addPage(stringHashCode, page);
                return page;
            }
        }
        return null;
    }

    /**
     * Gets number of pair parenthesis it is in
     * @param numParenthesis
     * @param sub
     * @param i
     * @return
     *      number of pair parenthesis it is in
     */
    private int getNumParenthesis(int numParenthesis, String sub, int i) {
        if (sub.charAt(i) == '(') {
            numParenthesis++;
        } else if (sub.charAt(i) == ')') {
            numParenthesis--;
        }
        return numParenthesis;
    }

    /**
     * Checks if link is not to an image or section of a page or other link
     * @param modifiedString
     * @return
     */
    private boolean isValidArticle(String modifiedString) {
        return !modifiedString.contains("#")
                && !modifiedString.contains(":") && !modifiedString.contains(".org");
    }


    /**
     * Cuts out href="\wiki" from the substring
     * @param s
     * @return
     *      String after parsing
     */
    private String modifyString(String s) {
        String modifiedString;
        modifiedString = s.substring(12, s.length() - 1);

        return modifiedString;
    }
}
