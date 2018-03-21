/**
 *
 */
public class WebPage {
    private static final String BASE = "https://en.wikipedia.org/wiki/";

    private String pageName;

    public WebPage(String name){
        this.pageName = name;
    }

    public String getPageName() {
        return pageName;
    }

    public String getURL() {
        return BASE + pageName;
    }
}
