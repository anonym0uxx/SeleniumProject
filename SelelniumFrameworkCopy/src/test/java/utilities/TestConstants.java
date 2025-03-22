package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestConstants {
    public static final String G1_HOME_URL = "https://www.golden1.com";
    public static final String EXPECTED_TITLE = "Supporting Your Financial Wellness | Golden 1 Credit Union";

    // Timeout durations
    public static final int DEFAULT_WAIT_TIME_SECONDS = 10;  // WebDriverWait default timeout

    // ExtentReport related constants
    public static final String REPORT_PATH = System.getProperty("user.dir") + "/test-output/extentReport.html";
    public static final String REPORT_NAME = "Golden1 Test Automation Report";

    // Global setting for screenshot capture (true = capture screenshots, false = no screenshots)
    public static final boolean ENABLE_SCREENSHOTS = true;  // Set to 'false' to disable screenshots
    public static final boolean ENABLE_EMAIL = true;  

    // Dynamic screenshots directory based on the timestamp of the test run
    public static final String TIMESTAMP = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    public static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/Screenshots/TestRun_" + TIMESTAMP + "/";
}
