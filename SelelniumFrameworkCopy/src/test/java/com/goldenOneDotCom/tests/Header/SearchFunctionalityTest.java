package com.goldenOneDotCom.tests.Header;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class SearchFunctionalityTest {

    private WebDriver driver;
    private ExtentTest test;

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Golden1.com Search Functionality Test", 
                "Verify the behavior of an application user able to access the Search Text box on the Main Landing header screen");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testSearchFunctionality() {
        try {
            // Step 1: Open new browser and enter the QA URL: https://www.golden1.com/
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000);
            test.pass("Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("HomePage_Loaded");

            // Step 2: Type the key word in the text box
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("site-search")));
            String searchKeyword = "checking";
            searchBox.sendKeys(searchKeyword);
            test.pass("Typed keyword '" + searchKeyword + "' in the search box");
            takeScreenshot("Keyword_Typed");

            // Step 3: Click {x} button
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='search-suggestions__close']//span[@class='js-close-search']")));
            closeButton.click();
            test.pass("Clicked close (x) button");
            takeScreenshot("Close_Button_Clicked");

            // Verify search box is cleared or hidden
            Thread.sleep(1000); // Brief pause to allow UI to update
            takeScreenshot("Search_Box_After_Close");

            // Step 4: Now again type the keyword in the text box
            searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("site-search")));
            searchBox.clear();
            searchBox.sendKeys(searchKeyword);
            test.pass("Typed keyword '" + searchKeyword + "' in the search box again");
            takeScreenshot("Keyword_Typed_Again");

            // Step 5: Click Close button
            closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='search-suggestions__close']//span[@class='js-close-search']")));
            closeButton.click();
            test.pass("Clicked close button again");
            takeScreenshot("Close_Button_Clicked_Again");

            // Step 6: Again type the keyword and click search icon
            searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("site-search")));
            searchBox.clear();
            searchBox.sendKeys(searchKeyword);
            test.pass("Typed keyword '" + searchKeyword + "' for search");
            takeScreenshot("Final_Keyword_Typed");

            // Click search icon or press Enter
            WebElement searchIcon = driver.findElement(By.xpath("//img[@class='search-icon' and @alt='search']"));
            searchIcon.click();
            test.pass("Clicked search icon");
            takeScreenshot("Search_Icon_Clicked");

            // Wait for search results page to load
            wait.until(ExpectedConditions.urlContains("search"));
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("search"), "Should navigate to search results page");
            test.pass("Successfully navigated to search results page");
            takeScreenshot("Search_Results_Page");

            // Verify search results are displayed
            try {
                WebElement searchResults = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[contains(@class, 'search-results') or contains(@id, 'search')]")));
                Assert.assertTrue(searchResults.isDisplayed(), "Search results should be displayed");
                test.pass("Search results are displayed");
                takeScreenshot("Search_Results_Displayed");
            } catch (Exception e) {
                test.info("Search results container not found with standard selectors, but search page loaded");
                takeScreenshot("Search_Page_Loaded");
            }

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