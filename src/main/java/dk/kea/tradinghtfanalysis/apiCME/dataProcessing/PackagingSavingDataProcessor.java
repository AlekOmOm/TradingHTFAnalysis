package dk.kea.tradinghtfanalysis.apiCME.dataProcessing;

import dk.kea.tradinghtfanalysis.model.Candle;
import dk.kea.tradinghtfanalysis.model.TimeFrame;
import dk.kea.tradinghtfanalysis.repository.CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class PackagingSavingDataProcessor {

    // Dependencies
    private final CandleRepository candleRepository;

    @Autowired
    public PackagingSavingDataProcessor(CandleRepository candleRepository) {
        this.candleRepository = candleRepository;
    }

    // Fields
    private Candle current5mCandle;
    private Candle prior5mCandle;
    private Candle current1mCandle;

    private List<Candle> five1mCandles = new ArrayList<>(5);

    private long initialTimeFrame = 0;
    private LocalDateTime previousDateTime;
    private double previousPrice;

    private LocalDateTime dateTime;
    private double price;

    private Map<LocalDateTime, Double> timeAndPrice = new LinkedHashMap<>();

    private static int counter = 0;


    // Main Methods
    public void streamData(Map<LocalDateTime, Double> timeAndPriceMap) {
        updateTimeAndPrice(timeAndPriceMap);

        if (previousDateTime != null) {

            if (counter == 1) {
                initialTimeFrame = java.time.Duration.between(previousDateTime, dateTime).toMinutes();
            }

            if (initialTimeFrame == 1) {
                updateCurrent1mCandle();

            } else if (initialTimeFrame == 5) {
                updateCurrent5mCandle();

            } else {
                System.out.println("Unsupported time frame: " + initialTimeFrame + " minutes.");
            }
        }



        previousDateTime = dateTime;
        previousPrice = price;
        counter++;
    }

    private void updateTimeAndPrice(Map<LocalDateTime, Double> timeAndPriceMap) {
        this.dateTime = timeAndPriceMap.keySet().iterator().next();
        this.price = timeAndPriceMap.get(dateTime);  // Update the price

        if (isAt1mMark()) {
            this.timeAndPrice.put(dateTime, price);
        }
    }

    // 5m Candle Creation and Aggregation

    private void updateCurrent5mCandle() {
        if (current5mCandle == null) {
            startNew5mCandle();
        }

        if (isAt5mMark()) {
            current5mCandle.setClose(price);
            saveAndReset5mCandle();
            startNew5mCandle();
        } else {
            updateCandlePriceData(price);
        }
    }

    private void startNew5mCandle() {
        current5mCandle = new Candle();
        current5mCandle.setTimeFrame(TimeFrame.FIVE_MINUTES);
        current5mCandle.setDateTime(dateTime);
        current5mCandle.setOpen(price);
        current5mCandle.setHigh(price);
        current5mCandle.setLow(price);
    }

    private void saveAndReset5mCandle() {
        candleRepository.save(current5mCandle);
        current5mCandle = null;
    }


    private void createNew5mCandle() {
        if (five1mCandles.size() == 5) {
            aggregateAndSave5mCandle();
        }
    }

    private void aggregateAndSave5mCandle() {
        double open = five1mCandles.get(0).getOpen();
        double close = five1mCandles.get(4).getClose();
        double high = five1mCandles.stream().mapToDouble(Candle::getHigh).max().orElse(0.0);
        double low = five1mCandles.stream().mapToDouble(Candle::getLow).min().orElse(0.0);

        current5mCandle = new Candle();
        current5mCandle.setDateTime(five1mCandles.get(0).getDateTime());  // Set start of the interval
        current5mCandle.setOpen(open);
        current5mCandle.setClose(close);
        current5mCandle.setHigh(high);
        current5mCandle.setLow(low);

        candleRepository.save(current5mCandle);
        prior5mCandle = current5mCandle;

        five1mCandles.clear();
    }

    // 1m Candle Management

    private void updateCurrent1mCandle() {
        // 3 scenarios:
        //      1) don't save (system just started up, waiting for 5m mark),
        //      2) save
        //          2a) 1m mark -> candle full -> save and reset
        //          2b) not 1m mark -> update candle price data

        if (current1mCandle == null && isAt5mMark()) { // -> 1) don't save
            startNew1mCandle();
        }

        if (current1mCandle != null) { //   -> 2) save
            if (isAt1mMark()) {         // 2a) 1m mark -> candle full -> save and reset
                current1mCandle.setClose(price);
                saveAndReset1mCandle();
                startNew1mCandle();
            } else {
                updateCandlePriceData(price);  // 2b) not 1m mark -> update candle price data
            }
        }
    }

    private void startNew1mCandle() {
        current1mCandle = new Candle();
        current1mCandle.setTimeFrame(TimeFrame.ONE_MINUTE);
        current1mCandle.setDateTime(dateTime);
        current1mCandle.setOpen(price);
        current1mCandle.setHigh(price);
        current1mCandle.setLow(price);
    }

    private void saveAndReset1mCandle() {
        five1mCandles.add(current1mCandle);
        if (five1mCandles.size() == 5) {
            createNew5mCandle();  // Create a new 5-minute candle when we have 5 one-minute candles
        }
        current1mCandle = null;  // Reset for the next minute
    }

    private void updateCandlePriceData(double price) {
        current1mCandle.setHigh(Math.max(current1mCandle.getHigh(), price));
        current1mCandle.setLow(Math.min(current1mCandle.getLow(), price));
        current1mCandle.setClose(price);
    }



    // Helper Methods
    private boolean isAt5mMark() {
        LocalTime time = dateTime.toLocalTime();
        return time.getSecond() == 0 && time.getMinute() % 5 == 0;
    }

    private boolean isAt1mMark() {
        return dateTime.getSecond() == 0 && (current1mCandle == null || dateTime.getMinute() != current1mCandle.getDateTime().getMinute());
    }

    private TimeFrame determineTimeFrame(Map<LocalDateTime, Double> timeAndPriceMap) {
        List<LocalDateTime> times = new ArrayList<>(timeAndPriceMap.keySet());
        if (times.size() < 2) {
            throw new IllegalArgumentException("Not enough data points to determine time frame.");
        }
        LocalDateTime first = times.get(0);
        LocalDateTime second = times.get(1);
        long minutesBetween = java.time.Duration.between(first, second).toMinutes();

        if (minutesBetween == 1) {
            return TimeFrame.ONE_MINUTE;
        } else if (minutesBetween == 5) {
            return TimeFrame.FIVE_MINUTES;
        } else {
            throw new IllegalArgumentException("Unsupported time frame: " + minutesBetween + " minutes.");
        }
    }

}
