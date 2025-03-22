package utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ITestListener, ISuiteListener {

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("SuiteListener: onFinish triggered. Sending email...");

        // Call the method to send the report after all tests have completed
        ExtentReportManager.flushReport();  // Flushes the extent report
        ExtentReportManager.sendReportByEmail();  // Sends the report via email
        System.out.println("Email sent successfully.");
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("SuiteListener: onFinish triggered for Suite. Preparing to send test results via SMS...");

        // Generate a summary of the test results
        int totalTests = suite.getResults().size();
        int passedTests = (int) suite.getResults().values().stream()
                .flatMap(result -> result.getTestContext().getPassedTests().getAllResults().stream())
                .count();
        int failedTests = (int) suite.getResults().values().stream()
                .flatMap(result -> result.getTestContext().getFailedTests().getAllResults().stream())
                .count();

        String messageContent = String.format("Test Suite: %s\nTotal Tests: %d\nPassed: %d\nFailed: %d",
                suite.getName(), totalTests, passedTests, failedTests);

        // Send the results via Twilio SMS
        try {
            TwilioUtil.sendTestResults(messageContent);
            System.out.println("Test results sent successfully via SMS.");
        } catch (Exception e) {
            System.err.println("Failed to send SMS with Twilio: " + e.getMessage());
        }
    }

    // Other ITestListener methods can remain unimplemented if not needed, 
    // or you can implement them as per your requirements.
}
