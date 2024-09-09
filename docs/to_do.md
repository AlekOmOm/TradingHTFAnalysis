
CandleProcessingService
observations:
- now all 5m are saved
- except the very first candle
- and all of the price variables are saved with the close price,
- and the date and time is set to the candle prior


- [ ] init with Real Chart Data (5m candles)
  - [ ] resolve issue with not saving all 5m candles, properly in PackagingSavingDataProcessor
    - [ ] isAt5mMark Method: Ensure that this method correctly identifies the 5-minute marks.
    - [ ] createNew5mCandle Method: Verify that this method is called correctly and that it aggregates the 1-minute candles properly.
    - [ ] updateCurrent1mCandle Method: Ensure that this method correctly updates and saves the 1-minute candles.
  - [] check if it is because data is in 5m candles and not 1m candles or lower

      =======================
- [x] insure 5m candle start at 5m marks (00, 05, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)
- [x] insure Date is saved to DB 
- [x] add tests for CandleProcessingService


- [x] Clean up and Simplify CandleProcessingService (NewCandleService, ProcessMarketDataService, InitAPIData, TradingHtfAnalysisApplicationTests)

