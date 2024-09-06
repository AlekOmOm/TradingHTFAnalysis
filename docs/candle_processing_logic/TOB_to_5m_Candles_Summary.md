
# Summary: Processing Streaming Real-Time Market Data (5s TOB) into 5-Minute Candles

## Objective:
The goal is to convert real-time streaming Top of Book (TOB) data, which is updated every 5 seconds, into accurate 5-minute OHLC (Open, High, Low, Close) candles for storage in a database.

## Approach:

### 1. **WebSocket Data (TOB Updates)**:
- The WebSocket provides Top of Book (TOB) updates every 500 milliseconds (or twice per second).
- Each TOB message contains the current best bid and offer prices, and for the purpose of candles, we focus on the **price** from these updates.

### 2. **Handling 5-Minute Intervals**:
To handle the conversion of this data into 5-minute OHLC candles:
- **Open**: The first TOB price received at the start of each 5-minute interval.
- **High**: The highest TOB price observed during the 5-minute interval.
- **Low**: The lowest TOB price observed during the 5-minute interval.
- **Close**: The last TOB price observed at the exact end of the 5-minute interval (i.e., at the 00 second mark of the next interval).

### 3. **Using a Scheduled Task**:
- A **Scheduled Task** is used to create and finalize the candles.
- This task runs every 5 minutes using the cron expression `@Scheduled(cron = "2 0/5 * * * *")`.
- The task is triggered 2 seconds after each 5-minute mark (e.g., 00:05:02, 00:10:02) to allow for any delays in processing the last TOB message.

### 4. **Tracking the Price at the 00 Second Mark**:
- During the `processMarketData()` method (which handles incoming TOB updates):
    - Each TOB price is evaluated to check if it arrives at the `00` second mark.
    - The last TOB price received at the `00` second mark is stored as the **priceAtZeroSecond**.
    - This price is used as the **close** price for the current 5-minute candle.

### 5. **Finalizing Candles**:
- When the scheduled task runs, the current candle is finalized by:
    - Using the **priceAtZeroSecond** as the **close** price.
    - The candle is then saved to the database.
- A new candle is created for the next 5-minute interval, and the process continues.

### 6. **Candle Initialization and Updates**:
- As each 5-minute interval starts, a new candle is created.
- The first TOB price received after the candle is created becomes the **open** price.
- The **high** and **low** prices are continuously updated with each new TOB price within the interval.
- The **close** price is continuously updated with each new TOB message but is finalized using the **priceAtZeroSecond**.

## Summary of Flow:
1. **TOB updates** are continuously received via the WebSocket and processed in real-time.
2. **Candle creation and finalization** are handled by the scheduled task at 5-minute intervals.
3. The scheduled task uses the **priceAtZeroSecond** for accurate candle close prices.
4. The OHLC values (Open, High, Low, Close) are tracked throughout the 5-minute window for each candle.

This approach ensures that each 5-minute candle accurately reflects the market data based on TOB updates.
