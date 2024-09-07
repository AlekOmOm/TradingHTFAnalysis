package dk.kea.tradinghtfanalysis.apiCME;

import dk.kea.tradinghtfanalysis.model.Candle;
import dk.kea.tradinghtfanalysis.repository.CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

@Service
public class NewCandleService {
    // ------------------ Dependencies ------------------
    private final CandleRepository candleRepository;
    @Autowired
    public NewCandleService(CandleRepository candleRepository) {
        this.candleRepository = candleRepository;
    }

    // ------------------ Purpose------------------
    /*
        - creation of new candles every 5 minutes
        - updating the current candle with the latest price
        - closing the current candle when the 5-minute interval is up
        - saving the closed candle to the database

        thus, methods:
        - streamPrice(double price)
        - createNewCandle() // scheduled to run every 5 minutes
        - updateCurrent1mCandle // scheduled to run every second
        - readCurrentCandle // checks to ensure complete candle data before closing
        - saveCandleToDB // aka. save and delete currentCandle to the database

     */


    // ------------------ Fields ------------------


        // 1. candles
    private Candle current5mCandle;
    private Candle prior5mCandle;
    private Candle current1mCandle;

    private List<Candle> five1mCandles = new ArrayList<>(5); // For 5-minute candles

        // 2. time tracking
    private LocalDateTime startTime;
    private LocalDateTime dateTime;
    private LocalDate date;
    private LocalTime time;

        // 3. price tracking
    private double price;

        // 4. Time and Price
    private Map<LocalDateTime, Double> timeAndPrice = new LinkedHashMap<>();
    private List<Map<LocalDateTime, Double>> timeAndPrice15mHistory = new ArrayList<>();



    // ------------------ Methods ------------------

    public void streamPrice(Map<LocalDateTime, Double> timeAndPrice) {

        updateTimeAndPriceTracking(timeAndPrice);
        messageScheduled(dateTime, price);

        updateCurrent1mCandle();
    }

    private void messageScheduled(LocalDateTime time, double price){

        if (startTime == null && time.getSecond()==0) {
            startTime = time;
        }
        // print candles every 1 and 5 minutes
        // every 1m time
        if (time.getSecond() == 0) {
            System.out.println(" 1m time reached");
            System.out.println("  - Current 1m Candle: " + current1mCandle);
        }

        // every 5m time
        if (startTime != null ) {
            if (time.isAfter(startTime.plusMinutes(5))) {
                System.out.println(" 5m time reached");
                startTime = time;
                System.out.println(" - Current 5m Candle: " + current5mCandle);
            }
        }
    }

    // ------------------ CRUD Methods ------------------

    // 1. Create

    public void createNew5mCandle() {

        if (five1mCandles.size() == 5) {
            aggregateAndUpdate5mCandle();
            saveAndClear();
        }
        
    }

    private void aggregateAndUpdate5mCandle() {
        // Aggregate the 5 one-minute candles into a 5-minute candle
        double open = five1mCandles.get(0).getOpen();
        double close = five1mCandles.get(4).getClose();
        double high = five1mCandles.stream().mapToDouble(Candle::getHigh).max().orElse(0.0);
        double low = five1mCandles.stream().mapToDouble(Candle::getLow).min().orElse(0.0);

        current5mCandle = new Candle();
        current5mCandle.setTime(five1mCandles.getFirst().getTime()); // Set the time to the start of the 5-minute interval
        current5mCandle.setOpen(open);
        current5mCandle.setClose(close);
        current5mCandle.setHigh(high);
        current5mCandle.setLow(low);
    }


    // 2. update

    private void updateCurrent1mCandle() {

        if (isValidMinute(dateTime)) {
            // Close the current candle and start a new one

            if (current1mCandle != null) {
                current1mCandle.setClose(price); // Finalize the last price as the close
                saveAndClear1mCandle();          // Save the candle and clear for the next minute
            }

            // Create a new candle for the new minute
            current1mCandle = new Candle();
            current1mCandle.setDateTime(dateTime);
            current1mCandle.setOpen(price);
            current1mCandle.setHigh(price);
            current1mCandle.setLow(price);
        } else {
            // Continue updating the current candle
            updateCandleHighLowClose(price);
        }
    }


    // 3. read (scheduled reading)



    
    // 4. delete (save and clear)
    
    private void saveAndClear() {
        // Save the 5-minute candle to the database
        candleRepository.save(current5mCandle);
        System.out.println("Saved new 5-minute candle: " + current5mCandle);

        // Clear the 1-minute candles for the next 5-minute interval
        prior5mCandle = current5mCandle;
        current5mCandle = null;
        five1mCandles.clear();
        saveAndClearTimeAndPrice();
    }

    private void saveAndClearTimeAndPrice() {
        // method for saving the 5m interval time and price data to history
            // timeAndPrice (1m entries), timeAndPrice5mInterval, timeAndPrice15mHistory

        Map<LocalDateTime, Double> timeAndPrice5mInterval = new HashMap<>();
        // get the 5-minute interval time and price data in 1m intervals

        timeAndPrice.keySet().forEach(time -> {


            if (time.isBefore(dateTime)) {

                // if 5m interval is complete, save to history and clear
                if (timeAndPrice5mInterval.size() == 5) {
                    if (timeAndPrice15mHistory.size() == 15) { // size 15m -> each Map is 1m
                        timeAndPrice15mHistory.remove(0); // remove the oldest entry
                    }
                    timeAndPrice15mHistory.add(timeAndPrice5mInterval);
                    timeAndPrice5mInterval.clear();
                }
                // if time is on the minute, add to 5m interval
                if (time.getSecond() == 0) {
                    timeAndPrice5mInterval.put(time, timeAndPrice.get(time));
                }
            }
        });


        timeAndPrice.clear();
    }


    private void saveAndClear1mCandle() {
        // save 1m candle to List
        if (five1mCandles.size() == 5) {
            createNew5mCandle();
        }

        five1mCandles.add(current1mCandle);

        // Clear the 1-minute candle for the next minute
        current1mCandle = null;
    }


    // ------------------ Helper Methods ------------------
    private void updateTimeAndPriceTracking(Map<LocalDateTime, Double> timeAndPrice) {
        System.out.println("DEBUG updateTimeAndPriceTracking: " + timeAndPrice);
        LocalDateTime time = timeAndPrice.keySet().iterator().next();

        this.dateTime = time;
        this.date = time.toLocalDate();
        this.time = time.toLocalTime();

        this.price = timeAndPrice.get(time); // get price at time from map

        if (newMinute(this.time)) {
            this.timeAndPrice.put(dateTime, price);
        }
    }


    private boolean newMinute(LocalTime time) {
        return time.getSecond() == 0 && (current1mCandle == null || time.getMinute() != current1mCandle.getTime().getMinute());
    }

    private boolean isValidMinute(LocalDateTime priceTimestamp) {
        // Compare the current system minute with the timestamp minute from the TOB data
        return current1mCandle == null || priceTimestamp.getMinute() != current1mCandle.getTime().getMinute();
    }

    private void updateCandleHighLowClose(double price) {
        if (price > current1mCandle.getHigh()) {
            current1mCandle.setHigh(price);
        }
        if (price < current1mCandle.getLow()) {
            current1mCandle.setLow(price);
        }
        current1mCandle.setClose(price); // Continuously update the close price
    }

}
