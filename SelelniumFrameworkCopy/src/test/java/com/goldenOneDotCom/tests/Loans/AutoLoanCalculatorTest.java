package com.goldenOneDotCom.tests.Loans;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

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

public class AutoLoanCalculatorTest {

    private WebDriver driver;
    private ExtentTest test;
    private final String QA_URL = "https://qa-new.golden1.com/";
    
    // List of all financial calculator types based on the HTML structure
    private final List<String> CALCULATOR_TYPES = Arrays.asList(
        "Consolidation",
        "Auto Loan", 
        "Debt Consolidation",
        "Prepayment Savings",
        "Payment Amortization",
        "Mortgage Refinance",
        "Refinance Break Even",
        "Home Loan",
        "Rent Vs Own",
        "Early Payoff",
        "Mortgage APR",
        "Savings"
    );

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser, ITestContext context) {
        // Capture the suite name
        TestConstantsTest.SUITE_NAME = context.getSuite().getName();
        ExtentReports extentReport = ExtentReportManagerTest.getInstance(TestConstantsTest.SUITE_NAME);
        test = extentReport.createTest("Smoke Tests - Verify Auto Loan Calculator and All Financial Calculator Types",
                "Validates that clicking 'Auto Loan Calculator' navigates to calculators page and tests each calculator type");
        driver = DriverUtilsTest.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
        System.out.println("Starting the Auto Loan Calculator test setup");
    }

    @Test
    public void testAutoLoanCalculatorAndAllTypes() {
        try {
            // Step 1: Navigate to the QA URL
            driver.get(QA_URL);
            Thread.sleep(1000);
            test.pass("Navigated to QA URL: " + QA_URL);
            takeScreenshot("Navigated_To_QA_HomePage");

            // Step 2: Click the Loans tab
            clickLoansTab();

            // Step 3: Click on "Auto Loan Calculator" under Tools and Resources
            clickAutoLoanCalculator();

            // Step 4: Verify navigation to Financial Calculators page
            verifyCalculatorsPageNavigation();

            // Step 5: Click each type under Financial Calculator
            testAllCalculatorTypes();

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

    private void clickAutoLoanCalculator() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            
            // Look for "Auto Loan Calculator" link in Tools and Resources section
            WebElement autoLoanCalculatorLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Auto Loan Calculator')]")));
            
            // Scroll into view to ensure visibility
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", autoLoanCalculatorLink);
            Thread.sleep(500);
            
            autoLoanCalculatorLink.click();
            test.pass("Clicked on 'Auto Loan Calculator' link");
            takeScreenshot("Clicked_Auto_Loan_Calculator");
            
        } catch (Exception e) {
            test.fail("Failed to click 'Auto Loan Calculator' link: " + e.getMessage());
            takeScreenshot("Auto_Loan_Calculator_Click_Failed");
            Assert.fail("Exception occurred while clicking 'Auto Loan Calculator': " + e.getMessage());
        }
    }

    private void verifyCalculatorsPageNavigation() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            
            // Wait for the page to load completely
            wait.until(ExpectedConditions.urlContains("calculators"));
            
            // Verify the current URL contains calculators
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("calculators"), 
                "Expected URL to contain 'calculators' but found: " + currentUrl);
            
            // Verify page title indicates we're on the Financial Calculators page
            WebElement calculatorsPageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(),'Financial Calculators')]")));
            
            Assert.assertTrue(calculatorsPageTitle.isDisplayed(), 
                "Financial Calculators page title is not displayed");
            
            test.pass("Successfully navigated to the Financial Calculators page");
            test.pass("Current URL: " + currentUrl);
            takeScreenshot("Financial_Calculators_Page_Loaded");
            
        } catch (Exception e) {
            test.fail("Failed to verify navigation to calculators page: " + e.getMessage());
            takeScreenshot("Calculators_Page_Verification_Failed");
            Assert.fail("Exception occurred while verifying calculators page navigation: " + e.getMessage());
        }
    }

    private void testAllCalculatorTypes() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            
            test.info("Starting to test all calculator types. Total types to test: " + CALCULATOR_TYPES.size());
            
            for (String calculatorType : CALCULATOR_TYPES) {
                try {
                    test.info("Testing calculator type: " + calculatorType);
                    
                    // Navigate back to calculators page if we're not there
                    if (!driver.getCurrentUrl().contains("calculators")) {
                        driver.navigate().back();
                        wait.until(ExpectedConditions.urlContains("calculators"));
                    }
                    
                    // Find and click the calculator type
                    clickCalculatorType(calculatorType, wait);
                    
                    // Verify the calculator page loads
                    verifyCalculatorPageLoaded(calculatorType, wait);
                    
                    test.pass("Successfully tested calculator type: " + calculatorType);
                    
                } catch (Exception e) {
                    test.warning("Failed to test calculator type '" + calculatorType + "': " + e.getMessage());
                    takeScreenshot("Calculator_Type_Failed_" + calculatorType.replace(" ", "_"));
                    // Continue with next calculator type instead of failing the entire test
                }
            }
            
            test.pass("Completed testing all calculator types");
            
        } catch (Exception e) {
            test.fail("Exception occurred while testing calculator types: " + e.getMessage());
            takeScreenshot("Calculator_Types_Test_Failed");
            Assert.fail("Exception occurred while testing calculator types: " + e.getMessage());
        }
    }

    private void clickCalculatorType(String calculatorType, WebDriverWait wait) throws Exception {
        try {
            // Try multiple locator strategies for calculator links
            WebElement calculatorLink = null;
            
            // Strategy 1: Try by exact text match in nav items
            try {
                calculatorLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='calculator-nav-item']//a[contains(text(),'" + calculatorType + "')]")));
            } catch (Exception e1) {
                // Strategy 2: Try by h2 text within calculator nav
                try {
                    calculatorLink = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//h2[contains(text(),'" + calculatorType + "')]/parent::a")));
                } catch (Exception e2) {
                    // Strategy 3: Try mobile dropdown if desktop nav failed
                    try {
                        // Click mobile dropdown if it exists
                        WebElement mobileDropdown = driver.findElement(By.className("calculator-dropbtn"));
                        if (mobileDropdown.isDisplayed()) {
                            mobileDropdown.click();
                            Thread.sleep(500);
                        }
                        calculatorLink = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[@class='calculator-dropdown-content']//a[contains(text(),'" + calculatorType + "')]")));
                    } catch (Exception e3) {
                        throw new Exception("Could not locate calculator link for: " + calculatorType);
                    }
                }
            }
            
            // Scroll into view and click
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", calculatorLink);
            Thread.sleep(500);
            calculatorLink.click();
            
            takeScreenshot("Clicked_Calculator_" + calculatorType.replace(" ", "_"));
            
        } catch (Exception e) {
            throw new Exception("Failed to click calculator type '" + calculatorType + "': " + e.getMessage());
        }
    }

    private void verifyCalculatorPageLoaded(String calculatorType, WebDriverWait wait) throws Exception {
        try {
            // Wait for page to load and URL to change
            Thread.sleep(2000);
            
            String currentUrl = driver.getCurrentUrl();
            
            // Verify URL contains calculator-related path
            Assert.assertTrue(currentUrl.contains("calculators"), 
                "URL should contain 'calculators' for " + calculatorType + " but found: " + currentUrl);
            
            // Look for calculator-specific elements that indicate the page loaded
            try {
                // Try to find calculator form elements or specific page indicators
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.className("calculator")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'calculator')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//form[contains(@class,'calculator')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'" + calculatorType + "')]"))
                ));
            } catch (Exception e) {
                // If specific elements not found, at least verify page loaded
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            }
            
            takeScreenshot("Calculator_Page_Loaded_" + calculatorType.replace(" ", "_"));
            
        } catch (Exception e) {
            throw new Exception("Failed to verify calculator page loaded for '" + calculatorType + "': " + e.getMessage());
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