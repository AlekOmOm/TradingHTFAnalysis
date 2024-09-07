package dk.kea.tradinghtfanalysis.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Candle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date; // date of the candle, yyyy-mm-dd
    @Column
    private LocalTime time; // time of the candle, hh:mm:ss

    private TimeFrame timeFrame = TimeFrame.FIVE_MINUTES; // always 5 minutes for DB storage, and aggregate to HTFs in the service layer

    private double open;
    private double high;
    private double low;
    private double close;






    // ------------------ Getters and Setters ------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.date = dateTime.toLocalDate();
        this.time = dateTime.toLocalTime();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

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
