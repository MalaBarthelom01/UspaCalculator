#   Selenium Maven FrameWork to USPS Calculator
## Objective
         Develop the "QAVectorAutomation" to write automation test cases for the USPS Retail Postage Calculator: [https://postcalc.usps.com/]
		 The frame is developed with JAVA-MAven and TESTNG
### Supporting Features:

        * Screenshot capture for failed tests
        * Video recording of test runs
	* Screenshot capture for failed tests
        * Data-driven testing using Excel to control test data
        * Cross-browser testing of suites/test cases
        * Testing on both Windows and Mac platforms
        * Page Object Model
        * emailable-reports
	* Windows and Mac(Platforms)
### Stack & Libraries
         * Java
         * Selenium WebDriver
         * TestNG
         * Maven
         * Extent Reports and logging
### Prerequisites
        * IDE (Eclipse)
        * JDK (version  8)
        * Maven configuration
### Easy way to configure Maven (Mac)
  1. Download Maven (tar.gz for mac[https://maven.apache.org/download.cgi]
  2. Move the downloaded apache-maven folder to your Home directory (This is the folder with your username)
  
     In terminal:
       1. Set system variables:
            export M2_HOME=/Users/<YOUR USER NAME>/<apache-maven->/

      2. Append the Maven bin folder to the path:
          export PATH=$PATH:/Users/<YOUR USER NAME>/<apache-maven-3>/bin/

      3. If you don't have a bash profile, create one:
            touch .bash_profile

      4. If you do have one, or after creating one, open the bash profile to edit:
           open .bash_profile

      5. Paste the two paths from step 1 and 2 .Save and close
         Now Maven will be installed for all sessions. To verify Maven is installed, type:
          mvn --version
	  
###  How to run tests
    In terminal: Clone the project from Git or unzip the given package.

     *  git clone https://github.com/MalaBarthelom01/UspaCalculator.git
         Change directory to the location of cloned project folder and run Maven clean
         mvn clean
     *   To run the test
	      You can run the automation suite using the command line or any IDE (I am using Eclipse). Follow the steps below:

     *   The test suites are located at the project level.
         Suite names:
     		UspsCalculator.xml (supports Chrome)
     		UspsCalculatorSafari.xml (supports Safari)
		
         To run the UspsCalculator.xml suite on Chrome, use the following command:
     		mvn test -DsuiteXmlFile=UspsCalculator.xml
         To run the UspsCalculatorSafari.xml suite on Safari, use the following command:
     		mvn test -DsuiteXmlFile=UspsCalculatorSafari.xml
		
##  Windows Environment: 
	Note: I did not Test it in windows PC. I dont have windows environment to test. But the code should work.
	mvn test -DsuiteXmlFile=UspsCalculatorIE.xml
	mvn test -DsuiteXmlFile=UspsCalculatorEdge.xml
	
			Attached is a screenshot for reference.
![email_Snapshot](https://user-images.githubusercontent.com/128941730/227891718-a4ef8d3a-0dba-4f57-beb4-819c78da3424.png)
	
[Image-screen-Reports.docx](https://github.com/MalaBarthelom01/UspaCalculator/files/11076739/Image-screen-Reports.docx)

![IMG_4459](https://user-images.githubusercontent.com/128941730/227895231-df76dcd6-5206-4ab5-95dd-85d543bd28d0.jpg)
![IMG_4460](https://user-images.githubusercontent.com/128941730/227895314-0d8b714f-5e72-48b7-abda-b0e2560f30d1.JPG)

	##	 Where to Find Reports:
		The reports are generated under the following directory as "emailable-report.html":
	
		- QAVectorAutomation/UspsAutomation/target/automation-reports/
		   - QAVectorAutomation/UspsAutomation/target/automation-reports/Automation_Report_03-26-2023.html
            Attached is a screenshot for reference.

     ![IMG_4459](https://user-images.githubusercontent.com/128941730/227892820-24889884-9edc-4dad-9a96-df042b3bd85b.jpg)


##  Step definitions for the USPS calculator workflow
	
	 ### The test cases to calculate the "USPS Calculator" involve the following steps:
         1. Select the country, origin zip code, and destination zip code.
         2. Select the shipping date and time.
         3. Select the package option for ground delivery.
         4. Select the package option to view dimensions.
	 5. Enter the package weight in pounds or ounces.
         6. Select the package option for ground retail.
         7. Enter the package dimensions in height, length, and width.
         8. Click "Continue" to calculate the USPS shipping price.
         9. Validate that there are images present on the final "Mail Services" page.
           If the price is higher than $80, the test should fail. If the price is less than $80, the test passes.
            There are three types of test cases created for Home Depot Moving boxes that can be used for shipping:
             *  Small box (16 in. L x 12 in. W x 12 in. D)
             *  Medium box (22 in. L x 16 in. W x 15 in. D)
             * Large box (28 in. L x 15 in. W x 16 in. D)


 
