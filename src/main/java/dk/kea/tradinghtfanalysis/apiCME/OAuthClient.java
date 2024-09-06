package dk.kea.tradinghtfanalysis.apiCME;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
@Component
public class OAuthClient {

    private static final String TOKEN_URL = "https://authnr.cmegroup.com/as/token.oauth2"; // Testing/Certification URL (not Prod)
    private static final String CLIENT_ID = System.getenv("CLIENT_API_ID");
    private static final String CLIENT_SECRET = System.getenv("CLIENT_API_ID_SECRET");

    public static String getToken() {
        RestTemplate restTemplate = new RestTemplate();

        // Create the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        // Create the body for the token request
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        // Wrap the headers and body into an HttpEntity
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        System.out.println("Sending POST request to get token...");

        try {
            // Send the request to get the token
            ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Extract the token from the response
                System.out.println("Token request successful, processing response...");
                String token = extractTokenFromResponse(response.getBody()); // Assuming a method to parse the response
                System.out.println("Token received: " + token);
                return token;
            } else {
                System.err.println("Error: Received non-OK HTTP status: " + response.getStatusCode());
                throw new RuntimeException("Failed to get the token: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while requesting token: " + e.getMessage());
            throw new RuntimeException("Error requesting token", e);
        }
    }

    // Dummy method to simulate token extraction from response, replace with actual JSON parsing logic
    private static String extractTokenFromResponse(String responseBody) {
        // Simulating extraction
        return "dummy_token"; // Replace with actual token extraction logic
    }
}
