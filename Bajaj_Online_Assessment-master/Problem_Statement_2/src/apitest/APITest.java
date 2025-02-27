package apitest;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class APITest {

    private static final String API_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus/create/user";

    public static void main(String[] args) {
        // Example test cases
        testCreateUser("1", "Test", "User", "9999999999", "test.user@test.com"); // Valid case
        testCreateUser("2", "John", "Doe", "9999999999", "john.doe@test.com");   // Duplicate phone number
        testCreateUser(null, "Alice", "Smith", "8888888888", "alice.smith@test.com"); // Missing roll-number
        testCreateUser("3", "Bob", "Johnson", null, "bob.johnson@test.com");      // Missing phone number
        testCreateUser("4", "Charlie", "Brown", "7777777777", null);             // Missing email ID
    }

    private static void testCreateUser(String rollNumber, String firstName, String lastName, String phoneNumber, String emailId) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            if (rollNumber != null) {
                conn.setRequestProperty("roll-number", rollNumber);
            }
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"phoneNumber\": \"%s\", \"emailId\": \"%s\"}",
                firstName, lastName, phoneNumber != null ? phoneNumber : "null", emailId != null ? emailId : "null"
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Response Code: " + code);
            if (code == HttpURLConnection.HTTP_OK) {
                System.out.println("Success: " + conn.getResponseMessage());
            } else {
                System.out.println("Error: " + conn.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
