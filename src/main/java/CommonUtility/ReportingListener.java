package CommonUtility;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ReportingListener implements ITestListener {


    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("********Begin with new Test : " + result.getName() + "********");
        String description = "";
        try {
            description = result.getMethod().getDescription();
        } catch (Exception e) {
            description = "Description missing in test.";
        }
        ReportingMaster.createTest(result.getName(), description, result.getTestContext().getCurrentXmlTest().getName());

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("******** Test Successfull: " + result.getName() + "********");
        try {
            ReportingMaster.getResult(result);
        } catch (Exception e) {
            System.out.println("Error writing test status for" + result.getTestName());
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("******** Test Failed : " + result.getName() + "********");
        try {
            ReportingMaster.getResult(result);
        } catch (Exception e) {
            System.out.println("Error writing test status for" + result.getTestName());
            e.printStackTrace();
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("******** Test Skipped: " + result.getName() + "********");
        try {
            ReportingMaster.getResult(result);
        } catch (Exception e) {
            System.out.println("Error writing test status for" + result.getTestName());
            e.printStackTrace();
        }

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("********Intialized Reporting : On Start********");
        try {
            ReportingMaster.initReport(context.getCurrentXmlTest().getName());
        }catch(Exception e){
            System.out.println("Exception in Report master");
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("********Writing Final Report file : On Finish********");
        try {
            ReportingMaster.endReport();
            ReportingMaster.killAllChromeInstances();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
