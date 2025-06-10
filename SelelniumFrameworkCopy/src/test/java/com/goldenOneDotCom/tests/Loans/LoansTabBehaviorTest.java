package com.goldenOneDotCom.tests.Loans;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.utilities.DriverUtils;
import com.utilities.ExtentReportManager;
import com.utilities.TestConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.time.Duration;

public class LoansTabBehaviorTest {

    private WebDriver driver;
    private ExtentTest test;

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Golden1.com Loans Tab Behavior Test", 
            "Test Case 1: Verify the behavior of an application user able to view Loans tab");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testLoansTabBehavior() {
        try {
            // Step 1: Open new browser and enter the QA URL: https://golden1.com/
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000);
            test.pass("Step 1: Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("HomePage_Loaded");

            // Step 2: Click on Loans tab
            clickLoansTab();
            
            // Verify Loans submenu is displayed and contains expected options
            verifyLoansSubmenuDisplayed();
            
            test.pass("Test Case 1 completed successfully: User is able to view Loans tab and its submenu");

        } catch (Exception e) {
            test.fail("Test Case 1 failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test Case 1 failed due to exception: " + e.getMessage());
        }
    }

    private void clickLoansTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Look for the Loans tab in the navigation
            WebElement loansTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Loans') and @role='button']")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loansTab);
            Thread.sleep(1000);
            loansTab.click();
            
            test.pass("Step 2: Successfully clicked on Loans tab");
            takeScreenshot("Loans_Tab_Clicked");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking Loans tab: " + e.getMessage());
            takeScreenshot("Loans_Tab_Click_Failed");
            Assert.fail("Exception occurred while clicking Loans tab: " + e.getMessage());
        }
    }

    private void verifyLoansSubmenuDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Verify submenu is displayed
            WebElement submenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'sub-nav') and contains(@class,'open')] | //div[@class='sub-nav open']")));
            
            Assert.assertTrue(submenu.isDisplayed(), "Loans submenu should be displayed");
            test.pass("Loans submenu is successfully displayed");
            
            // Verify Personal Loans link is visible in submenu
            WebElement personalLoansLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(text(),'Personal Loans')]")));
            
            Assert.assertTrue(personalLoansLink.isDisplayed(), "Personal Loans link should be visible in submenu");
            test.pass("Personal Loans option is visible in the submenu");
            
            // Verify other loan options are visible
            WebElement autoLoansLink = driver.findElement(
                By.xpath("//a[contains(text(),'Auto Loans') or contains(text(),'New or Used Auto Loans')]"));
            Assert.assertTrue(autoLoansLink.isDisplayed(), "Auto Loans option should be visible");
            test.pass("Auto Loans option is visible in the submenu");
            
            WebElement recreationalLoansLink = driver.findElement(
                By.xpath("//a[contains(text(),'Recreational') or contains(text(),'Specialty Vehicle')]"));
            Assert.assertTrue(recreationalLoansLink.isDisplayed(), "Recreational/Specialty Vehicle Loans option should be visible");
            test.pass("Recreational/Specialty Vehicle Loans option is visible in the submenu");
            
            takeScreenshot("Loans_Submenu_Verified");

        } catch (Exception e) {
            test.fail("Exception occurred while verifying Loans submenu: " + e.getMessage());
            takeScreenshot("Submenu_Verification_Failed");
            Assert.fail("Exception occurred while verifying Loans submenu: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            test.pass("Browser closed successfully");
        }
        ExtentReportManager.flushReport();
    }

    private void takeScreenshot(String screenshotName) {
        if (TestConstants.ENABLE_SCREENSHOTS) {
            try {
                DriverUtils.takeScreenshot(driver, screenshotName);
            } catch (Exception e) {
                test.warning("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
}