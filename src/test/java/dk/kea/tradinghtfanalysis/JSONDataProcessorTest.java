package dk.kea.tradinghtfanalysis;
import dk.kea.tradinghtfanalysis.apiCME.dataProcessing.JSONDataProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JSONDataProcessorTest {

    @InjectMocks
    private JSONDataProcessor JSONDataProcessor;

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
    void testProcessJSONData_withValidData() {
        // Call the method to test
        Map<LocalDateTime, Double> result = JSONDataProcessor.processJSONData(sampleJson);

        // Verify the returned map contains the expected values
        assertFalse(result.isEmpty());
        LocalDateTime expectedTime = LocalDateTime.parse("2023-01-01T12:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
        assertEquals(100.50, result.get(expectedTime));
    }

    @Test
    void testProcessJSONData_withInvalidData() {
        // Test with invalid JSON data
        String invalidJson = "{ \"invalid\": \"data\" }";
        Map<LocalDateTime, Double> result = JSONDataProcessor.processJSONData(invalidJson);

        // Verify that an empty map is returned
        assertTrue(result.isEmpty());
    }

    @Test
    void testProcessJSONData_withEmptyData() {
        // Test with empty JSON string
        Map<LocalDateTime, Double> result = JSONDataProcessor.processJSONData("");

        // Verify that an empty map is returned
        assertTrue(result.isEmpty());
    }
}
