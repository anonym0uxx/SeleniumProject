package com.goldenOneDotCom.tests.SmokeTests.Header;

import org.openqa.selenium.By;
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

public class LogoVerificationTest {

    private WebDriver driver;
    private ExtentTest test;

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Golden1.com Logo Verification Test", 
                "Verify the behavior of an application user able to click the Golden1 Credit Union Logo on the Main landing header screen");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testLogoClickBehavior() {
        try {
            // Step 1: Open new browser and enter the QA URL: https://www.golden1.com/
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000);
            test.pass("Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("HomePage_Loaded");

            // Step 2: Open any of the tab on the main landing Page
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            WebElement checkingTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@class='nav-item-link' and contains(text(), 'Checking')]")));
            checkingTab.click();
            test.pass("Clicked on Checking tab");
            takeScreenshot("Checking_Tab_Opened");

            // Step 3: Now click any of the sub links on the present tab
            WebElement freeCheckingLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/checking/free-checking-account' and contains(text(), 'Free Checking')]")));
            freeCheckingLink.click();
            test.pass("Clicked on Free Checking sub-link");
            takeScreenshot("Free_Checking_Page_Loaded");

            // Verify we're not on the home page anymore
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.equals(TestConstants.G1_HOME_URL), 
                    "Should have navigated away from home page");
            test.pass("Confirmed navigation away from home page");

            // Step 4: Now click the Golden1 Credit Union Logo at the top left on the present screen
            WebElement logo = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='logo']//a[@href='/']")));
            logo.click();
            test.pass("Clicked on Golden 1 Credit Union logo");
            takeScreenshot("Logo_Clicked");

            // Verify that clicking the logo returns to home page
            wait.until(ExpectedConditions.urlToBe(TestConstants.G1_HOME_URL));
            String finalUrl = driver.getCurrentUrl();
            Assert.assertEquals(finalUrl, TestConstants.G1_HOME_URL, 
                    "Logo should navigate back to home page");
            test.pass("Logo successfully navigated back to home page");
            takeScreenshot("Back_To_HomePage");

            // Verify page title is correct
            String actualTitle = driver.getTitle();
            Assert.assertEquals(actualTitle, TestConstants.EXPECTED_TITLE, "Page title should match expected title");
            test.pass("Page title verified successfully");

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test failed due to exception: " + e.getMessage());
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