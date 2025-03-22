package utilities;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.ApiException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class TwilioUtil {
    // Replace with your Twilio credentials once masked
    public static final String ACCOUNT_SID = "ENTER ACCT SID";
    public static final String AUTH_TOKEN = "ENTER AUTH TOKEN";
    public static final String TWILIO_PHONE_NUMBER = "ENTER TWILIO CELL #"; // Replace with your Twilio phone number

    // Array of recipient phone numbers - can add as many here as needed
    public static final String[] RECIPIENT_PHONE_NUMBERS = {"Phone1", "Phone2"};

    // Initialize Twilio
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendTestResults(String messageContent) {
        // Iterate through array to send message to each recipient
        for (String recipient : RECIPIENT_PHONE_NUMBERS) {
            try {
                Message message = Message.creator(
                        new PhoneNumber(recipient),
                        new PhoneNumber(TWILIO_PHONE_NUMBER),
                        messageContent)
                        .create();

                if (message.getErrorCode() != null) {
                    logError("Twilio API Error: Code: " + message.getErrorCode() + ", Message: " + message.getErrorMessage());
                } else {
                    logInfo("Sent message to " + recipient + ": " + message.getSid());
                }
            } catch (ApiException e) {
                logError("Twilio API Exception for recipient " + recipient + ": " + e.getMessage());
            } catch (Exception e) {
                logError("General Exception while sending message to " + recipient + ": " + e.getMessage());
            }
        }
    }

    // Log successful operations
    private static void logInfo(String infoMessage) {
        try (FileWriter writer = new FileWriter("twilio_info.log", true)) {
            writer.write(LocalDateTime.now() + " INFO: " + infoMessage + System.lineSeparator());
            System.out.println("INFO: " + infoMessage);
        } catch (IOException e) {
            System.err.println("Error writing to info log file: " + e.getMessage());
        }
    }

    // Log error messages
    private static void logError(String errorMessage) {
        try (FileWriter writer = new FileWriter("twilio_errors.log", true)) {
            writer.write(LocalDateTime.now() + " ERROR: " + errorMessage + System.lineSeparator());
            System.err.println("ERROR: " + errorMessage);
        } catch (IOException e) {
            System.err.println("Error writing to error log file: " + e.getMessage());
        }
    }
}
