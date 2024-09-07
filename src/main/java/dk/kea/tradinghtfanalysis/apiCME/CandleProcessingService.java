package dk.kea.tradinghtfanalysis.apiCME;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import dk.kea.tradinghtfanalysis.model.Candle;
import dk.kea.tradinghtfanalysis.repository.CandleRepository;

import org.springframework.scheduling.annotation.Scheduled;

@Service
public class CandleProcessingService {


    private final CandleRepository candleRepository;
    private final NewCandleService newCandleService;
    private final ProcessMarketDataService processMarketDataService;

    @Autowired
    public CandleProcessingService(CandleRepository candleRepository, NewCandleService newCandleService, ProcessMarketDataService processMarketDataService) {
        this.candleRepository = candleRepository;
        this.newCandleService = newCandleService;
        this.processMarketDataService = processMarketDataService;
    }


    // Table of Content
        // 1. Process the TOB data from the WebSocket
        // 2. Create a new candle every 5 minutes


    public void update(String jsonData) {

        newCandleService.streamPrice(processMarketDataService.processMarketData(jsonData));

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

}

