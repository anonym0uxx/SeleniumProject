package com.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestConstantsTest {
        // Website's URL's
        public static final String G1_HOME_URL = "https://www.golden1.com";
        public static final String EXPECTED_TITLE = "Supporting Your Financial Wellness | Golden 1 Credit Union";
        public static final String GUIDED_WEALTH_PORTFOLIOS_URL = "https://www.golden1.com/planning-and-investing/investment-services/guided-wealth-portfolios";
        public static final String CALIFORNIA_INSURANCE_LICENSE_INFORMATION_PDF = "https://www.golden1.com/-/media/golden1/site-documents/investment-services/ca-insurance-license-information.pdf?rev=96ff05fb56304b96a85c8a8661c8dc12&hash=7AD20D25402C3FD950116A86AA723B37";
        public static final String BROKER_CHECK_URL = "https://brokercheck.finra.org/";
        public static final String FINRA_WEBSITE_URL = "https://www.finra.org/";
        public static final String SIPC_WEBSITE_URL = "https://www.sipc.org/";
        public static final String DOWNLOAD_FOR_IOS_URL = "https://apps.apple.com/us/app/lpl-account-view/id1397972267";
        public static final String DOWNLOAD_FOR_ANDROID_URL = "https://play.google.com/store/apps/details?id=com.lpl.avlpl&hl=en_US&gl=US";
        public static final String SELF_GUIDED_INVESTING_URL_REDIRECTION = "https://www.golden1.com/planning-and-investing/investment-services/guided-wealth-portfolios";
        public static final String LPL_FINANCIAL_WEBSITE_BASIC = "https://www.myaccountviewonline.com/prospectview/home/g1digital";
        public static final String LPL_FINANCIAL_WEBSITE_PLUS =  "https://www.myaccountviewonline.com/prospectview/home/g1digitalplus";
        public static final String ADDITIONAL_FAQS_SELF_GUIDED_INVESTING_PAGE = "https://www.golden1.com/planning-and-investing/investment-services/guided-wealth-portfolios/gwp-faqs";

        // Timeout durations
        public static final int DEFAULT_WAIT_TIME_SECONDS = 10; // WebDriverWait default timeout

        // ExtentReport related constants
        public static final String REPORT_PATH = System.getenv("USERPROFILE")
                        + "\\OneDrive\\Selenium Automation Test Outputs";
        public static final String REPORT_NAME = "Golden1 Test Automation Report";

        // Global setting for screenshot capture (true = capture screenshots, false = no
        // screenshots)
        public static final boolean ENABLE_SCREENSHOTS = true; // Set to 'false' to disable screenshots
        public static final boolean ENABLE_EMAIL = true;

        // Dynamic screenshots directory based on the timestamp of the test run
        public static final String TIMESTAMP = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        public static final String SCREENSHOT_DIR = System.getenv("USERPROFILE")
                        + "\\OneDrive\\Selenium Automation Test Outputs\\";
        public static String SUITE_NAME = "";
        //We will organize the screenshots by test as well
        public static String TEST_NAME ="";

        // JsonFilePaths Constants
        public static final String INVESTMENT_SERVICES_HEADERS_JSON = "src/test/resources/investmentServicesHeaders.json";
        public static final String SELF_GUIDED_INVESTMENT_HEADERS_JSON = "src/test/resources/selfGuidedInvestmentHeaders.json";
}
