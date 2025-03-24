package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extentReport;

    // Singleton instance for ExtentReports
    public static ExtentReports getInstance() {
        if (extentReport == null) {
            // Create the ExtentReports instance and attach SparkReporter
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(TestConstants.REPORT_PATH);
            htmlReporter.config().setReportName(TestConstants.REPORT_NAME);

            extentReport = new ExtentReports();
            extentReport.attachReporter(htmlReporter);
        }
        return extentReport;
    }

    // Flush the report after all tests are run
    public static void flushReport() {
        if (extentReport != null) {
            extentReport.flush();
        }
    }

    public static void sendReportByEmail() {
        System.out.println("Attempting to send email...");  // Add this for debugging

        try {
            String reportPath = TestConstants.REPORT_PATH;
            String recipientEmail = "REPLACE";  // Replace with recipient email
            String senderEmail = "REPLACE";          // Your email (sender)
            String senderPassword = "REPLACE";        // Your email password (or app-specific password)
            String subject = "TestNG Suite Emailable Report";
            String body = "Please find the attached report summarizing all test executions from the Golden1.com suite.";

            EmailUtil.sendEmailWithReport(senderEmail, senderPassword, recipientEmail, subject, body, reportPath);
            System.out.println("Email sent successfully!");  // Add this for debugging
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}