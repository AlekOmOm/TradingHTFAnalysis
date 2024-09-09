package dk.kea.tradinghtfanalysis.apiCME;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.kea.tradinghtfanalysis.apiCME.connection.CMEWebSocketClient;
import dk.kea.tradinghtfanalysis.apiCME.dataProcessing.APIProcessingService;
import dk.kea.tradinghtfanalysis.apiCME.dataProcessing.JSONDataProcessor;
import dk.kea.tradinghtfanalysis.model.Candle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class InitAPIData implements CommandLineRunner {

    @Autowired
    public APIProcessingService APIProcessingService;
    @Autowired
    private CMEWebSocketClient cmeWebSocketClient;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing API Data...");
        System.out.println(" connecting to WebSocket...");
        cmeWebSocketClient.connect();

//
//        loadCandleData();
//        // streamTestData(60, 1000, 0.25, 1.0, 60); // 60 data points, 1 minute interval(60s)
//
//        testRepo();
    }



    // ------------------ Dependencies ------------------
    @Autowired
    JSONDataProcessor JSONDataProcessor = new JSONDataProcessor();

    // API Data, every second the price will be updated
    Map<String, String> data = new LinkedHashMap<>(); // Changed to LinkedHashMap

    String lastUpdateTime;
    String price;

    private static final DateTimeFormatter CUSTOM_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Sys env variable
    private static final String GITHUB_LOCAL_REPO_PATH = System.getenv("GITHUB_LOCAL_REPO_PATH");
    private static final String CHART_DATA_PATH = System.getenv("CHART_DATA_JSON_PATH");

    String tradingVaultPath = System.getenv("TRADING_VAULT_PATH");




    // ------------------ Main Methods ------------------

    public void streamTestData(int dataPoints, int startPrice, double tickValue, double volatility, int secondInterval) {
        initNQData(dataPoints, startPrice, tickValue, volatility, secondInterval);

        for (Map.Entry<String, String> entry : data.entrySet()) {
            lastUpdateTime = entry.getKey();
            price = entry.getValue();
            String json = getSampleJson(lastUpdateTime, price);
            APIProcessingService.update(json);
        }
    }

    private void testRepo() {

        List<Candle> candles = APIProcessingService.getCandleRepository().findAll();
        assert (candles.size() > 0);

        System.out.println("Candles in the repository: " + candles.size());
        System.out.println(" first candle saved: " + candles.get(0));
        System.out.println(" last candle saved: " + candles.get(candles.size() - 1));
    }


    // ------------------ Data Generation Methods ------------------

    public void loadCandleData() {
        try {
            Map<String, String> dataLinked = new LinkedHashMap<>(); // Use LinkedHashMap here too

            // Create an object mapper
            ObjectMapper mapper = new ObjectMapper();

            // Read the JSON file into a JsonNode
            JsonNode rootNode = mapper.readTree(new File(CHART_DATA_PATH));

            Candle candle;

            // Iterate over each candle object in the JSON array
            for (JsonNode jsonCandle : rootNode) {
                String datetime = jsonCandle.get("datetime").asText();
                candle = new Candle();
                candle.setOpen(jsonCandle.get("open").asDouble());
                candle.setHigh(jsonCandle.get("high").asDouble());
                candle.setLow(jsonCandle.get("low").asDouble());
                candle.setClose(jsonCandle.get("close").asDouble());
                candle.setDateTime(LocalDateTime.parse(datetime, CUSTOM_DATE_TIME_FORMATTER));

                APIProcessingService.getCandleRepository().save(candle);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                "        \"price\": \"" + price + "\",\n" +
                "        \"qty\": \"15\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    // ------------------ Data Initialization ------------------

    // NQ price ticks: 0.25
    private void initNQData(int dataPoints, int startPrice, double tickValue, double volatility, int secondInterval) {
        data.putAll(generateRandomPriceAtXIntervals(dataPoints, startPrice, tickValue, volatility, secondInterval));
    }

    private Map<String, String> generateRandomPriceAtXIntervals(int dataPoints, int startPrice, double tickValue, double volatility, int secondInterval) {
        Map<String, String> data = new LinkedHashMap<>(); // Use LinkedHashMap here too

        // start time
        LocalDateTime dateTime = LocalDateTime.of(2023, 11, 28, 10, 36, 00);
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
