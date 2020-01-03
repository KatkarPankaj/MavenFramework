package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.BaseTestMethod;

import actions.SendMailSSLWithAttachment;


public class ExecuteTestCases implements ITest {

	public static final String FIREFOX = "firefox";
	public static final String IE = "IE";
	public static final String CHROME = "chrome";
	String cbrowserName = "BROWSER_NAME";
	String curl = "URL";
	String cdriver = "IEDRIVER";
	ReadConfigProperty config = new ReadConfigProperty();
	ReadElementLocators read = new ReadElementLocators();
	static ExcelLibrary lib = new ExcelLibrary();

	static WebDriver driver = null;
	static ExcelAction act = new ExcelAction();

	static List list;
	protected String mTestCaseName = "";
	/**
	 * In this class, this is the first method to be executed.
	 * Reading testsuite , test case sheet and data sheet and storing the values in Hashmap
	 **/
	@BeforeClass
	public void setup() {
		System.out.println(ExecuteTestCases.class.getName()
				+ "   setup() method called");


		act.readTestSuite();
		act.readTestCaseInExcel();
		act.readTestDataSheet();
		act.readCapturedObjectProperties();

		/**
		 * Selecting which browser to be executed
		 **/
		String browserName = config.getConfigValues(cbrowserName);
		selectBrowser(browserName);

		System.out.println(config.getConfigValues(curl));
		/**
		 * launching the url
		 **/
		driver.get(config.getConfigValues(curl));
		WebDriverClass.setDriver(driver);
	}
	
	/**
	 *Select the browser on which you want to execute tests 
	 **/
	private void selectBrowser(String browserName) {

		if (browserName.equalsIgnoreCase("Chrome")) {
			chrome();
			
		}
		if (browserName.equalsIgnoreCase("Firefox")) {
			firefoxProfile();
			
		}
		if (browserName.equalsIgnoreCase("IE")) {
			ie();
			
		}
		

	}

	private void chrome() {

		System.setProperty("webdriver.chrome.driver",
				config.getConfigValues(cdriver));

		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	private void ie() {
		System.setProperty("webdriver.ie.driver",
				config.getConfigValues(cdriver));

		driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		System.out.println("IE launch");

	}

	/**
	 * Firefox profile will help in automatic download of files
	 */
	private void firefoxProfile() {
		
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 1);
		profile.setPreference("browser.download.manager.showWhenStarting",
				false);

		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document");

		driver = new FirefoxDriver(profile);
		driver.manage().window().maximize();

	}

	/**
	 * To override the test case name in the report
	 */
	@BeforeMethod(alwaysRun = true)
	public void testData(Object[] testData) {
		String testCase = "";
		if (testData != null && testData.length > 0) {
			String testName = null;
			// Check if test method has actually received required parameters
			for (Object tstname : testData) {
				if (tstname instanceof String) {
					testName = (String) tstname;
					break;
				}
			}
			testCase = testName;
		}
		this.mTestCaseName = testCase;

	}

	public String getTestName() {
		return this.mTestCaseName;
	}

	public void setTestName(String name) {

		this.mTestCaseName = name;

	}

	/**
	 * All the test cases execution will start from here, which will take the input from the data provider
	 * @throws Exception 
	 */
	@Test(dataProvider = "dp")
	public void testSample1(String testName) throws Exception {
		System.out.println(ExecuteTestCases.class.getName()
				+ "  @Test method called" + "    " + testName);

			try{
				this.setTestName(testName);
			
			System.out.println("testSuiteIterate() calling");
			act.testSuiteIterate(testName);
			Reporter.log("ex" + getTestName());
			

		} catch (Exception ex) {
			
			System.out.println("This is exception"+ex);
			System.out.println("This is message"+ex.getMessage());
			System.out.println("This is StackTrace"+ex.getStackTrace());
			System.out.println("This is GetCause"+ex.getCause());

			Reporter.log("exception in execute testCase====" + ex);
			//Assert.fail();
		//	As
		 Assert.fail("TestCase Is Failed Because of:--- "+ex);
		
		}
	}
	

	@AfterMethod
	public void setResultTestName(ITestResult result) {
		try {
			BaseTestMethod bm = (BaseTestMethod) result.getMethod();
			Field f = bm.getClass().getSuperclass()
					.getDeclaredField("m_methodName");
			f.setAccessible(true);
			f.set(bm, mTestCaseName);
			Reporter.log(bm.getMethodName());
			this.mTestCaseName = " ";

		} catch (Exception ex) {
			stack(ex);
			Reporter.log("ex" + ex.getMessage());
		}
	}

	/**
	 * Parameterization in testng
	 */
	@DataProvider(name = "dp")
	public Object[][] regression() {
		List list = (ArrayList) ExcelAction.listOfTestCases;
		Object[][] data = new Object[list.size()][1];
		System.out.println(ExecuteTestCases.class.getName()
				+ " TestCases to be executed" + "    " + data);
		for (int i = 0; i < data.length; i++) {
			data[i][0] = (String) list.get(i);
		}
		return data;
	}

	@AfterClass
	public void cleanup() {
		act.clean();
		lib.clean();

	}

	public static void stack(Exception e) {
		e.printStackTrace();
		System.out.println(ExecuteTestCases.class.getName()
				+ " Exception occured" + "    " + e.getCause());
	}
}
