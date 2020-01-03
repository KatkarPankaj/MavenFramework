package util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import actions.MethodType;
import model.MethodParameters;




public class extendReport implements IReporter{
	static String projectPath = System.getProperty("user.dir");
    private static final String OUTPUT_FOLDER = projectPath+"\\Test_Report\\";
    private static final String FILE_NAME = "Eextent_Test_Report.html";
    
    private ExtentReports extent;
    String methodName = null;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        init();
        
        for (ISuite suite : suites) {
        	Map<String, ISuiteResult> result= suite.getResults();
            
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                
                try {
					buildTestNodes(context.getFailedTests(), Status.FAIL);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                try {
					buildTestNodes(context.getSkippedTests(), Status.SKIP);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                try {
					buildTestNodes(context.getPassedTests(), Status.PASS);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }
        }
        
        for (String s : Reporter.getOutput()) {
            extent.setTestRunnerOutput(s);
        }
  
        extent.flush();
    }
    
    private void init() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME);
        htmlReporter.config().setDocumentTitle("Extent Report Suite");
        htmlReporter.config().setReportName("Extent Report Name");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        htmlReporter.config().setTheme(Theme.STANDARD);
        
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
    }
    
    private void buildTestNodes(IResultMap tests, Status status) throws Exception {
        ExtentTest test;
     
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extent.createTest(result.getName());
                System.out.println("Get Method Name------"+result.getMethod().getMethodName());
                System.out.println("Get Name------"+result.getName());
                System.out.println("Get TestName------"+result.getTestName());
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);

                if (result.getStatus() == ITestResult.FAILURE) {
                	test.log(status,
							result.getName()+ " got " + status.toString().toLowerCase() + "ed");
					test.log(status, result.getThrowable().getMessage());
					test.log(status, result.getThrowable().getStackTrace().toString());
					test.log(status, result.getParameters().toString());

					String screenshotPath = TestListener.captureScreenShot(result, result.getName());
					
					MediaEntityModelProvider mediaModel = MediaEntityBuilder
							.createScreenCaptureFromPath("../Test_Report/fail/" + screenshotPath).build();

					test.fail("Screenshot", mediaModel);
					

				} else if (result.isSuccess()) {
					test.log(status,
							result.getName() + " got " + status.toString().toLowerCase() + "ed");
					test.log(status, result.getTestName());
					//test.log(status, result.getThrowable().getMessage());
					test.log(status, result.getName());
					MethodType obj = new MethodType();
					test.log(status, TestData);
				
			
	String screenshotPath = TestListener.captureScreenShot(result, result.getName());
					
					MediaEntityModelProvider mediaModel = MediaEntityBuilder
							.createScreenCaptureFromPath("../Test_Report/pass/" + screenshotPath).build();

					test.pass("Screenshot", mediaModel);
					
				}
                
                test.getModel().setStartTime(getTime(result.getStartMillis()));
                test.getModel().setEndTime(getTime(result.getEndMillis()));
              
            }
        }
    }
    
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();      
    }


}


