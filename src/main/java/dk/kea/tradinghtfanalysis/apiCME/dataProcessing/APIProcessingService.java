package dk.kea.tradinghtfanalysis.apiCME.dataProcessing;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import dk.kea.tradinghtfanalysis.repository.CandleRepository;

@Service
public class APIProcessingService {


    private final CandleRepository candleRepository;
    private final PackagingSavingDataProcessor packagingSavingDataProcessor;
    private final JSONDataProcessor JSONDataProcessor;

    @Autowired
    public APIProcessingService(CandleRepository candleRepository, PackagingSavingDataProcessor packagingSavingDataProcessor, JSONDataProcessor JSONDataProcessor) {
        this.candleRepository = candleRepository;
        this.packagingSavingDataProcessor = packagingSavingDataProcessor;
        this.JSONDataProcessor = JSONDataProcessor;
    }


    // Table of Content
        // 1. Process the TOB data from the WebSocket
        // 2. Create a new candle every 5 minutes


    public void update(String jsonData) {

        packagingSavingDataProcessor.streamData(JSONDataProcessor.processJSONData(jsonData));

    }

    public CandleRepository getCandleRepository() {
        return candleRepository;
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

