# Use Cases for Trading High Time Frame Analysis System (MVP)

## 1. Ingest Real-Time Data (Requirement 1)

### Use Case Name: Ingest Real-Time 5-Minute Candle Data
- **Description**: The system retrieves and stores real-time 5-minute OHLC candle data from an external API.
- **Actors**: External API, System
- **Preconditions**:
    - The API is available and provides real-time data.
    - The database is available to store the candle data.
- **Postconditions**:
    - The 5-minute OHLC data is saved to the database with the correct timestamp.
- **Main Flow**:
    1. System requests 5-minute candle data from the API.
    2. API responds with the 5-minute OHLC data.
    3. System saves the data to the database, including the timestamp and volume.

## 2. Aggregate Candles into Higher Time Frames (Requirement 5)

### Use Case Name: Aggregate Data into Higher Time Frames
- **Description**: The system dynamically aggregates 5-minute candle data to create 6-hour, daily, and weekly OHLC candles.
- **Actors**: System
- **Preconditions**:
    - The system has 5-minute candle data in the database.
    - A query or request for a higher time frame (6-hour, daily, weekly) is made.
- **Postconditions**:
    - Aggregated OHLC data is computed and ready for display or further analysis.
- **Main Flow**:
    1. System receives a request to generate a higher time frame candle (e.g., 6-hour).
    2. The system retrieves the relevant 5-minute candle data from the database.
    3. The system aggregates the retrieved data into the requested time frame.
    4. The aggregated OHLC candle is returned or stored as necessary.

## 3. Display IPDA Interval Analysis (Requirement 3)

### Use Case Name: Display IPDA Interval Data
- **Description**: The system displays OHLC data for the last 5, 20, and 40 intervals for each HTF (6-hour, daily, weekly).
- **Actors**: User, System
- **Preconditions**:
    - Aggregated OHLC data for the HTF is available.
    - The system can calculate intervals from the available data.
- **Postconditions**:
    - The user sees the OHLC data for the requested IPDA intervals.
- **Main Flow**:
    1. User selects an HTF (e.g., 6-hour) and an interval (5, 20, or 40).
    2. The system calculates the OHLC data for the selected number of intervals.
    3. The system displays the results to the user in the GUI.

## 4. GUI Interaction for IPDA Information (Requirement 4)

### Use Case Name: Interact with GUI to Display IPDA Analysis
- **Description**: The user interacts with the GUI to display OHLC data for selected IPDA intervals.
- **Actors**: User, System
- **Preconditions**:
    - The GUI is functional and connected to the data retrieval system.
    - The necessary OHLC data is stored in the database.
- **Postconditions**:
    - The user successfully views OHLC data based on their input in the GUI.
- **Main Flow**:
    1. User opens the GUI and selects an HTF (6-hour, daily, weekly).
    2. User selects an IPDA interval (5, 20, or 40).
    3. The system retrieves and displays the appropriate OHLC data.

## 5. Ensure Database Management for Candle Storage (Requirement 5)

### Use Case Name: Manage Database for Storing Candle Data
- **Description**: The system manages the database to ensure efficient storage of 5-minute candle data and facilitates retrieval for aggregating higher time frames.
- **Actors**: System
- **Preconditions**:
    - The database is properly configured to store OHLC data.
    - Data is being ingested and stored in real time.
- **Postconditions**:
    - Candle data is stored efficiently and can be retrieved as needed for calculations.
- **Main Flow**:
    1. The system stores 5-minute OHLC candle data in the database.
    2. Data can be efficiently queried for aggregation into higher time frames.
