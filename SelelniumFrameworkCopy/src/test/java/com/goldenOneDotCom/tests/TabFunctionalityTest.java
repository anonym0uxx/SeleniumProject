package com.goldenOneDotCom.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.utilities.DriverUtilsTest;
import com.utilities.ExtentReportManagerTest;
import com.utilities.TestConstantsTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.time.Duration;

public class TabFunctionalityTest {

    static ExtentTest Golden1TabClickTest;
    private WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser,ITestContext context) {
        //When starting the execution what we need to set is the suiteName
        TestConstantsTest.SUITE_NAME = context.getSuite().getName();  
        ExtentReports extentReport = ExtentReportManagerTest.getInstance(TestConstantsTest.SUITE_NAME);
        Golden1TabClickTest = extentReport.createTest("Golden1.com Tab Click Test", "Clicks each tab and validates they are clickable");

        driver = DriverUtilsTest.getDriver(browser);
        Golden1TabClickTest.log(Status.INFO, "Browser started and maximized: " + browser);

        System.out.println("This is the Test Suite Name: "+TestConstantsTest.SUITE_NAME);
    }

    @Test
    public void testClickingEachTab() {
        try {
            if (driver != null && driver.getWindowHandles().size() > 0) {
                driver.get(TestConstantsTest.G1_HOME_URL);
                Golden1TabClickTest.pass("Navigated to " + TestConstantsTest.G1_HOME_URL);
                DriverUtilsTest.takeScreenshot(driver, "TabFunctionality_HomePage");

                String actualTitle = driver.getTitle();
                Assert.assertEquals(actualTitle, TestConstantsTest.EXPECTED_TITLE, "Homepage title mismatch!");
                Golden1TabClickTest.pass("Homepage title validated successfully");

                String[] tabs = {"Checking", "Savings", "Home Loans", "Credit Cards", "Loans", "Investing", "Community"};
                for (String tabName : tabs) {
                    validateTabIsClickable(tabName);
                }
            } else {
                Golden1TabClickTest.fail("Browser window is already closed or session was lost.");
            }
        } catch (Exception e) {
            Golden1TabClickTest.fail("Test failed due to exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void validateTabIsClickable(String tabName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(tabName)));
            tabElement.click();
            Golden1TabClickTest.pass("Clicked on '" + tabName + "' tab");
            DriverUtilsTest.takeScreenshot(driver, "TabFunctionality_" + tabName);

        } catch (Exception e) {
            Golden1TabClickTest.fail("Failed to click '" + tabName + "' tab: " + e.getMessage());
            Assert.fail("Exception occurred while clicking '" + tabName + "' tab: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        ExtentReportManagerTest.flushReport();
        Golden1TabClickTest.info("Test completed");
    }
}
