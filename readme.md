# Trading High Time Frame Analysis System (MVP)

### Purpose

- pre-market **_Developing_ Daily Bias**
  - based on _ICT_ concepts 
    - Institutional Order Flow
    - Market Structure
    - Inefficiencies and Liquidity

    
- **_HTF_ analysis for _NDX/USD_**   _(RTH only)_   
  - 6-hour, daily, weekly
  - _only_ NDX/USD, _only_ RTH   (later S&P 500 for HTF SMT analysis)


- **designed to provide** traders: 
  - a _simple and clear_ fundament for _developing their Daily Bias_:
    - analyzing market structure in order to identify Institutional Order FLow
    - -> thus, increasing the probability of successfully identifying the Daily Order Flow 

### Utility

- Core Features:
  - Real-Time Data Ingestion
  - Data Aggregation
  - Institutional Order Flow Analysis
    - analysis for Developing Daily Bias (based on ICT concepts)
  - Efficient Database Management


- User Interface: 
  - Provides a user-friendly GUI for displaying analysis results.


- Future-Proof Design: 
  - Emphasizes future extensibility, modularity, and scalability to incorporate additional ICT (Inner Circle Trader) analysis concepts later on.


### Technology Stack


- The project is built using the following technologies:
  - Java: The main programming language used for developing the system.
  - Spring Boot: The framework used for creating stand-alone, production-grade Spring based applications.
  - Maven: The build automation tool used for managing project builds and dependencies.
  - MySQL DB: The relational database used for storing candle data (in 5m OHLC format).
  - JPA/Hibernate: The Java Persistence API used for managing relational data in Java applications.


### Table of Contents

- [Functional Requirements](docs/functional_requirements.md)
- [Use Cases](docs/use_cases.md)
- [System Architecture](docs/system_architecture.md)