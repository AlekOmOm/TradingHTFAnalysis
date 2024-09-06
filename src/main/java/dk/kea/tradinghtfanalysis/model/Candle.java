package dk.kea.tradinghtfanalysis.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Candle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private LocalTime time;

    private TimeFrame timeFrame = TimeFrame.FIVE_MINUTES; // always 5 minutes for DB storage, and aggregate to HTFs in the service layer

    private double open;
    private double high;
    private double low;
    private double close;






    // ------------------ Getters and Setters ------------------

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    // toString method:
    @Override
    public String toString() {
        return "Candle{" +
                "time=" + time +
                ", timeFrame=" + timeFrame +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                '}';
    }

}
