package com.goldenOneDotCom.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class SearchIndexFeatureTest {

    static ExtentTest Golden1SearchTest;
    private WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser,ITestContext context) {
        //When starting the execution what we need to set is the suiteName
        TestConstantsTest.SUITE_NAME = context.getSuite().getName();  
        ExtentReports extentReport = ExtentReportManagerTest.getInstance(TestConstantsTest.SUITE_NAME);
        Golden1SearchTest = extentReport.createTest("Golden1.com Search Test", "Tests the search functionality on Golden1.com");

        driver = DriverUtilsTest.getDriver(browser);
        Golden1SearchTest.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testGolden1SearchFunctionality() {
        try {
            if (driver != null && driver.getWindowHandles().size() > 0) {
                driver.get(TestConstantsTest.G1_HOME_URL);
                Golden1SearchTest.pass("Navigated to " + TestConstantsTest.G1_HOME_URL);
                DriverUtilsTest.takeScreenshot(driver, "SearchFunctionality_HomePage");

                String actualTitle = driver.getTitle();
                Assert.assertEquals(actualTitle, TestConstantsTest.EXPECTED_TITLE, "Homepage title mismatch!");
                Golden1SearchTest.pass("Homepage title validated successfully");

                // Locate the search box using the correct ID and perform search
                WebElement searchBox = driver.findElement(By.id("site-search"));
                searchBox.sendKeys("Checking");
                searchBox.sendKeys(Keys.RETURN);
                Golden1SearchTest.pass("Entered 'Checking' into the search box and pressed Enter");
                DriverUtilsTest.takeScreenshot(driver, "SearchFunctionality_AfterSearch");
            } else {
                Golden1SearchTest.fail("Browser window is already closed or session was lost.");
            }
        } catch (Exception e) {
            Golden1SearchTest.fail("Test failed due to exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        ExtentReportManagerTest.flushReport();
        Golden1SearchTest.info("Test completed");
    }
}
