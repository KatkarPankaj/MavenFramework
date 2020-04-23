# Sample maven framework- with excel and Extent reports implemented
Sample maven framework- with excel 
Steps to get started: Example file and tests are already provided in the project.

Important files:
1. Config.properties
A. Tester needs to change the application URL
B. There are Test suite name and Test Case Path already defined for excel files.

Excel file needs to be modified.
//This file will have all test case name and tester can choose which tests to be executed.
*Test Suite2.xlsx* -> Define your test case name and execution status - based on this file script will execute the tests. 
If Execution status is "Yes" then only the test case will be executed.

// This excel file will have all test cases and test steps to be defined by tester based on the application.
*Test Cases2.xlsx* 
Define- WebElement in "CaptureObjectProperties" sheet as following.
ID, LINKTEXT, XPATH, CSS etc.

Define test steps in "TestCaseSheet" sheet. following are the action need to write in ActionType column.
click, selectDropDownByValue, enterText, verifyURL, navigateToURL, clear, isDisplayed 






