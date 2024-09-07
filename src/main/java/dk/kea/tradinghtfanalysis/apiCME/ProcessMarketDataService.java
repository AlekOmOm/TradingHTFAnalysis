package dk.kea.tradinghtfanalysis.apiCME;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

@Service
public class ProcessMarketDataService {

    LocalDateTime startTime;

    // Parse the TOB data from the WebSocket
    public Map<LocalDateTime, Double> processMarketData(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode payload = rootNode.get("payload");

            // Extract the price (e.g., from askLevel)
            JsonNode askLevel = payload.get("askLevel").get(0);
            if (askLevel == null || askLevel.get("price") == null || askLevel.get("lastUpdateTime") == null) {
                System.err.println("Invalid TOB data: Missing price or timestamp.");
                return Collections.emptyMap(); // Return empty map if data is incomplete
            }

            double price = askLevel.get("price").asDouble();
            String timestampStr = askLevel.get("lastUpdateTime").asText();
            LocalDateTime time = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_DATE_TIME);


            messageScheduled(time, price);
            return Map.of(time, price); // Time & Price

        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
            return Collections.emptyMap(); // Return empty map instead of null
        }
    }


    private void messageScheduled(LocalDateTime time, double price){
        if (startTime == null && time.getSecond()==0) {
            startTime = time;
        }
        // every 5m time
        if (startTime != null) {
            if (time.isAfter(startTime.plusMinutes(5))) {
                System.out.println("5m time reached");
                startTime = time;
                System.out.println("Price: " + price + ", Time: " + time);
            }
        }
    }

}
