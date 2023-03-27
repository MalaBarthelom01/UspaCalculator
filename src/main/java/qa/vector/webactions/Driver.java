package qa.vector.webactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import qa.vector.ui.pages.TestDataLoader;
import qa.vector.ui.pages.BrowserType;
import qa.vector.ui.pages.IdentifierMethod;
import qa.vector.ui.pages.BasePage;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Driver {
    private static WebDriver webDriver = null;
    private DesiredCapabilities desiredCapabilities = null;
    public static WebDriverWait wait = null;
    public HashMap<String, String> config = null;
    private boolean noException = false;



    public Driver() throws Exception {
        config = getConfigData();
    }
    
    /**
     * Gets Config data from config.properties file
     *
     * @return HashMap
     */
    public HashMap<String, String> getConfigData() throws Exception {
        HashMap<String, String> hashMap = null;
        hashMap = new TestDataLoader().getConfigData();
        return hashMap;
    }

  
    /**
     * Sets up the browser to be used for running automation
     *
     * @param browser : See {@link BrowserType}
     * @throws Exception
     */
    public void
    setWebDriver(BrowserType browser) throws Exception {
        boolean flag = false;
        if (config.get("browserHeadless").equals("true")) {
            flag = true;
        }
        if (config.get("execution").equals("local")) {
            String downloadFilepath;
            if (System.getProperty("os.name").contains("Windows")) {
                downloadFilepath = System.getProperty("user.dir") + "\\Downloads";
            } else {
                downloadFilepath = System.getProperty("user.dir") + "/Downloads";
            }
            switch (browser) {
                case FIREFOX:
                    WebDriverManager.firefoxdriver().version("v0.23.0").setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setCapability("marionette", true);
                    firefoxOptions.addPreference("browser.download.folderList", 2);
                    firefoxOptions.addPreference("browser.download.manager.showWhenStarting", false);
                    //Set downloadPath
                    firefoxOptions.addPreference("browser.download.dir", downloadFilepath);
                    //Set File Open &amp; Save preferences
                    firefoxOptions.addPreference("browser.helperApps.neverAsk.openFile", "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
                    firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk",
                            "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
                    firefoxOptions.addPreference("browser.helperApps.alwaysAsk.force", false);
                    firefoxOptions.addPreference("browser.download.manager.alertOnEXEOpen", false);
                    firefoxOptions.addPreference("browser.download.manager.focusWhenStarting", false);
                    firefoxOptions.addPreference("browser.download.manager.useWindow", false);
                    firefoxOptions.addPreference("browser.download.manager.showAlertOnComplete", false);
                    firefoxOptions.addPreference("browser.download.manager.closeWhenDone", false);
                    if (flag) {
                        firefoxOptions.addArguments("--headless");
                        firefoxOptions.addArguments("window-size=1200x600");
                    }
                    webDriver = new FirefoxDriver(firefoxOptions);
                    break;

                case CHROME:
                    WebDriverManager.chromedriver().setup();
                    HashMap<String, Object> chromePrefs = new HashMap();
                    chromePrefs.put("profile.default_content_settings.popups", 0);
                    chromePrefs.put("download.default_directory", downloadFilepath);
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setExperimentalOption("prefs", chromePrefs);
                    chromeOptions.addArguments("test-type");
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-web-security");
                    chromeOptions.addArguments("--allow-running-insecure-content");
                    chromeOptions.addArguments("--ignore-certificate-errors");
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    System.setProperty("webdriver.chrome.silentOutput", "true");
                    if (flag) {
                        chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("window-size=1680x952");
                        chromeOptions.addArguments("--disable-notifications");
                        chromeOptions.addArguments("--start-maximized");
                        chromeOptions.addArguments("disable-infobars");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--allow-running-insecure-content");
                        chromeOptions.addArguments("--disable-extensions");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                        ChromeDriverService driverService = ChromeDriverService.createDefaultService();

                        webDriver = new ChromeDriver(driverService, chromeOptions);

                        Map<String, Object> commandParams = new HashMap();
                        commandParams.put("cmd", "Page.setDownloadBehavior");
                        Map<String, String> params = new HashMap();
                        params.put("behavior", "allow");
                        params.put("downloadPath", downloadFilepath);
                        commandParams.put("params", params);
                        ObjectMapper objectMapper = new ObjectMapper();
                        HttpClient httpClient = HttpClientBuilder.create().build();

                        String command = objectMapper.writeValueAsString(commandParams);

                        String u = driverService.getUrl().toString() + "/session/" + ((ChromeDriver) webDriver).getSessionId() + "/chromium/send_command";

                        HttpPost request = new HttpPost(u);
                        request.addHeader("content-type", "application/json");
                        request.setEntity(new StringEntity(command));
                        httpClient.execute(request);
                    } else {
                        webDriver = new ChromeDriver(chromeOptions);
                    }
                    break;
                case IE:
                    WebDriverManager.iedriver().version("3.4.0").arch32().setup();
                    InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                    internetExplorerOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    internetExplorerOptions.setCapability("nativeEvents", false);
                    internetExplorerOptions.setCapability("unexpectedAlertBehaviour", "accept");
                    internetExplorerOptions.setCapability("ignoreProtectedModeSettings", true);
                    internetExplorerOptions.setCapability("disable-popup-blocking", true);
                    internetExplorerOptions.setCapability("enablePersistentHover", true);
                    internetExplorerOptions.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
                    webDriver = new InternetExplorerDriver(internetExplorerOptions);
                    break;
                case IEEDGE:
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    webDriver = new EdgeDriver(edgeOptions);
                    break;

                case SAFARI:
                    DesiredCapabilities safariCapabilities = new DesiredCapabilities();
                    safariCapabilities.setCapability("safari.options.dataDir", downloadFilepath);
                    SafariOptions safariOptions = new SafariOptions(safariCapabilities);
                    webDriver = new SafariDriver(safariOptions);
                    break;

                default:
                    throw new Exception(browser + " Browser has not been Implemented");
            }
        } else if (config.get("execution").equals("grid")) {
            switch (browser) {
                case FIREFOX:
                    desiredCapabilities = DesiredCapabilities.firefox();
                    break;
                case CHROME:
                    desiredCapabilities = DesiredCapabilities.chrome();
                    break;
                case IE:
                    desiredCapabilities = DesiredCapabilities.internetExplorer();
                    break;
                default:
                    throw new Exception(browser + " Browser has not been Implemented");
            }
        }
        wait = new WebDriverWait(getWebDriver(), Integer.parseInt(config.get("TimeOut")));
        webDriver.manage().timeouts().implicitlyWait(Long.parseLong(config.get("ImplicitWait")), TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
    }

    /**
     * Sets up the Webdriver for running automation using the supplied browser
     * type and desired capabilities
     *
     * @param browser
     * @param desiredCapabilities
     * @throws Exception
     */
    public void setWebDriver(BrowserType browser, DesiredCapabilities desiredCapabilities) throws Exception {


        if (config.get("execution").equals("local")) {
            switch (browser) {
                case FIREFOX:
                    webDriver = new FirefoxDriver(desiredCapabilities);
                    break;
                case IE:
                    desiredCapabilities.setCapability("nativeEvents", false);
                    desiredCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
                    desiredCapabilities.setCapability("ignoreProtectedModeSettings", true);
                    desiredCapabilities.setCapability("disable-popup-blocking", true);
                    desiredCapabilities.setCapability("enablePersistentHover", true);
                    desiredCapabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
                    webDriver = new InternetExplorerDriver(desiredCapabilities);
                    break;
                case CHROME:
                    webDriver = new ChromeDriver(desiredCapabilities);
                case HEADLESS:
                    webDriver = new ChromeDriver(desiredCapabilities);
                    break;
                default:
                    throw new Exception(browser + " Browser has not been Implemented");
            }
            wait = new WebDriverWait(getWebDriver(), Integer.parseInt(config.get("TimeOut")));
            webDriver.manage().timeouts().implicitlyWait(Long.parseLong(config.get("ImplicitWait")), TimeUnit.SECONDS);
            webDriver.manage().window().maximize();
        } else if (config.get("execution").equals("grid")) {
            webDriver = new RemoteWebDriver(new URL("http://" + config.get("hubIP") + ":" + config.get("hubPort") + "/wd/hub"), desiredCapabilities);
        }
    }

    public String SwichToNewWindowAndReturnParentWindowHandle() {
        String parentWindow = getWebDriver().getWindowHandle();

        Set<String> windows = getWebDriver().getWindowHandles();

        Iterator itr = windows.iterator();
        String childWindow = null;
        while (itr.hasNext()) {
            childWindow = itr.next().toString();

            if (!parentWindow.equals(childWindow)) {
                getWebDriver().switchTo().window(childWindow);
                getWebDriver().manage().window().maximize();
            }
        }
        return parentWindow;
    }

    /**
     * Gets the current instance of webDriver
     *
     * @return WebDriver
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Opens URL in given browser
     *
     * @param browser
     * @throws Exception
     */
    public void openBrowser(BrowserType browser) throws Exception {
        setWebDriver(browser);
        String url = "";
        if (config.get("ExecutionEnvironment").equalsIgnoreCase("USPS")) {
            url = config.get("urlUSPSEnv");
        } 
        
       /* else if (config.get("ExecutionEnvironment").equalsIgnoreCase("Environment1")) {
            url = config.get("urlPerfEnv");
        } else if (config.get("ExecutionEnvironment").equalsIgnoreCase("Environment1")) {
            url = config.get("urlFun2Env");
        }*/

        
else {
	throw new NoSuchElementException("No such element(Url) could be found with existing config file");
        
        }
        getWebDriver().get(url);
    }

    /**
     * Clicks on element
     *
     * @param elementIdentifier
     */
    public void click(HashMap<String, String> elementIdentifier) {

        WebElement element = find(elementIdentifier);

        element.click();

    }

    /**
     * This method is for negative scenarios where we just want to check the visibility of elements
     *
     * @param elementIdentifier
     * @return boolean
     */
    public boolean checkElementVisibility(HashMap<String, String> elementIdentifier) {
        noException = true;
        int timeOutInSeconds = Integer.parseInt("1");
        return waitForElementToAppear(elementIdentifier, timeOutInSeconds);
    }

    /**
     * Closes the browser
     */
    public void closeBrowser() {
        getWebDriver().quit();

    }

    /**
     * Closes the window
     */
    public void closeWindow() {
        getWebDriver().close();

    }

    /**
     * Executes javascript on identified web element
     *
     * @param elementIdentifier
     * @param script
     */
    public void executeJavaScript(HashMap<String, String> elementIdentifier, String script) {
        WebElement element = find(elementIdentifier);
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        js.executeScript(script, element);
    }

    public void executeJavaScript(WebElement element, String script) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        js.executeScript(script, element);
    }


    /**
     * Finds all WebElements
     *
     * @param elementIdentifier
     * @return List<WebElement>
     */
    public List<WebElement> findAll(HashMap<String, String> elementIdentifier) {
        List<WebElement> foundElements = null;
        IdentifierMethod[] identifierMethods = IdentifierMethod.values();
        Set<IdentifierMethod> method = new HashSet();
        for (IdentifierMethod value : identifierMethods) {
            method.add(value);
        }
        Iterator<IdentifierMethod> iterator = method.iterator();
        IdentifierMethod locator = null;
        do {
            locator = iterator.next();
            if (elementIdentifier.get(locator.toString()) != null) {
                foundElements = findAll(elementIdentifier, locator);
            }
        } while (foundElements == null && iterator.hasNext());

        if (foundElements == null) {

            System.out.println(" Failed : No such element could be found with existing repository locators ");
            throw new NoSuchElementException("No such element could be found with existing repository locators");
        }

        return foundElements;
    }

    /**
     * Finds all WebElements using locators
     *
     * @param elementIdentifier
     * @param method
     * @return List<WebElement>
     */
    private List<WebElement> findAll(HashMap<String, String> elementIdentifier, IdentifierMethod method) {
        List<WebElement> foundElements = null;
        try {
            switch (method) {
                case ID:
                    foundElements = getWebDriver().findElements(By.id(elementIdentifier.get(method.toString())));
                    break;
                case XPATH:
                    foundElements = getWebDriver().findElements(By.xpath(elementIdentifier.get(method.toString())));
                    break;
                case NAME:
                    foundElements = getWebDriver().findElements(By.name(elementIdentifier.get(method.toString())));
                    break;
                case LINKTEXT:
                    foundElements = getWebDriver().findElements(By.linkText(elementIdentifier.get(method.toString())));
                    break;
                case PARTIALLINKTEXT:
                    foundElements = getWebDriver()
                            .findElements(By.partialLinkText(elementIdentifier.get(method.toString())));
                    break;
                case CLASSNAME:
                    foundElements = getWebDriver().findElements(By.className(elementIdentifier.get(method.toString())));
                    break;
                case CSSSELECTOR:
                    foundElements = getWebDriver().findElements(By.cssSelector(elementIdentifier.get(method.toString())));
                    break;
                case TAGNAME:
                    foundElements = getWebDriver().findElements(By.tagName(elementIdentifier.get(method.toString())));
                    break;
                default:
                    find(elementIdentifier);
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
        return foundElements;
    }

    /**
     * Find element
     *
     * @param elementIdentifier present in element repository
     * @return WebElement
     */
    public WebElement find(HashMap<String, String> elementIdentifier) {
        WebElement foundElement = null;
        IdentifierMethod[] identifierMethods = IdentifierMethod.values();
        Set<IdentifierMethod> method = new HashSet();
        for (IdentifierMethod value : identifierMethods) {
            method.add(value);
        }
        Iterator<IdentifierMethod> iterator = method.iterator();
        IdentifierMethod locator = null;

        do {
            locator = iterator.next();
            if (elementIdentifier.get(locator.toString()) != null) {
                foundElement = find(elementIdentifier, locator);
            }
        } while (foundElement == null && iterator.hasNext());

        if (foundElement == null) {
            if (elementIdentifier.containsKey("XPATH")) {
                if (noException != true) {
                    System.out.println(getActionName() + " : " + elementIdentifier
                            + " Failed \n No such element could be found with existing repository locators : " + elementIdentifier.get("XPATH"));
                    noException = false;
                }
                throw new NoSuchElementException("No such element could be found with locator XPATH : " + elementIdentifier.get("XPATH"));
            } else {
                if (noException != true) {
                    System.out.println(getActionName() + " : " + elementIdentifier
                            + " Failed \n No such element could be found with existing repository locators ");
                    noException = false;
                }
                throw new NoSuchElementException("No such element could be found with existing repository locators");
            }
        }

        return foundElement;
    }

    /**
     * Find element using locators
     *
     * @param method
     * @param elementIdentifier
     * @return WebElement
     */
    private WebElement find(HashMap<String, String> elementIdentifier, IdentifierMethod method) {
        WebElement foundElement = null;
        try {
            WebDriverWait wait = new WebDriverWait(getWebDriver(), 30);
            switch (method) {
                case ID:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Failed to find element " + e.getMessage());
                    }
                    foundElement = getWebDriver().findElement(By.id(elementIdentifier.get(method.toString())));
                    break;
                case XPATH:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Failed to find element " + e.getMessage());
                    }
                    foundElement = getWebDriver().findElement(By.xpath(elementIdentifier.get(method.toString())));
                    break;
                case NAME:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Failed to find element " + e.getMessage());
                    }
                    foundElement = getWebDriver().findElement(By.name(elementIdentifier.get(method.toString())));
                    break;
                case LINKTEXT:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Failed to find element " + e.getMessage());
                    }
                    foundElement = getWebDriver().findElement(By.linkText(elementIdentifier.get(method.toString())));
                    break;
                case PARTIALLINKTEXT:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Failed to find element " + e.getMessage());
                    }
                    foundElement = getWebDriver().findElement(By.partialLinkText(elementIdentifier.get(method.toString())));
                    break;
                case CLASSNAME:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Failed to find element " + e.getMessage());
                    }
                    foundElement = getWebDriver().findElement(By.className(elementIdentifier.get(method.toString())));
                    break;
                case CSSSELECTOR:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Failed to find element " + e.getMessage());
                    }
                    foundElement = getWebDriver().findElement(By.cssSelector(elementIdentifier.get(method.toString())));
                    break;
                case TAGNAME:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Failed to find element " + e.getMessage());
                    }
                    foundElement = getWebDriver().findElement(By.tagName(elementIdentifier.get(method.toString())));
                    break;
                default:
                    find(elementIdentifier);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return foundElement;
    }

    /**
     * Gets the text on the web element
     *
     * @param elementIdentifier
     * @return String
     */
    public String getText(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);

        return element.getText();
    }
    public String getAttributeSrc(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);

        return element.getAttribute("src");
    }
    /**
     * Selects element in dropdown by visible text
     *
     * @param elementIdentifier
     * @param visibleText
     */
    public void selectItemByVisibleText(HashMap<String, String> elementIdentifier, String visibleText) {
        WebElement element = find(elementIdentifier);

        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(visibleText);
    }

    /**
     * Sets text on the web element
     *
     * @param elementIdentifier
     * @param text
     */
    public void setText(HashMap<String, String> elementIdentifier, String text) {
        WebElement element = find(elementIdentifier);

        try {
            if (element.isDisplayed() && element.isEnabled()) {
                //Hard code wait to solve the problem of send key partial text set
                Thread.sleep(1000);
                element.clear();
                element.sendKeys(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Checks the checkbox or radiobutton
     *
     * @param elementIdentifier
     */
    public void check(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        try {
            if (element.isDisplayed() && element.isEnabled() && !element.isSelected()) {
                element.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for an element to appear within the default timeOut in seconds given in the config file.
     *
     * @param elementIdentifier
     * @return boolean
     */
    public boolean waitForElementToAppear(HashMap<String, String> elementIdentifier) {
        int timeOutInSeconds = Integer.parseInt(config.get("TimeOut"));
        return waitForElementToAppear(elementIdentifier, timeOutInSeconds);
    }

    /**
     * Waits for an element to appear within the given timeOut in seconds.
     *
     * @param elementIdentifier
     * @param timeOutInSeconds
     * @return boolean
     */
    public boolean waitForElementToAppear(HashMap<String, String> elementIdentifier, int timeOutInSeconds) {
        boolean flag = false;
        try {
            turnOffImplicitWaits();
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver()).withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
                    .pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchFrameException.class);
            wait.until(ExpectedConditions.visibilityOf(find(elementIdentifier)));
            turnOnImplicitWaits();
            flag = true;

        } catch (Exception e) {

            flag = false;
        }

        return flag;
    }

    private void turnOffImplicitWaits() {
        getWebDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    private void turnOnImplicitWaits() {
        int timeOutInSeconds = Integer.parseInt(config.get("ImplicitWait"));
        getWebDriver().manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS);
    }

    private String getActionName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static BrowserType getBrowser(String browser) {
        if (browser.equals("CHROME")) {
            return BrowserType.CHROME;
        } else if (browser.equals("FIREFOX")) {
            return BrowserType.FIREFOX;
        } else if (browser.equals("IE")) {
            return BrowserType.IE;
        } else {
            return null;
        }
    }

    public void scrollToElement(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static String getBrowserName() {
        Capabilities caps = ((RemoteWebDriver) BasePage.driver.getWebDriver()).getCapabilities();
        return caps.getBrowserName().toUpperCase();
    }
    
}