package com.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DriverUtilsTest {

    // Method to initialize WebDriver based on browser type
    public static WebDriver getDriver(String browser) {
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            case "safari":
                // SafariDriver does not need WebDriverManager since it's built-in on macOS
                driver = new SafariDriver();
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        driver.manage().window().maximize();
        return driver;
    }

    // Method to take a screenshot and save it in the dynamically created folder
    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        if (TestConstantsTest.ENABLE_SCREENSHOTS) {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                // Define the directory and file path
                String screenshotDir = TestConstantsTest.SCREENSHOT_DIR;
                //Since we already executed the Test we have the suite name assigned to our Test Constant
                screenshotDir = screenshotDir + File.separator+TestConstantsTest.SUITE_NAME +File.separator+TestConstantsTest.TIMESTAMP +File.separator+ "Screenshots";
                File dir = new File(screenshotDir);
                if (!dir.exists()) dir.mkdirs();  // Create the directory if it doesn't exist
                System.out.println("Screenshots DIR: "+screenshotDir);
                String filePath = screenshotDir +File.separator+ screenshotName + ".png";
                FileUtils.copyFile(screenshotFile, new File(filePath));
                System.out.println("Screenshot saved: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
