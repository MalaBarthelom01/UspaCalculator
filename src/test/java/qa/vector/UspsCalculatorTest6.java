package qa.vector;

import java.util.ArrayList;
//import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import CommonUtility.ReportingListener;
import CommonUtility.ReportingMaster;
import qa.vector.ui.pages.BasePage;
import qa.vector.ui.pages.BrowserType;
import qa.vector.ui.pages.UspsPage;
import qa.vector.webactions.Driver;
import qa.vector.webactions.MasterBase;
/**
 * @author Mala
 */
@Listeners(ReportingListener.class)
public class UspsCalculatorTest6 extends MasterBase {


	public UspsCalculatorTest6() throws Exception {
		//super();
		// TODO Auto-generated constructor stub
	}

	

	@BeforeMethod()
	public void open() throws Exception {
		try{
			
			System.out.println("In before test");
			initBrowser(BrowserType.SAFARI);
			BasePage.driver.openBrowser(browser);
			startATURecorder();
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(description = "Type3:Home Depot box Large - (28 in. L x 15 in. W x 16 in. D)")
	public void uspsCalTest3() {
		reportLog = new ArrayList<>();
		ReportingMaster.setProject(ReportingMaster.Project.UIPROJECT);
		
		try {
			
			UspsPage uspPage = new UspsPage();
			//uspPage.login();
			reportLog.add("Navigate to USPS");
			uspPage.clickTitle("Retail Postage Price Calculator");
			uspPage.selectoption();
			String ZipCodeOrig =BasePage.driver.getConfigData().get("ZipCodemailOrig");
			String ZipCodeDes =BasePage.driver.getConfigData().get("ZipmailToDes");
			String pounds="25";
			String ounces="0";
			String length="28";
			String height="16";
			String width="15";
			
			/*	
			 * Select the origin zip code 
			 */
				uspPage.sendkey(ZipCodeOrig);
				/*	
				 * Select the origin zip code 
				 */
				uspPage.sendkeyDes(ZipCodeDes);
				/*	
				 * Select the shipping Date and time 
				 */
				uspPage.sendkeyShipping();
				uspPage.sendkeyShippingtime();
				/*	
				 * Select package dimensions in inches or centimeters.Select the shipping Date and time 
				 */
				uspPage.clickbox();
				/*Enter the package weight in pounds or ounces.*/
				uspPage.pounds(pounds);
				uspPage.ounces(ounces);
				/*Enter the package option for  Ground retail.*/
				uspPage.clickbox();
				/*
				 * Enter the package dimensions in height,length and width
				 */
				uspPage.length(length);
				uspPage.height(height);
				uspPage.width(width);
				/*
				 * click continue to calculate the USPS shipping price
				 */
				uspPage.submit();
				uspPage.groundRetailImage();
				reportLog.add("Select All columns");
				/*
				 * Get the  calculated  USPS shipping price
				 */
				uspPage.groundRetailPrice();
				/*
				 * validate that there are images present on the final “Mail Services” page.
				 */
				uspPage.compareImageNames();
				/*
				 * If the price is higher than $80 - the test should fail. If the price is less than $80 - the test passes.
				 */
				
				uspPage.groundRetailPriceValidation();
				

			reportLog.add("UI Validation starts");

	
		
		} catch (Throwable e) {
			e.printStackTrace();
			for (String s : reportLog) {
				ReportingMaster.logger.createNode(s);
			}
			Assert.fail(e.getMessage());
		}
	}

	@AfterMethod()
	public void close() throws Exception {
		try {
			BasePage.driver.closeBrowser();
			System.out.println("after method");
			stopATURecorder();
		} catch (Exception e){}
	}
}

