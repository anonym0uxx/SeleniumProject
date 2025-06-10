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

public class ExploreLoanOptionsTest {

    private WebDriver driver;
    private ExtentTest test;
    
    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Golden1.com Explore Loan Options Test", 
            "Tests the Explore Loan Options functionality under Loans tab with Cancel and Proceed scenarios.");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testExploreLoanOptions() {
        try {
            // Step 1: Navigate to Golden1.com
            navigateToHomePage();
            
            // Step 2: Click on Loans tab to open the dropdown
            clickLoansTab();
            
            // Step 3: Click "Explore my loan options" for the first time
            clickExploreLoanOptions();
            
            // Step 4: Verify popup appears and click Cancel
            handlePopupCancel();
            
            // Step 5: Click "Explore my loan options" again
            clickExploreLoanOptionsAgain();
            
            // Step 6: Click Proceed this time
            handlePopupProceed();
            
            // Step 7: Verify we are redirected to College Ave website
            verifyCollegeAveRedirection();

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    private void navigateToHomePage() {
        try {
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000); // Static wait to ensure page loads
            
            // Verify we're on the correct page
            String actualTitle = driver.getTitle();
            Assert.assertEquals(actualTitle, TestConstants.EXPECTED_TITLE, "Homepage title mismatch!");
            
            test.pass("Successfully navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("Homepage_Loaded");
            
        } catch (Exception e) {
            test.fail("Failed to navigate to homepage: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to homepage", e);
        }
    }

    private void clickLoansTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Locate and click the Loans tab
            WebElement loansTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Loans') and @role='button']")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loansTab);
            Thread.sleep(500);
            loansTab.click();
            
            // Verify the sub-navigation menu opens
            WebElement subNavMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='sub-nav open']")));
            Assert.assertTrue(subNavMenu.isDisplayed(), "Loans sub-navigation menu did not open");
            
            test.pass("Successfully clicked Loans tab and opened sub-navigation menu");
            takeScreenshot("Loans_Tab_Opened");
            
        } catch (Exception e) {
            test.fail("Failed to click Loans tab: " + e.getMessage());
            throw new RuntimeException("Failed to click Loans tab", e);
        }
    }

    private void clickExploreLoanOptions() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Locate the "Explore my loan options" button
            WebElement exploreLoanButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Explore my loan options')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", exploreLoanButton);
            Thread.sleep(500);
            exploreLoanButton.click();
            
            test.pass("Successfully clicked 'Explore my loan options' button (first time)");
            takeScreenshot("Explore_Loan_Options_Clicked_First");
            
        } catch (Exception e) {
            test.fail("Failed to click 'Explore my loan options' button: " + e.getMessage());
            throw new RuntimeException("Failed to click 'Explore my loan options' button", e);
        }
    }

    private void handlePopupCancel() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Wait for the external redirect modal to appear
            WebElement modalHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'YOU ARE ABOUT TO LEAVE GOLDEN 1 WEBSITE')]")));
            Assert.assertTrue(modalHeading.isDisplayed(), "External redirect modal did not appear");
            
            test.pass("External redirect modal appeared as expected");
            takeScreenshot("External_Redirect_Modal_Appeared");
            
            // Click the Cancel button
            WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Cancel')] | //a[contains(text(),'Cancel')]")));
            cancelButton.click();
            
            // Verify we're still on the Golden1 website (modal should close)
            Thread.sleep(1000);
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("golden1.com"), 
                "Should still be on Golden1 website after clicking Cancel");
            
            test.pass("Successfully clicked Cancel button and remained on Golden1 website");
            takeScreenshot("After_Cancel_Click");
            
        } catch (Exception e) {
            test.fail("Failed to handle popup cancel: " + e.getMessage());
            throw new RuntimeException("Failed to handle popup cancel", e);
        }
    }

    private void clickExploreLoanOptionsAgain() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // We might need to open the Loans tab again if the menu closed
            try {
                WebElement subNavMenu = driver.findElement(By.xpath("//div[@class='sub-nav open']"));
                if (!subNavMenu.isDisplayed()) {
                    clickLoansTab();
                }
            } catch (Exception e) {
                // Sub-nav might not be visible, try to open it again
                clickLoansTab();
            }
            
            // Click "Explore my loan options" button again
            WebElement exploreLoanButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Explore my loan options')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", exploreLoanButton);
            Thread.sleep(500);
            exploreLoanButton.click();
            
            test.pass("Successfully clicked 'Explore my loan options' button (second time)");
            takeScreenshot("Explore_Loan_Options_Clicked_Second");
            
        } catch (Exception e) {
            test.fail("Failed to click 'Explore my loan options' button again: " + e.getMessage());
            throw new RuntimeException("Failed to click 'Explore my loan options' button again", e);
        }
    }

    private void handlePopupProceed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Wait for the external redirect modal to appear again
            WebElement modalHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'YOU ARE ABOUT TO LEAVE GOLDEN 1 WEBSITE')]")));
            Assert.assertTrue(modalHeading.isDisplayed(), "External redirect modal did not appear on second click");
            
            test.pass("External redirect modal appeared again as expected");
            takeScreenshot("External_Redirect_Modal_Second_Time");
            
            // Click the Proceed button
            WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Proceed')] | //a[contains(text(),'Proceed')]")));
            proceedButton.click();
            
            test.pass("Successfully clicked Proceed button");
            takeScreenshot("After_Proceed_Click");
            
        } catch (Exception e) {
            test.fail("Failed to handle popup proceed: " + e.getMessage());
            throw new RuntimeException("Failed to handle popup proceed", e);
        }
    }

    private void verifyCollegeAveRedirection() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Longer wait for external redirect
            
            // Wait for new tab/window to open and switch to it
            String originalWindow = driver.getWindowHandle();
            
            // Check if a new tab was opened
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            
            // Switch to the new tab
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            
            // Verify we're now on College Ave website
            wait.until(ExpectedConditions.urlContains("collegeave.com"));
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("collegeave.com"), 
                "Should be redirected to College Ave website. Current URL: " + currentUrl);
            
            test.pass("Successfully redirected to College Ave website: " + currentUrl);
            takeScreenshot("College_Ave_Website_Loaded");
            
            // Verify page title or content to ensure we're on the right page
            String pageTitle = driver.getTitle();
            test.info("College Ave page title: " + pageTitle);
            
            // Switch back to original window for cleanup
            driver.close(); // Close the College Ave tab
            driver.switchTo().window(originalWindow);
            
        } catch (Exception e) {
            test.fail("Failed to verify College Ave redirection: " + e.getMessage());
            // Try to switch back to original window for cleanup
            try {
                String originalWindow = driver.getWindowHandles().iterator().next();
                driver.switchTo().window(originalWindow);
            } catch (Exception switchException) {
                test.warning("Failed to switch back to original window: " + switchException.getMessage());
            }
            throw new RuntimeException("Failed to verify College Ave redirection", e);
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            // Close all windows and quit the driver
            for (String window : driver.getWindowHandles()) {
                driver.switchTo().window(window);
                driver.close();
            }
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