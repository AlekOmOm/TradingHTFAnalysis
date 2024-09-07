package dk.kea.tradinghtfanalysis;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.kea.tradinghtfanalysis.apiCME.ProcessMarketDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProcessMarketDataServiceTest {

    @InjectMocks
    private ProcessMarketDataService processMarketDataService;

    private final String sampleJson = "{\n" +
            "  \"messageType\": \"TOB\",\n" +
            "  \"payload\": {\n" +
            "    \"askLevel\": [\n" +
            "      {\n" +
            "        \"lastUpdateTime\": \"2023-01-01T12:00:00Z\",\n" +
            "        \"price\": \"100.50\",\n" +
            "        \"qty\": \"10\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"bidLevel\": [\n" +
            "      {\n" +
            "        \"lastUpdateTime\": \"2023-01-01T12:00:00Z\",\n" +
            "        \"price\": \"100.45\",\n" +
            "        \"qty\": \"15\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize the mocks
    }

    @Test
    void testProcessMarketData_withValidData() {
        // Call the method to test
        Map<LocalDateTime, Double> result = processMarketDataService.processMarketData(sampleJson);

        // Verify the returned map contains the expected values
        assertFalse(result.isEmpty());
        LocalDateTime expectedTime = LocalDateTime.parse("2023-01-01T12:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
        assertEquals(100.50, result.get(expectedTime));
    }

    @Test
    void testProcessMarketData_withInvalidData() {
        // Test with invalid JSON data
        String invalidJson = "{ \"invalid\": \"data\" }";
        Map<LocalDateTime, Double> result = processMarketDataService.processMarketData(invalidJson);

        // Verify that an empty map is returned
        assertTrue(result.isEmpty());
    }

    @Test
    void testProcessMarketData_withEmptyData() {
        // Test with empty JSON string
        Map<LocalDateTime, Double> result = processMarketDataService.processMarketData("");

        // Verify that an empty map is returned
        assertTrue(result.isEmpty());
    }
}
