package dk.kea.tradinghtfanalysis.apiCME.connection;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import dk.kea.tradinghtfanalysis.apiCME.dataProcessing.APIProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.net.URI;

@Component
public class CMEWebSocketClient {

    private static final String CME_API_URL = "wss://markets.api.cmegroup.com/marketdatastream/v1"; // CME WebSocket API URL
    private static String token;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private APIProcessingService APIProcessingService; // Assuming you have a service to handle candle data


    public void connect() {
        WebSocketClient client = new WebSocketClient();

        token = tokenManager.getToken();

        if (token != null) {
            System.out.println("Token obtained successfully: " + token);
        } else {
            System.err.println("Failed to retrieve token. Aborting WebSocket connection.");
            return;
        }

        System.out.println("Attempting to connect to WebSocket...");

        try {
            client.start();
            URI uri = new URI(CME_API_URL);
            System.out.println("Connecting to WebSocket...");

            // Create a ClientUpgradeRequest and add the token to the headers
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            request.setHeader("Authorization", "Bearer " + token);

            client.connect(new CMEWebSocketHandler(), uri, request).get();
            System.out.println("WebSocket connection initiated.");
        } catch (Exception e) {
            System.err.println("Error occurred while connecting to WebSocket: " + e.getMessage());
        }
    }

    // Inner WebSocket handler to manage incoming messages
    public class CMEWebSocketHandler extends WebSocketAdapter {

        @Override
        public void onWebSocketConnect(Session session) {
            super.onWebSocketConnect(session);
            System.out.println("Connection established. Sending subscription request for NQ...");

            // Send a subscription request for NQ
            String subscriptionRequest = "{ \"messageType\": \"Subscribe\", \"product\": \"NQ\" }";
            try {
                session.getRemote().sendString(subscriptionRequest);
            } catch (Exception e) {
                System.err.println("Error sending subscription request: " + e.getMessage());
            }
        }

        @Override
        public void onWebSocketText(String message) {
            System.out.println("Received: " + message);

            // Parse the incoming message and process it into 5m OHLC candles
            APIProcessingService.update(message);
        }
    }
}