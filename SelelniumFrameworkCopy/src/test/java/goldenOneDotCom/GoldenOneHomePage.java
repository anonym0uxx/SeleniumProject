package goldenOneDotCom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utilities.DriverUtils;
import utilities.ExtentReportManager;
import utilities.TestConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class GoldenOneHomePage {

    static ExtentTest Golden1SmokeTest;
    static ExtentReports extentReport;
    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        // Set up ExtentReports and SparkReporter
        extentReport = ExtentReportManager.getInstance();
        Golden1SmokeTest = extentReport.createTest("Golden1.com Smoke Test", "Validates basic member-facing website functionality");

        // Setup WebDriver
        driver = DriverUtils.getDriver("chrome");
        driver.manage().window().maximize();
        Golden1SmokeTest.log(Status.INFO, "Browser started and maximized");
    }

    @Test
    public void testGolden1HomePage() {
        // Capture start time (before loading the page)
        long startTime = System.currentTimeMillis();

        // Navigate to Golden1.com
        driver.get(TestConstants.G1_HOME_URL);
        Golden1SmokeTest.pass("Navigated to " + TestConstants.G1_HOME_URL);
        DriverUtils.takeScreenshot(driver, "HomePage_Navigation");

        // Capture end time (after the page has loaded)
        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;
        Golden1SmokeTest.log(Status.INFO, "Browser load time: " + loadTime + " milliseconds");

        // Validate the page title
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, TestConstants.EXPECTED_TITLE, "Title mismatch!");
        Golden1SmokeTest.pass("Title validated successfully");
        DriverUtils.takeScreenshot(driver, "HomePage_TitleValidation");

        // Validate UI elements (Checking, Savings, Home Loans, etc.)
        validateTab("Checking");
        validateTab("Savings");
        validateTab("Home Loans");
        validateTab("Credit Cards");
        validateTab("Loans");
        validateTab("Investing");
        validateTab("Community");
    }

    private void validateTab(String tabName) {
        WebElement tabElement = driver.findElement(By.linkText(tabName));
        Assert.assertEquals(tabElement.getText(), tabName, "Tab text mismatch for: " + tabName);
        Golden1SmokeTest.pass("Validated " + tabName + " Tab UI Is Accurate");
        DriverUtils.takeScreenshot(driver, "TabValidation_" + tabName);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();  // Ensure browser closes after test
            Golden1SmokeTest.pass("Browser closed successfully");
        }
        ExtentReportManager.flushReport();
        Golden1SmokeTest.info("Test completed");
    }
}
