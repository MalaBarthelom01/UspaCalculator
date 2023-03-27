package CommonUtility;


import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import qa.vector.ui.pages.TestDataLoader;
import qa.vector.webactions.Driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ReportingMaster extends Driver {
    public enum Project {
        UIPROJECT, APIPROJECT
    }

    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;
    public static ExtentTest testNode;
    public static ExtentTest logger;
    public static Project project;
    public static HashMap<String, String> config;
    public static boolean reportGenerated = false;
    static final String timestamp = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    static final String reportname = "Automation_Report_" + timestamp + ".html";
    static int passcount = 0;
    static int failcount = 0;
    static int skipcount = 0;
    public static String responseTime=null;
    public static String apiOwnership = null;

    ReportingMaster() throws Exception {
        super();
        System.out.println("Reporting Master called");
    }

    public static void setProject(Project project) {
        ReportingMaster.project = project;
        System.out.println("Creating test report for Project : " + ReportingMaster.project);
    }


    public static void initReport(String suiteName) throws Exception {
        passcount = 0;
        failcount = 0;
        skipcount = 0;

        config = new TestDataLoader().getConfigData();
        if (reportGenerated) {
            System.out.println("Appending suite result to existing report");
            testNode = extent.createTest(suiteName);

        } else {
            String filepath = System.getProperty("user.dir") + "/target/automation-reports/" + reportname;
            
            reportGenerated = true;
            try {
                File file = new File(filepath);
                if (file.exists()) {
                    file.delete();
                    System.out.println("Deleting previous report entry");
                }
            } catch (Exception e) {
            }
            System.out.println("Report File name" + filepath);
            htmlReporter = new ExtentHtmlReporter(filepath);
            htmlReporter.setAppendExisting(true);
            // Create an object of Extent Reports
            extent = new ExtentReports();
            extent.attachReporter();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo("Host Name", "USPS");
            extent.setSystemInfo("Environment", config.get("ExecutionEnvironment").toUpperCase());
            extent.setSystemInfo("User Name", "QA Team");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("OS Version", System.getProperty("os.name")+"-"+System.getProperty("os.version"));
            extent.setAnalysisStrategy(AnalysisStrategy.SUITE);
            htmlReporter.config().setDocumentTitle("Execution summary");
            // Name of the report
            htmlReporter.config().setReportName("REPORT:USPS AUTOMATION REPORT--ENV-"+config.get("ExecutionEnvironment").toUpperCase());
            // Dark Theme
            htmlReporter.config().setTheme(Theme.DARK);
            String customcss = ".text-small{font-size:20px;} .report-name{font-size:19.5px;background-color:#1565C0 !important;font-weight:bold;padding:3px 6px;border-radius: 4px;} div.nav-wrapper > a{font-size: 19px; display:none;}  div.test-content div.test-steps table tbody tr td.step-details span{font-size: large;} td.timestamp{display: none;} td.status{display:none;} div.test-steps table thead tr th:nth-child(1){display:none;} div.test-steps table thead tr th:nth-child(2){display:none;} div.node-steps table thead tr th:nth-child(1){display:none;} div.node-steps table thead tr th:nth-child(2){display:none;} #dashboard-view td{font-weight: 1000; font-size: medium;} div.test-content span.category{display:none}";
            htmlReporter.config().setCSS(customcss);

            //Using create test method to create Suite tag in report
            testNode = extent.createTest(suiteName);

        }

    }

    public static void createTest(String testName, String description, String suitename) {

        if (testName.equals("SetStagingEnv") || testName.equals("SetPerfEnv") || testName.equals("SetStaging2Env")) {

        } else {
            logger = testNode.createNode(testName, MarkupHelper.createLabel(" Test Description: " + description, ExtentColor.BLUE).getMarkup()).assignCategory(suitename);
        }

    }

    public static void getResult(ITestResult result) throws Exception {
        if (result.getName().equals("SetStagingEnv") || result.getName().equals("SetPerfEnv") || result.getName().equals("SetStaging2Env")) {

        } else {
            if (result.getStatus() == ITestResult.FAILURE && !result.getName().equals("SetStagingEnv") && !result.getName().equals("SetPerfEnv")) {
                logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
                logger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
                if (null != responseTime) {
                    logger.log(Status.FAIL, MarkupHelper.createLabel(apiOwnership + " API Response time- " + responseTime + "ms", ExtentColor.INDIGO));
                    responseTime = null;
                    apiOwnership = "";
                }
                //To capture screenshot path and store the path of the screenshot in the string "screenshotPath"
                //We do pass the path captured by this method in to the extent reports using "logger.addScreenCapture" method.
                if (project.equals(Project.UIPROJECT)) {
                    String screenshotPath = getScreenShot(new Driver().getWebDriver(), result.getName(), true);
                    //To add it in the extent report
                    logger.fail("Test Case Failed Snapshot is below " + logger.addScreenCaptureFromPath(screenshotPath));
                }
                failcount++;
            } else if (result.getStatus() == ITestResult.SKIP && !result.getName().equals("SetStagingEnv") && !result.getName().equals("SetPerfEnv")) {
                logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
                if (null != responseTime) {
                    logger.log(Status.SKIP, MarkupHelper.createLabel(apiOwnership + " API Response time- " + responseTime + "ms", ExtentColor.INDIGO));
                    responseTime = null;
                    apiOwnership = "";
                }
                skipcount++;
            } else if (result.getStatus() == ITestResult.SUCCESS && !result.getName().equals("SetStagingEnv") && !result.getName().equals("SetPerfEnv")) {
                logger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
                if (null != responseTime) {
                    logger.log(Status.PASS, MarkupHelper.createLabel(apiOwnership + " API Response time- " + responseTime + "ms", ExtentColor.INDIGO));
                    responseTime = null;
                    apiOwnership = "";
                }
                passcount++;
            }
        }
    }

    public static void endReport() throws Exception {
        int total = passcount + failcount + skipcount;
        String finalStat = "This suite status " + passcount + " Out of " + total + " Passed";
        testNode.info(MarkupHelper.createLabel(finalStat, ExtentColor.BLUE));
        extent.flush();
        System.out.println("This after create report");
        takeReportScreenshot();
        cleanupOldScreenshots();
    }

    public static void cleanupOldScreenshots() throws ParseException {
        File folder = new File(System.getProperty("user.dir") + "/target/automation-reports/");
        File[] files = folder.listFiles();
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, -15);
        currentDate = c.getTime();
        String dateToCompare = new SimpleDateFormat("yyyyMMdd").format(currentDate);
        Date dateToComparewith = new SimpleDateFormat("yyyyMMdd").parse(dateToCompare);

        for (File file : files) {
            if (file.getName().endsWith(".png") && file.getName().contains("_ts")) {
                String datestamp = file.getName().split("_ts")[1];
                datestamp = datestamp.replaceAll(".png", "");
                Date date = new SimpleDateFormat("yyyyMMdd").parse(datestamp);
                if (date.before(dateToComparewith)) {
                    System.out.println("Deleting old screenshot :" + file.getName());
                    file.delete();
                }
            }
        }

    }

    public static String getScreenShot(WebDriver driver, String screenshotName, boolean appendTimeStamp) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        // after execution, you could see a folder "FailedTestsScreenshots" under src folder
        String scrFolderPath = System.getProperty("user.dir") + "/target/automation-reports/";
        File file = new File(scrFolderPath);
        if (file.exists() && file.isDirectory()) {
        } else {
            new File(scrFolderPath).mkdir();
        }
        String fileName;
        if (appendTimeStamp) {
            fileName = screenshotName + "_ts" + dateName + ".png";
        } else {
            fileName = screenshotName + ".png";
        }
        String destination = scrFolderPath + fileName;
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return fileName;
    }

    public static void takeReportScreenshot() {
        try {
            WebDriverManager.chromedriver().setup();
            HashMap<String, Object> chromePrefs = new HashMap();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("prefs", chromePrefs);
            chromeOptions.addArguments("test-type");
            chromeOptions.addArguments("--start-maximized");
            chromeOptions.addArguments("--disable-web-security");
            chromeOptions.addArguments("--allow-running-insecure-content");
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("window-size=1200x600");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("disable-infobars");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--no-sandbox");

            Map<String, Object> commandParams = new HashMap();
            commandParams.put("cmd", "Page.setDownloadBehavior");
            Map<String, String> params = new HashMap();
            params.put("behavior", "allow");
            commandParams.put("params", params);

            WebDriver driver = new ChromeDriver(chromeOptions);
            Thread.sleep(3000);
            String reportPath = "file:///" + System.getProperty("user.dir") + "/target/automation-reports/" + reportname;
            driver.get(reportPath);
            getScreenShot(driver, "email_Snapshot", false);
            driver.quit();
        } catch (Throwable e) {
            System.out.println("Could not take report screenshot for email");
            e.printStackTrace();
        }
    }


    public static void killAllChromeInstances() {
        try {
            String line;
            Process p1 = Runtime.getRuntime().exec("killall -9 chromedriver");
            Process p2 = Runtime.getRuntime().exec("pkill chrome");
            Process p = Runtime.getRuntime().exec("ps -e");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                //System.out.println(line);
                if (line.contains("chromedriver")) {
                    System.out.println(line);
                    System.out.println("found chromedriver instance on server");

                }//<-- Parse data here.

            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}