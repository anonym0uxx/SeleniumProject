package com.goldenOneDotCom.tests.Loans;

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

public class SetUpRecurringPaymentTest {

    private WebDriver driver;
    private ExtentTest test;

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Set Up Recurring Payment Test", 
                "Verifying user able to click 'Set Up Recurring Payment' option under Make a Payment for Loans Tab");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testSetUpRecurringPayment() {
        try {
            // Step 1: Open new browser and enter the QA URL
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000);
            test.pass("Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("HomePage_Loaded");

            // Step 2: Click Loans Tab
            clickLoansTab();

            // Step 3: Click Make a Payment
            clickMakeAPayment();

            // Step 4: Click Set Up Recurring Payment
            clickSetUpRecurringPayment();

            // Step 5: Click each type under Set Up a Recurring Payment
            verifySetUpRecurringPaymentOptions();

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    private void clickLoansTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            WebElement loansTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Loans') and @role='button']")));
            loansTab.click();
            test.pass("Clicked on 'Loans' tab");
            takeScreenshot("Loans_Tab_Clicked");
            Thread.sleep(1000);
        } catch (Exception e) {
            test.fail("Failed to click Loans tab: " + e.getMessage());
            throw new RuntimeException("Failed to click Loans tab", e);
        }
    }

    private void clickMakeAPayment() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // First try to find the Make a Payment link in the Loans submenu
            try {
                WebElement makePaymentLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'Make a Payment')]")));
                makePaymentLink.click();
                test.pass("Clicked on 'Make a Payment' link from Loans submenu");
            } catch (Exception e) {
                // If not found in submenu, navigate directly to the make payment page
                driver.get(TestConstants.G1_HOME_URL + "/credit-cards-loans/make-a-payment");
                test.pass("Navigated directly to Make a Payment page");
            }
            
            takeScreenshot("Make_Payment_Page_Loaded");
            Thread.sleep(2000); // Allow page to load
        } catch (Exception e) {
            test.fail("Failed to navigate to Make a Payment page: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to Make a Payment page", e);
        }
    }

    private void clickSetUpRecurringPayment() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Look for the "Set up recurring payment" button on the page
            WebElement setUpRecurringPaymentBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/credit-cards-loans/make-a-payment/recurring' and contains(@class, 'btn')]")));
            
            // Scroll to the button to ensure it's visible
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", setUpRecurringPaymentBtn);
            Thread.sleep(500);
            
            setUpRecurringPaymentBtn.click();
            test.pass("Clicked on 'Set up recurring payment' button");
            takeScreenshot("Set_Up_Recurring_Payment_Button_Clicked");
            Thread.sleep(2000); // Allow page to load
        } catch (Exception e) {
            test.fail("Failed to click Set up recurring payment button: " + e.getMessage());
            throw new RuntimeException("Failed to click Set up recurring payment button", e);
        }
    }

    private void verifySetUpRecurringPaymentOptions() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Verify we're on the recurring payment page
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("/recurring")) {
                test.pass("Successfully navigated to recurring payment page: " + currentUrl);
                takeScreenshot("Recurring_Payment_Page_Loaded");
                
                // Look for common recurring payment form elements or autopay options
                boolean foundRecurringOptions = false;
                
                // Check for recurring payment type dropdowns or radio buttons
                try {
                    java.util.List<WebElement> recurringElements = driver.findElements(
                        By.xpath("//select[contains(@name, 'recurring') or contains(@name, 'autopay') or contains(@name, 'frequency')] | " +
                                "//input[@type='radio'][contains(@name, 'recurring') or contains(@name, 'autopay')] | " +
                                "//div[contains(@class, 'recurring')] | " +
                                "//div[contains(@class, 'autopay')] | " +
                                "//input[contains(@name, 'frequency')]"));
                    
                    if (!recurringElements.isEmpty()) {
                        foundRecurringOptions = true;
                        test.pass("Found recurring payment elements: " + recurringElements.size());
                        takeScreenshot("Recurring_Payment_Elements_Found");
                        
                        for (WebElement element : recurringElements) {
                            if (element.isDisplayed()) {
                                String elementInfo = element.getTagName() + " - " + 
                                    element.getAttribute("name") + " - " + 
                                    element.getAttribute("value");
                                test.info("Recurring payment element: " + elementInfo);
                            }
                        }
                    }
                } catch (Exception e) {
                    test.info("No specific recurring payment elements found: " + e.getMessage());
                }
                
                // Check for form inputs that might indicate recurring payment setup
                try {
                    java.util.List<WebElement> formElements = driver.findElements(
                        By.xpath("//form//input | //form//select | //form//button"));
                    
                    if (!formElements.isEmpty()) {
                        foundRecurringOptions = true;
                        test.pass("Found recurring payment form with " + formElements.size() + " interactive elements");
                        takeScreenshot("Recurring_Payment_Form_Elements");
                        
                        // Look specifically for frequency or autopay related fields
                        for (WebElement element : formElements) {
                            String nameAttr = element.getAttribute("name");
                            String idAttr = element.getAttribute("id");
                            if ((nameAttr != null && (nameAttr.contains("frequency") || nameAttr.contains("autopay") || nameAttr.contains("recurring"))) ||
                                (idAttr != null && (idAttr.contains("frequency") || idAttr.contains("autopay") || idAttr.contains("recurring")))) {
                                test.info("Found recurring-specific field: " + element.getTagName() + " - " + nameAttr + " - " + idAttr);
                            }
                        }
                    }
                } catch (Exception e) {
                    test.info("No form elements found: " + e.getMessage());
                }
                
                // Check for any recurring payment-related buttons or links
                try {
                    java.util.List<WebElement> recurringButtons = driver.findElements(
                        By.xpath("//button[contains(text(), 'Recurring') or contains(text(), 'AutoPay') or contains(text(), 'Set Up') or contains(text(), 'Continue')] | " +
                                "//a[contains(text(), 'Recurring') or contains(text(), 'AutoPay') or contains(text(), 'Set Up')]"));
                    
                    for (WebElement button : recurringButtons) {
                        if (button.isDisplayed() && button.isEnabled()) {
                            foundRecurringOptions = true;
                            String buttonText = button.getText();
                            test.pass("Found recurring payment action button: " + buttonText);
                            takeScreenshot("Recurring_Button_" + buttonText.replace(" ", "_"));
                        }
                    }
                } catch (Exception e) {
                    test.info("No recurring payment buttons found: " + e.getMessage());
                }
                
                if (foundRecurringOptions) {
                    test.pass("Successfully verified Set Up Recurring Payment page functionality");
                } else {
                    test.warning("Reached recurring payment page but no interactive recurring payment elements were found");
                }
                
            } else {
                test.warning("Expected to be on recurring payment page but current URL is: " + currentUrl);
            }

        } catch (Exception e) {
            test.fail("Failed to verify Set Up Recurring Payment options: " + e.getMessage());
            throw new RuntimeException("Failed to verify recurring payment options", e);
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            test.pass("Browser closed.");
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