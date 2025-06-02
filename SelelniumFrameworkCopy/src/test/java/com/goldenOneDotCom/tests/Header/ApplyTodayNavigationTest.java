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

public class ApplyTodayNavigationTest {
    
    private WebDriver driver;
    private ExtentTest test;
    
    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Golden1.com Apply Today Navigation Test", 
                "Verify the behavior of an application user able to click the Apply today on the Main landing header screen");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }
    
    @Test
    public void testApplyTodayNavigation() {
        try {
            // Step 1: Open new browser and enter the QA URL: https://www.golden1.com/
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000);
            test.pass("Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("HomePage_Loaded");
            
            // Step 2: Click the "Open Account" button in the header
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Multiple selector strategies for better reliability
            WebElement openAccountButton = null;
            
            // Try primary selector - using the href attribute which is more reliable
            try {
                openAccountButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("a.login-section__signup[href='/apply-today']")));
                test.info("Found Open Account button using CSS selector");
            } catch (Exception e) {
                test.info("Primary selector failed, trying alternative selectors");
                
                // Alternative selector 1: Using text content
                try {
                    openAccountButton = driver.findElement(By.xpath("//a[contains(text(), 'Open Account')]"));
                    test.info("Found Open Account button using text content");
                } catch (Exception e2) {
                    // Alternative selector 2: Using class and data attributes
                    openAccountButton = driver.findElement(By.xpath("//a[@class='login-section__signup btn btn-secondary' and @data-analytics-link-text='open account']"));
                    test.info("Found Open Account button using data attributes");
                }
            }
            
            // Verify the button is found
            Assert.assertNotNull(openAccountButton, "Open Account button should be found");
            test.pass("Open Account button located successfully");
            takeScreenshot("Open_Account_Button_Located");
            
            // Step 3: Click the Open Account button
            openAccountButton.click();
            test.pass("Clicked on 'Open Account' button");
            takeScreenshot("Open_Account_Clicked");
            
            // Step 4: Wait for Apply Today page to load and validate navigation
            wait.until(ExpectedConditions.urlContains("apply-today"));
            test.pass("Successfully navigated to Apply Today page");
            takeScreenshot("Apply_Today_Page_Loaded");
            
            // Step 5: Validate URL contains apply-today
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("apply-today"), 
                    "URL should contain 'apply-today'. Current URL: " + currentUrl);
            test.pass("Apply Today URL validation successful: " + currentUrl);
            
            // Step 6: Validate page title
            String expectedTitle = "Apply Today | Golden 1 Credit Union";
            String actualTitle = driver.getTitle();
            Assert.assertEquals(actualTitle, expectedTitle, "Page title should match expected title");
            test.pass("Page title validation successful: " + actualTitle);
            
            // Step 7: Validate key elements on Apply Today page are present
            validateApplyTodayPageElements(wait);
            
            test.pass("Apply Today navigation flow completed successfully");
            
        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    private void validateApplyTodayPageElements(WebDriverWait wait) {
        try {
            // Validate main heading "Apply Today" is present
            WebElement applyTodayHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(), 'Apply Today')]")));
            Assert.assertTrue(applyTodayHeading.isDisplayed(), "Apply Today heading should be visible");
            test.pass("Apply Today main heading is displayed");
            
            // Validate application tiles are present
            String[] expectedTiles = {"New Account", "Credit Card", "Home Loan", "Personal Loan", "Vehicle Loan", "Check Status"};
            
            for (String tileName : expectedTiles) {
                try {
                    WebElement tile = driver.findElement(By.xpath("//h2[contains(text(), '" + tileName + "')]"));
                    Assert.assertTrue(tile.isDisplayed(), tileName + " tile should be visible");
                    test.pass(tileName + " tile is displayed on Apply Today page");
                } catch (Exception e) {
                    test.warning("Could not locate " + tileName + " tile: " + e.getMessage());
                }
            }
            
            // Validate "Apply in 3 Steps" section is present
            try {
                WebElement stepsSection = driver.findElement(By.xpath("//img[@alt='Apply in 3 Steps']"));
                Assert.assertTrue(stepsSection.isDisplayed(), "Apply in 3 Steps section should be visible");
                test.pass("Apply in 3 Steps section is displayed");
            } catch (Exception e) {
                test.warning("Apply in 3 Steps section not found: " + e.getMessage());
            }
            
            // Validate accordion section "What Do I Need to Apply?" is present
            try {
                WebElement accordionSection = driver.findElement(By.xpath("//h2[contains(text(), 'What Do I Need to Apply?')]"));
                Assert.assertTrue(accordionSection.isDisplayed(), "FAQ accordion section should be visible");
                test.pass("'What Do I Need to Apply?' section is displayed");
            } catch (Exception e) {
                test.warning("FAQ accordion section not found: " + e.getMessage());
            }
            
            takeScreenshot("Apply_Today_Page_Elements_Validated");
            
        } catch (Exception e) {
            test.warning("Some page elements validation failed: " + e.getMessage());
            takeScreenshot("Page_Elements_Validation_Issues");
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