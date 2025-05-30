package com.goldenOneDotCom.tests.InvestingTab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

public class ValidateInvestingTabContentTest {

    private WebDriver driver;
    private ExtentTest test;

    @Parameters("browser")
    @BeforeTest

    public void setUp(@Optional("chrome") String browser, ITestContext context) {
        // We capture the suite name
        TestConstantsTest.SUITE_NAME = context.getSuite().getName();
        ExtentReports extentReport = ExtentReportManagerTest.getInstance(TestConstantsTest.SUITE_NAME);
        test = extentReport.createTest("Smoke Tests - Validate Investing Tab SubNavMenu Links",
                "Validate that after clicking the investing tab all subsections/link are displayed in the subnav menu");
        driver = DriverUtilsTest.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
        System.out.println("Starting the setup");
    }

    @Test
    public void testClickInvestingTab() {
        try {
            // Step 1: Navigate to Golden1.com
            driver.get(TestConstantsTest.G1_HOME_URL);
            Thread.sleep(1000);

            // Step2: Click into the Investing Tab on the homepage
            clickInvestingTab();
            // Step 3: Validate the Investing Tab Links are displayed
            verifyInvestingTabLinksDisplayed();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Static wait to slow down execution
    }

    private void verifyInvestingTabLinksDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            // First checking the submenu of the nav option selected is displayed
            WebElement subNavMenu = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"sub-nav open\"]")));
            if (subNavMenu.isDisplayed()) {
                // Locating every link element
                WebElement makeInvestmentPlanLink = wait.until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//a[contains(text(),'Make an Investment Plan')]")));
                WebElement selfGuidedInvestingLink = wait.until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//a[contains(text(),'Self-Guided Investing')]")));
                WebElement findInvestmentAdvisorLink = wait.until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//a[contains(text(),'Find an Investment Advisor')]")));
                WebElement insuraceProtectionOptionsLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[contains(text(),'Insurance & Protection Options')]")));
                // Checking if every element is displayed correctly
                if (makeInvestmentPlanLink.isDisplayed() && selfGuidedInvestingLink.isDisplayed()
                        && findInvestmentAdvisorLink.isDisplayed() && insuraceProtectionOptionsLink.isDisplayed())
                    // changing test status to pass
                    test.pass("Investment Tab Links are all displayed correctly");
                // Taking the screenshot
                takeScreenshot("Investing_Tab_Links_Displayed");
            }
        } catch (Exception e) {
            test.fail("Something occurred while trying to check if the links are displayed...." + e.getMessage());
        }
    }

    private void clickInvestingTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            WebElement investingTabLink = wait.until(ExpectedConditions
                    .elementToBeClickable(By.xpath("//a[contains(text(),'Investing') and @role=\"button\"]")));
            Thread.sleep(1000);// We wait a little bit after locating the element
            investingTabLink.click();
            test.pass("Clicked Investing Tab");
            takeScreenshot("Clicked_Investing_Tab");
        } catch (Exception e) {
            test.fail("Exception ocurred while clicking Investing Tab: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            // Closes the browser
            driver.quit();
            test.pass("Browser closed");
        }
        // Sends the report to the desired location
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
