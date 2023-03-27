package qa.vector.ui.pages;


import qa.vector.webactions.Driver;

/**
 * HomePage class contains static Driver reference and default object repository. The default object repository can be changed in the pages/classes extending HomePage class. Common methods can be
 * placed in this class.
 */
public class BasePage {
    public static Driver driver;
    static final String script = "arguments[0].setAttribute('style','background: yellow; border: 2px solid red;');";

    public BasePage() throws Exception {
        driver = new Driver();
        
    }
}
