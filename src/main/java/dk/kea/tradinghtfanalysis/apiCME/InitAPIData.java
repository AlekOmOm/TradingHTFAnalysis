package dk.kea.tradinghtfanalysis.apiCME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class InitAPIData implements CommandLineRunner {

    @Autowired
    private CandleProcessingService candleProcessingService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing API Data...");
        streamDataToTest();
    }

    // ------------------ Dependencies ------------------
    @Autowired
    ProcessMarketDataService processMarketDataService = new ProcessMarketDataService();

    // API Data, every second the price will be updated
    Map<String, String> data = new LinkedHashMap<>(); // Changed to LinkedHashMap

    String lastUpdateTime;
    String price;

    // ------------------ Main Methods ------------------
    private void streamDataToTest() {
        initNQData();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            lastUpdateTime = entry.getKey();
            price = entry.getValue();
            String json = getSampleJson(lastUpdateTime, price);
            candleProcessingService.update(json);
        }

    }


    // ------------------ Data Generation Methods ------------------

    private String getSampleJson(String lastUpdateTime, String price) {
        return "{\n" +
                "  \"messageType\": \"TOB\",\n" +
                "  \"payload\": {\n" +
                "    \"askLevel\": [\n" +
                "      {\n" +
                "        \"lastUpdateTime\": \"" + lastUpdateTime + "\",\n" +
                "        \"price\": \"" + price + "\",\n" +
                "        \"qty\": \"10\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"bidLevel\": [\n" +
                "      {\n" +
                "        \"lastUpdateTime\": \"" + lastUpdateTime + "\",\n" +
                "        \"price\": \"" + price + "0.25\",\n" +
                "        \"qty\": \"15\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    // ------------------ Data Initialization ------------------

    // NQ price ticks: 0.25
    private void initNQData() {
        data.putAll(generateRandomPriceAtXIntervals(100, 1000, 0.25, 1.0, 15));
    }

    private Map<String, String> generateRandomPriceAtXIntervals(int dataPoints, int startPrice, double tickValue, double volatility, int secondInterval) {
        Map<String, String> data = new LinkedHashMap<>(); // Use LinkedHashMap here too

        // start time
        LocalDateTime dateTime = LocalDateTime.of(2023, 11, 28, 10, 36, 30);
        double price = startPrice;

        for (int i = 0; i < dataPoints; i++) {
            String time = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
            price += (Math.random() - 0.5) * volatility; // Random price change within volatility
            data.put(time, String.valueOf(price));
            dateTime = dateTime.plusSeconds(secondInterval); // Increment time by secondInterval
        }
        return data;
    }
}
