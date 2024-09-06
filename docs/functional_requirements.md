# Functional Requirements for Trading High Time Frame Analysis System (MVP) only NDX/USD

## 1. Real-Time Data Ingestion
- Retrieve and store real-time 5-minute candle data from API.
- Save OHLC data along with the timestamp in the database.

## 2. Data Aggregation for Higher Time Frames (HTF)
- Aggregate 5-minute candles into **6-hour**, **daily**, and **weekly** time frames dynamically.
- Store or calculate HTF data (OHLC) based on 5-minute data on-the-fly when queried.

## 3. IPDA Interval Analysis (GUI-based)
- Display OHLC data for the last **5**, **20**, and **40 intervals** for each HTF:
    - **6-Hour Time Frame**: Show the OHLC of the last 5, 20, 40 6-hour candles.
    - **Daily Time Frame**: Show the OHLC of the last 5, 20, 40 daily candles.
    - **Weekly Time Frame**: Show the OHLC of the last 5, 20, 40 weekly candles.

## 4. GUI for IPDA Information Display
- User interface that shows the calculated OHLC data for the last 5, 20, 40 intervals of the selected time frame (6-hour, daily, weekly).
- The interface will be minimal, displaying the core IPDA data.

## 5. Database Management
- Store only 5-minute candle data in the database.
- Allow aggregation of stored 5-minute data to generate higher time frames (6h, daily, weekly) as needed.

## 6. Future Extendibility
- Keep the system flexible for future enhancements, such as adding more analysis features or additional time frames.
