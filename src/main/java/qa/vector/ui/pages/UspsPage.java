package qa.vector.ui.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import qa.vector.ui.pages.BasePage;
//import qa.ml.ui.pages.LoginPage.usernames;
import qa.vector.ui.pages.ObjectRepositoryLoader;
import qa.vector.webactions.Driver;

//public class UspsPage {

//}
public class UspsPage extends BasePage {
    private HashMap<String, HashMap<String, String>> uspsPage = null;


    public UspsPage() throws Exception {
    	uspsPage = new ObjectRepositoryLoader().getObjectRepository("web:UspsPage.xml");
    }

   
    /**
     * method replace String for dynamic xpath
     *
     * @param IdentifierName - Identifier name in element repository
     * @param replacement    - replacement value for provided pattern
     * @param pattern-       Pattern to be replaced
     */
    public void replaceString(String IdentifierName, String replacement, String pattern) {
        String xpath = uspsPage.get(IdentifierName).get("XPATH");
        xpath = xpath.replaceAll(pattern, replacement);
        uspsPage.get(IdentifierName).put("XPATH", xpath);

    }

    
    public boolean clickTitle(String title){
        boolean status = false;
        replaceString("Title", title, "#replaceString#");
        driver.click(uspsPage.get("Title"));
        status =true;
        return status;
    }

   
    public boolean selectoption() {
   
        try {
           
        	WebElement ele=	driver.find(uspsPage.get("RetailPage"));
        	List<WebElement> selectedOptions = new Select(ele).getAllSelectedOptions();
            StringBuilder sb = new StringBuilder();
            for (WebElement selectedOption : selectedOptions) {
             sb.append(selectedOption.getText());
            }
            System.out.println(sb.toString());
            
        } catch (Exception e) {
            return true;
        }
        return false;
    }
    
    public void waitForCatalogPageToLoad(String a) {
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 11);
        wait.until(ExpectedConditions.visibilityOf(driver.find(uspsPage.get(a))));
    }

    /**
     * Login function for user
     *
     * @throws Exception
     */
    public void sendkey(String a) throws Exception {
    	waitForCatalogPageToLoad("ZipCodemailOrig");
    	driver.find(uspsPage.get("ZipCodemailOrig")).click();
        driver.find(uspsPage.get("ZipCodemailOrig")).sendKeys(a);
     
    }
    public void sendkeyDes(String a) throws Exception {
    	waitForCatalogPageToLoad("ZipmailToDes");
    	driver.setText(uspsPage.get("ZipmailToDes"), a);
      
       
      }
    public void pounds(String a) throws Exception {
    	waitForCatalogPageToLoad("Pounds");
    	driver.setText(uspsPage.get("Pounds"), a);
       
      }
    public void ounces(String a) throws Exception {
    	waitForCatalogPageToLoad("Ounces");
    	driver.setText(uspsPage.get("Ounces"), a);
       
      }
    public void length(String a) throws Exception {
    	waitForCatalogPageToLoad("Length");
    	driver.setText(uspsPage.get("Length"), a);
       
      }
    public void height(String a) throws Exception {
    	waitForCatalogPageToLoad("Height");
    	driver.setText(uspsPage.get("Height"), a);
    
      }
    public void width(String a) throws Exception {
    	waitForCatalogPageToLoad("Width");
    	driver.setText(uspsPage.get("Width"), a);
        
      }
    public void sendkeyShipping() throws Exception {
    	waitForCatalogPageToLoad("ShipDate");
        driver.find(uspsPage.get("ShipDate")).click();
        //driver.find(uspsPage.get("ShipDate")).sendKeys(a);
       
      }
    public void sendkeyShippingtime() throws Exception {
    	waitForCatalogPageToLoad("ShipTime");
        driver.find(uspsPage.get("ShipTime")).click();
        //driver.find(uspsPage.get("ShipDate")).sendKeys(a);
       
      }
    public void submit() throws InterruptedException{
         driver.click(uspsPage.get("Submit"));
         driver.SwichToNewWindowAndReturnParentWindowHandle();
        
         waitForCatalogPageToLoad("Mail");
         System.out.println(driver.getWebDriver().getTitle());
       
        
    }
    public void clickbox() throws InterruptedException{
        // boolean status = false;
         //replaceString("Title", title, "#replaceString#");
         driver.click(uspsPage.get("SelectBox4"));
         driver.SwichToNewWindowAndReturnParentWindowHandle(); 
    }
  

    public boolean groundRetailImage() throws InterruptedException{
        // boolean status = false;
         //replaceString("Title", title, "#replaceString#");
    	driver.click(uspsPage.get("USPSRetailGround"));
      String src=  driver.getAttributeSrc(uspsPage.get("USPSRetailGround"));
         return src.endsWith("USPSRetailGround.jpg");
    	
             
    }
    public String groundRetailPrice() throws InterruptedException{
        // boolean status = false;
         //replaceString("Title", title, "#replaceString#");
    	driver.click(uspsPage.get("USPSRetailGroundPrice"));
     return driver.getText(uspsPage.get("USPSRetailGroundPrice"));
           
    }
    
   
    public  void uspsImages() throws InterruptedException{
        int counter=0;
        
        
    	List<WebElement>  allImg= driver.findAll(uspsPage.get("USpsImages"));
    String img="";
  
    for(WebElement image:allImg)
    {
    if(image.isDisplayed())
    {
    counter++;
    String[] names = image.getAttribute("src").split("/");
    String name = names[names.length-1];
    System.out.println(name);
  
    
    }
    }
   // System.out.println(" ArrayList1 = " + ArrayList1);
    System.out.println("No. of total displable images: "+counter);
    }
    public void compareImageNames() throws InterruptedException {
        List<String> expectedImageNames = Arrays.asList("logo-sb.png", "VideoLibraryIcon_Small.png", "PriorityMailExpress.jpg", "PriorityMail.jpg", "USPSRetailGround.jpg", "MediaMail.jpg", "logo-usps-footer.png");

        int counter = 0;
        List<WebElement> allImg = driver.findAll(uspsPage.get("USpsImages"));

        for (WebElement image : allImg) {
            if (image.isDisplayed()) {
                counter++;
                String[] names = image.getAttribute("src").split("/");
                String name = names[names.length - 1];

                if (expectedImageNames.contains(name)) {
                    System.out.println(name + " is present in the list.");
                    Assert.assertTrue(expectedImageNames.contains(name), name + " is not present in the expected image names list.");
                } else {
                    System.out.println(name + " is not present in the list.");
                    Assert.fail(name + " is not present in the expected image names list.");
                }
            }
        }
    }
   
    public void groundRetailPriceValidation() throws InterruptedException{
        
    	String price = groundRetailPrice();
        float rePrice = Float.parseFloat(price.substring(1));
        System.out.println(rePrice);
        Assert.assertTrue(rePrice < 80.00, "Price is higher than $80");
    
    }
    /*public void groundRetailPriceValidation() throws InterruptedException{
        String price = groundRetailPrice();
        float rePrice = Float.parseFloat(price.substring(1));
        System.out.println(rePrice);
        Assert.assertGreaterThan(rePrice, 80.00, "Price is higher than or equal to $80");
    }*/
}
