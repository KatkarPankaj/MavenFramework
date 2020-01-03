package util;
/**
 * The TestListener class is used to help in generating html Report
 *
 * @author Pankaj Katkar
 * @version 1.0
 * 
 */
import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import model.MethodParameters;
import util.WebDriverClass;

/**
 * @author Pankaj Katkar
 *
 */
public class TestListener implements ITestListener{
	static String DateToday = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
	static String projectPath = System.getProperty("user.dir");

	static String screenshotPath = projectPath +"\\Test_Report\\";

	public void onTestStart(ITestResult result) {

	}

	public void onTestSuccess(ITestResult tr) {
		try {
			
			 captureScreenShot(tr,"pass");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onTestFailure(ITestResult tr) {
		try {
			captureScreenShot(tr, "fail");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}

	public static String captureScreenShot(ITestResult result, String status) throws Exception{	
		String destDir = "";
		String passfailMethod = result.getName();
	
		passfailMethod = passfailMethod;
	
		File scrFile = ((TakesScreenshot) WebDriverClass.driver).getScreenshotAs(OutputType.FILE);
    	String destFile = passfailMethod + ".png";
    	
    	
    	if(status.equalsIgnoreCase("fail")){
    		
    	 destDir = screenshotPath+"\\fail\\";
       	 FileUtils.copyFile(scrFile, new File(destDir+passfailMethod+".png"));
System.out.println("******ScreenShot is captured to***"+destDir);    	}

    	else if (status.equalsIgnoreCase("pass")){
    		destDir = screenshotPath+"\\pass\\";
        	 FileUtils.copyFile(scrFile, new File(destDir+passfailMethod+".png"));
        	 System.out.println("******ScreenShot is captured to***"+destDir);  
    	}
    	
    	
    
		return destFile;
   } 
	

}