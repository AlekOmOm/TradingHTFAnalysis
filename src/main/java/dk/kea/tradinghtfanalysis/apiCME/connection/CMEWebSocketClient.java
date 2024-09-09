package dk.kea.tradinghtfanalysis.apiCME.connection;

import dk.kea.tradinghtfanalysis.apiCME.dataProcessing.APIProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class CMEWebSocketClient {

    private static final String CME_API_URL = "wss://markets.api.cmegroup.com/marketdatastream/v1"; // CME WebSocket API URL
    private static String token;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private APIProcessingService APIProcessingService; // Assuming you have a service to handle candle data

    public void connect() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        token = tokenManager.getToken();

        if (token != null) {
            System.out.println("Token obtained successfully: " + token);
        } else {
            System.err.println("Failed to retrieve token. Aborting WebSocket connection.");
            return;
        }

        // Add the Authorization header with the Bearer token
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        System.out.println("Attempting to connect to WebSocket...");

        try {
            // Create a WebSocket connection manager
            WebSocketConnectionManager manager = new WebSocketConnectionManager(
                    client, new CMEWebSocketHandler(), CME_API_URL
            );
            manager.setHeaders(headers);
            manager.start();
            System.out.println("WebSocket connection initiated.");
        } catch (Exception e) {
            System.err.println("Error occurred while connecting to WebSocket: " + e.getMessage());
        }
    }

    // Inner WebSocket handler to manage incoming messages
    public class CMEWebSocketHandler extends TextWebSocketHandler {

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println("Connection established. Sending subscription request for NQ...");

            // Send a subscription request for NQ
            String subscriptionRequest = "{ \"messageType\": \"Subscribe\", \"product\": \"NQ\" }";
            session.sendMessage(new TextMessage(subscriptionRequest));
        }

        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            String payload = message.getPayload();
            System.out.println("Received: " + payload);

            // Parse the incoming message and process it into 5m OHLC candles
            APIProcessingService.update(payload);
        }
    }
}
