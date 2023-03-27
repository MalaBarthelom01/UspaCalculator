package CommonUtil;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.*;
import qa.vector.ui.pages.*;
//import qa.ml.ui.util.BrowserType;
import qa.vector.webactions.MasterBase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EmailUtil extends MasterBase {
    public EmailUtil() throws Exception {
    }


    @Test()
    public void emailTest() throws IOException {
        try{
            initBrowser(BrowserType.CHROME);

            BasePage.driver.setWebDriver(BrowserType.CHROME);
            String timestamp = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
            String fileName = "Automation_Report_"+timestamp+".html";
            String filePath = "file:///"+ System.getProperty("user.dir")+"/target/automation-reports/"+fileName;
            System.out.println(filePath);
            BasePage.driver.getWebDriver().get(filePath);
            Thread.sleep(3000);
            getScreenshot(BasePage.driver.getWebDriver());
            //SendEmail.mailSend();
        }catch (Exception e){
            System.out.println("Test failed");
            e.printStackTrace();

        }
    }


    @AfterMethod()
    public void close() throws Exception {
        try {
            BasePage.driver.closeBrowser();

        } catch (Exception e){}
    }

    String getScreenshot(WebDriver driver)throws IOException{
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        // after execution, you could see a folder "FailedTestsScreenshots" under src folder
        String scrFolderPath = System.getProperty("user.dir") + "/target/automation-reports/";
        File file = new File(scrFolderPath);
        if(file.exists() && file.isDirectory()){}else{
            new File(scrFolderPath).mkdir();
        }
        String fileName = "email_Snapshot.png";
        String destination = scrFolderPath + fileName;
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return fileName;
    }
}
