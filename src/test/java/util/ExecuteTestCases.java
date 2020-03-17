package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.internal.BaseTestMethod;

import actions.SendMailSSLWithAttachment;


public class ExecuteTestCases implements ITest {
	static String ProjectPath =System.getProperty("user.dir");
	String cbrowserName = "BROWSER_NAME";
	String curl = "URL";
	String cdriver = "IEDRIVER";
	ReadConfigProperty config = new ReadConfigProperty();
	ReadElementLocators read = new ReadElementLocators();
	static ExcelLibrary lib = new ExcelLibrary();

	static WebDriver driver = null;
	static ExcelAction act = new ExcelAction();
	String browserName = config.getConfigValues(cbrowserName);

	static List list;
	protected String mTestCaseName = "";
	/**
	 * In this class, this is the first method to be executed.
	 * Reading testsuite , test case sheet and data sheet and storing the values in Hashmap
	 **/
	@Parameters("browser01")
	@BeforeClass
	public void setup(String browser01) {
		System.out.println(ExecuteTestCases.class.getName()
				+ "   setup() method called");


		act.readTestSuite();
		act.readTestCaseInExcel();
		act.readTestDataSheet();
		act.readCapturedObjectProperties();
		

		/**
		 * Selecting which browser to be executed
		 **/
		//String browserName = config.getConfigValues(cbrowserName);
		//selectBrowser(browserName);

		//System.out.println(config.getConfigValues(curl));
		/**
		 * launching the url
		 **/
		selectBrowser(browser01);
		
		driver.get(config.getConfigValues(curl));
		WebDriverClass.setDriver(driver);
	}
	
	/**
	 *Select the browser on which you want to execute tests 
	 * @return 
	 **/
	
	public  void selectBrowser(String browser01) {

		if (browser01.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", ProjectPath+"\\chromedriver.exe");
			 driver = new ChromeDriver();
			 driver.manage().window().maximize();
				
		
			
		}
		if (browser01.equalsIgnoreCase("Firefox")) {
			System.setProperty("webdriver.gecko.driver", ProjectPath+"\\geckodriver.exe");
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			
			
		}
		if (browser01.equalsIgnoreCase("IE")) {
			System.setProperty("webdriver.ie.driver", ProjectPath+"\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}
	
		

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
	 */
	@Test(dataProvider = "dp")
	public void testSample1(String testName) {
		System.out.println(ExecuteTestCases.class.getName()
				+ "  @Test method called" + "    " + testName);

		try {
			this.setTestName(testName);
			System.out.println("testSuiteIterate() calling");
			act.testSuiteIterate(testName);
			Reporter.log("ex" + getTestName());

		} catch (Exception ex) {
			Assert.fail();
			stack(ex);

			Reporter.log("exception in execute testCase====" + ex);

		}
	}

	@AfterMethod
	public void setResultTestName(ITestResult result) {
		try {
			BaseTestMethod bm = (BaseTestMethod) result.getMethod();
			Field f = bm.getClass().getSuperclass()
					.getDeclaredField("m_methodName");
			String browserI = result.getTestContext().getCurrentXmlTest().getParameter("browser01");
			f.setAccessible(true);
			f.set(bm, mTestCaseName+" "+"Browser:"+browserI);
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
		List newList = (ArrayList) list.stream() 
                .distinct() 
                .collect(Collectors.toList());
		Object[][] data = new Object[newList.size()][1];
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