package com.goldenOneDotCom.tests.Loans;

import java.time.Duration;

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

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.utilities.DriverUtilsTest;
import com.utilities.ExtentReportManagerTest;
import com.utilities.TestConstantsTest;

public class SeeAllLoanRatesTest {

    private WebDriver driver;
    private ExtentTest test;
    private final String QA_URL = "https://qa-new.golden1.com/";
    private final String EXPECTED_RATES_URL = "https://qa-new.golden1.com/manage-accounts/all-rates";

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser, ITestContext context) {
        // Capture the suite name
        TestConstantsTest.SUITE_NAME = context.getSuite().getName();
        ExtentReports extentReport = ExtentReportManagerTest.getInstance(TestConstantsTest.SUITE_NAME);
        test = extentReport.createTest("Smoke Tests - Verify See All Loan Rates Under Tools and Resources for Loans Tab",
                "Validates that clicking 'See All Loan Rates' under Tools and Resources in the Loans tab navigates to the correct rates page");
        driver = DriverUtilsTest.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
        System.out.println("Starting the See All Loan Rates test setup");
    }

    @Test
    public void testSeeAllLoanRatesNavigation() {
        try {
            // Step 1: Navigate to the QA URL
            driver.get(QA_URL);
            Thread.sleep(1000);
            test.pass("Navigated to QA URL: " + QA_URL);
            takeScreenshot("Navigated_To_QA_HomePage");

            // Step 2: Click the Loans tab
            clickLoansTab();

            // Step 3: Click on "See All Loan Rates" under Tools and Resources
            clickSeeAllLoanRates();

            // Step 4: Verify navigation to the correct rates page
            verifyRatesPageNavigation();

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clickLoansTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            
            // Wait for the Loans tab to be clickable
            WebElement loansTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Loans') and @role='button']")));
            
            loansTab.click();
            test.pass("Clicked on the Loans tab");
            takeScreenshot("Clicked_Loans_Tab");
            
            // Wait for the sub-navigation to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='sub-nav open']")));
            
            Thread.sleep(1000); // Allow time for dropdown to fully load
            
        } catch (Exception e) {
            test.fail("Failed to click Loans tab: " + e.getMessage());
            takeScreenshot("Loans_Tab_Click_Failed");
            Assert.fail("Exception occurred while clicking Loans tab: " + e.getMessage());
        }
    }

    private void clickSeeAllLoanRates() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            
            // Look for "See All Loan Rates" link in Tools and Resources section
            WebElement seeAllLoanRatesLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'See All Loan Rates')]")));
            
            // Scroll into view to ensure visibility
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", seeAllLoanRatesLink);
            Thread.sleep(500);
            
            seeAllLoanRatesLink.click();
            test.pass("Clicked on 'See All Loan Rates' link");
            takeScreenshot("Clicked_See_All_Loan_Rates");
            
        } catch (Exception e) {
            test.fail("Failed to click 'See All Loan Rates' link: " + e.getMessage());
            takeScreenshot("See_All_Loan_Rates_Click_Failed");
            Assert.fail("Exception occurred while clicking 'See All Loan Rates': " + e.getMessage());
        }
    }

    private void verifyRatesPageNavigation() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            
            // Wait for the page to load completely
            wait.until(ExpectedConditions.urlContains("all-rates"));
            
            // Verify the current URL matches expected rates page URL
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("all-rates"), 
                "Expected URL to contain 'all-rates' but found: " + currentUrl);
            
            // Verify page elements that indicate we're on the rates page
            WebElement ratesPageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(),'Rates')] | //title[contains(text(),'Rates')] | //div[contains(@class,'rates')]")));
            
            Assert.assertTrue(ratesPageElement.isDisplayed(), 
                "Rates page element is not displayed");
            
            test.pass("Successfully navigated to the All Rates page");
            test.pass("Current URL: " + currentUrl);
            takeScreenshot("All_Rates_Page_Loaded");
            
        } catch (Exception e) {
            test.fail("Failed to verify navigation to rates page: " + e.getMessage());
            takeScreenshot("Rates_Page_Verification_Failed");
            Assert.fail("Exception occurred while verifying rates page navigation: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            test.pass("Browser closed successfully");
        }
        ExtentReportManagerTest.flushReport();
    }

    private void takeScreenshot(String screenshotName) {
        if (TestConstantsTest.ENABLE_SCREENSHOTS) {
            try {
                DriverUtilsTest.takeScreenshot(driver, screenshotName);
            } catch (Exception e) {
                test.warning("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
}