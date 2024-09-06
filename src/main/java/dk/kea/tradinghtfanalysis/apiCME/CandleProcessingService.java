package dk.kea.tradinghtfanalysis.apiCME;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalTime;
import dk.kea.tradinghtfanalysis.model.Candle;
import dk.kea.tradinghtfanalysis.repository.CandleRepository;

import org.springframework.scheduling.annotation.Scheduled;

@Service
public class CandleProcessingService {

    private LocalTime currentCandleStartTime;
    private Candle currentCandle;
    private Candle priorCandle;
    private double lastPrice; // Track the last TOB price received
    private double priceAtZeroSecond; // Track the last price received at the 00 second mark

    @Autowired
    private CandleRepository candleRepository;

    // This method will run every 5 minutes
    @Scheduled(cron = "2 0/5 * * * *") // Run 2 seconds after the start of each 5-minute interval
    public void createNewCandle() {

        // Use the price that was last seen at the 00 second mark as the close price
        if (currentCandle != null) {
            currentCandle.setClose(priceAtZeroSecond); // Use priceAtZeroSecond for close
            priorCandle = currentCandle;
            candleRepository.save(priorCandle);
            System.out.println("Saved new candle: " + currentCandle);
        }

        // Start a new candle for the next 5-minute interval
        currentCandleStartTime = LocalTime.now().withSecond(0); // Set to the start time of the interval
        currentCandle = new Candle();
        currentCandle.setTime(currentCandleStartTime);


        // Handle first candle creation when there's no prior candle
        if (priorCandle != null) {
            currentCandle.setOpen(priorCandle.getClose()); // Use prior candle close for open
        } else {
            // If it's the very first candle, we'll set the open with the first TOB price received
            currentCandle.setOpen(0.0); // Initialize open to be updated later with the first price
        }
        System.out.println("New candle created for interval starting at: " + currentCandleStartTime);
    }

    // Process the TOB data from the WebSocket
    public void processMarketData(String jsonData) {
        // Parse the TOB (Top of Book) data from the JSON message
        double price = parsePriceFromJson(jsonData);
        lastPrice = price; // Track the last price received

        // If the price comes in at the 00 second mark, capture it
        LocalTime now = LocalTime.now();

        if (now.getSecond() == 0) {
            priceAtZeroSecond = price;
        }

        // Update the current candle with the incoming market data
        if (currentCandle != null) {
            if (currentCandle.getOpen() == 0.0) {
                currentCandle.setOpen(price); // Set the open price with the first price of the interval
            }
            if (price > currentCandle.getHigh()) {
                currentCandle.setHigh(price);
            }
            if (price < currentCandle.getLow()) {
                currentCandle.setLow(price);
            }
            currentCandle.setClose(price); // Continuously update the close price with the latest TOB price
        }
    }

    // https://cmegroupclientsite.atlassian.net/wiki/spaces/EPICSANDBOX/pages/46475123/CME+Smart+Stream+Websockets+-+Top+of+Book
    /*
        TOB Message Example:
        {
            "header": {
                "messageType": "TOB",
                "sentTime": "2023-11-28T16:06:36.948000000Z",
                "sequenceNumber": "728",
                "version": "1.0"
            },
            "payload": [
                {
                    "instrument": {
                        "definitionSource": "E",
                        "exchangeMic": "XNYM",
                        "id": "735995",
                        "marketSeqmentId": "80",
                        "periodCode": "",
                        "productCode": "CL",
                        "productGroup": "CL",
                        "productType": "FUT",
                        "symbol": "CLG34"
                    },
                    "askLevel": [],
                    "bidLevel": []
                }
            ]
        }
     */

    private double parsePriceFromJson(String jsonData) {
        // Implement this method to extract the price from the incoming TOB data
        // This is just a placeholder example
        return 0.0; // Replace with actual price extraction logic
    }
}

