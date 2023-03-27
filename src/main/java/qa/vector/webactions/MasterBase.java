package qa.vector.webactions;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import qa.vector.ui.pages.BasePage;
import qa.vector.ui.pages.BrowserType;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@SuppressWarnings("ALL")
public class MasterBase {
    public static String testName = null;
    private String testCaseName = null;
    public HashMap<String, HashMap<String, String>> data = null;
    BasePage page = null;
    Reporter reporter = null;
    String currentDir = System.getProperty("user.dir");
    ATUTestRecorder recorder = null;
    static File videoStorageDirectory = null;
    public static HashMap<String, String> config;
  
    public static BrowserType browser;
    public static String executionEnvironment;
    public List<String> reportLog;
    public long startTime;
    private String  ZipCodeMailOrig;
    private String  ZipCodeMailToDes;
    private String  Pounds;
    private String Ounces;
    private String Length;
    private String Height;
    private String Width;
    private String dataExcelFile;
    /**
     * Method to start video recording of the test case which is in execution.
     *
     * @throws Exception
     */
    
    public MasterBase(String datafile,int rownum) throws Exception {
		this();
		XSSFWorkbook wb;
		XSSFSheet sheet;
		XSSFRow row;
		XSSFCell cell;
		FileInputStream fis;
		dataExcelFile=datafile;
		fis =new FileInputStream(dataExcelFile);

		//wb=new XSSFWorkbook(fis);
		//String sheetname = "";
		wb=new XSSFWorkbook(fis);
		sheet=wb.getSheet("content");
		
		try {
			row=sheet.getRow(rownum);
			
			String zipCodeMailOrig=row.getCell(6).getStringCellValue();
			setZipCodeMailOrig(zipCodeMailOrig);
			ZipCodeMailToDes=row.getCell(7).getStringCellValue();
			setZipCodeMailToDes(ZipCodeMailToDes);
			Pounds=row.getCell(8).getStringCellValue();
			
			Ounces=row.getCell(9).getStringCellValue();
			
			Length=row.getCell(10).getStringCellValue();
	
			Width=row.getCell(11).getStringCellValue();
			
			fis.close();


		}

		catch (Exception e1) {
			e1.getStackTrace();
		}

	}
    public boolean startATURecorder() throws Exception {
        HashMap<String, String> configData = BasePage.driver.getConfigData();
        String recordVideo = configData.get("recordVideo");
        String videoStorageLocation;

        //Created directory with name having current date and time.
        if (recordVideo.equalsIgnoreCase("TRUE")) {
            if (videoStorageDirectory == null) {
                videoStorageLocation = System.getProperty("user.dir");
                File storageLocation = new File(videoStorageLocation);
                checkIfDirectoryExists(storageLocation);
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
                String time = dateFormat.format(now);
                videoStorageDirectory = new File(videoStorageLocation + "/" + time);
                videoStorageDirectory.mkdir();
            }

            try {

                recorder = new ATUTestRecorder(videoStorageDirectory.getAbsolutePath(), getTestCaseName(), false);
                recorder.start();
                System.out.println("Started video recording of test :" + getTestCaseName() + " at location : " + videoStorageDirectory.getAbsolutePath());

            } catch (ATUTestRecorderException atuTestRecorderException) {
                System.out.println("Failed to start recording of video :" + getTestCaseName() + " with exception :" + atuTestRecorderException.getMessage());
            }
            return true;
        }
        return false;
    }

    /**
     * Method to check whether directory is present at passed location.
     *
     * @param dirLocation
     * @throws Exception
     */
    public void checkIfDirectoryExists(File dirLocation) throws Exception {

        if (!dirLocation.exists() || (dirLocation.exists() && !dirLocation.isDirectory())) {
            throw new Exception("Storage location not found at Location :" + dirLocation);
        }
    }

    /**
     * Method to stop the video recording of test case which is in execution.
     *
     * @throws Exception
     */
    public void stopATURecorder() throws Exception {
        //Stop recording only if recording is started.
        if (BasePage.driver.getConfigData().get("recordVideo").equalsIgnoreCase("TRUE") && recorder != null) {
            try {
                Thread.sleep(2000);
                recorder.stop();
                System.out.println("Stopped video recording of test :" + getTestCaseName());

            } catch (ATUTestRecorderException atuTestRecorderException) {
                System.out.println("Failed to stop recording of video :" + getTestCaseName() + " with exception :" + atuTestRecorderException.getMessage());
            }
        }
    }

    /**
     * Intiate browser
     *
     * @param br
     * @throws Exception
     */

    public void initBrowser(BrowserType br) throws Exception {
        //this();
        reporter = new Reporter();

        config = BasePage.driver.getConfigData();
        browser = br;
    }

    public MasterBase() throws Exception {
        page = new BasePage();
        executionEnvironment = BasePage.driver.config.get("ExecutionEnvironment");
    }

    /**
     * Delete files
     *
     * @param file
     */


    // Getter for test caseName
    public String getTestCaseName() {
        testCaseName = this.getClass().getSimpleName();
        return testCaseName;
    }

   

    protected void log(String message) {
        Reporter.log("<br>" + message);
    }


  
    public String getZipCodeMailOrig() {
		return ZipCodeMailOrig;
	}
	
	public String getZipCodeMailToDes() {
		return ZipCodeMailToDes;
	}
	public String getPounds() {
		return Pounds;
	}
	public String getOunces() {
		return Ounces;
	}
public String Length() {
		return Length;
	}
public String getHeight() {
		return Height;
	}
public String getWidth() {
		return Width;
	}

public void setdataExcelFile(String dataexcelfile) {
	dataExcelFile=dataexcelfile;
}
public void setZipCodeMailOrig(String zipCodeOrig) {
	ZipCodeMailOrig=zipCodeOrig;
}

public void setZipCodeMailToDes(String zipCodeDes) {
	ZipCodeMailToDes=zipCodeDes;
}
public void setPounds(String pound) {
	Pounds=pound;
}
public void setOunces(String ounce) {
	Ounces=ounce;
}
public void setLength(String len) {
Length=len;
}
public void setHeight(String height) {
	Height=height;
}
public void setWidth(String width) {
	Width=width;
}


   


}
