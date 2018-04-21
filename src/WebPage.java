public class WebPage {

    /**
     * Instance variables
     */
    private static final String BASE = "https://en.wikipedia.org/wiki/";
    private String pageName;

    /**
     * Constructor
     * @param name
     */
    public WebPage(String name){
        this.pageName = name;
    }

    /**
     * Accessor for the name of the article
     * @return
     *      name of the article
     */
    public String getPageName() {
        return pageName;
    }

    /**
     * Combines the base Wikipedia URL with article URL
     * @return
     *      URL of the article
     */
    public String getURL() {
        return BASE + pageName;
    }
}
