package dk.kea.tradinghtfanalysis.apiCME.connection;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenManager {
    private String token;
    private Instant expiryTime;

    public String getToken() {
        if (token == null || Instant.now().isAfter(expiryTime)) {
            System.out.println("Token expired or not available. Requesting a new one...");
            token = requestNewToken();
            expiryTime = Instant.now().plusSeconds(1799); // Adjust according to the 'expires_in' field
            System.out.println("New token obtained. Valid for 1799 seconds.");
        } else {
            System.out.println("Reusing existing token. Token still valid.");
        }
        return token;
    }

    private String requestNewToken() {
        System.out.println("Requesting new token from OAuthClient...");
        return OAuthClient.getToken();
    }
}
