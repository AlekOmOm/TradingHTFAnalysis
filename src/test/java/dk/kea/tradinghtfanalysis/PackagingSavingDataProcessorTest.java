package dk.kea.tradinghtfanalysis;


import dk.kea.tradinghtfanalysis.apiCME.dataProcessing.APIProcessingService;
import dk.kea.tradinghtfanalysis.apiCME.InitAPIData;
import dk.kea.tradinghtfanalysis.apiCME.dataProcessing.PackagingSavingDataProcessor;
import dk.kea.tradinghtfanalysis.apiCME.dataProcessing.JSONDataProcessor;
import dk.kea.tradinghtfanalysis.repository.CandleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PackagingSavingDataProcessorTest {

    @InjectMocks
    private PackagingSavingDataProcessor packagingSavingDataProcessor;

    @Mock
    private CandleRepository candleRepository;

    private InitAPIData initAPIData;

    private JSONDataProcessor JSONDataProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        initAPIData = new InitAPIData();
        packagingSavingDataProcessor = new PackagingSavingDataProcessor(candleRepository);
        JSONDataProcessor = new JSONDataProcessor();
        initAPIData.APIProcessingService = new APIProcessingService(candleRepository, packagingSavingDataProcessor, JSONDataProcessor);
    }

//    @Test
//    void testDateAndTimeFlowFromAPIData() throws Exception {
//        // Generate and stream the test data
//        initAPIData.streamDataToTest(60, 1000, 0.25, 1.0, 60); // 60 data points, 1 minute interval(60s)
//
//
//        // Verify that the 5-minute candles are created and saved in the repository
//        verify(candleRepository, atLeastOnce()).save(any(Candle.class));
//
//        // Verify repo is not empty
//        List<Candle> candlez = candleRepository.findAll();
//        assertFalse(candlez.isEmpty());
//
//
//        Candle current5mCandle = newCandleService.getCurrent5mCandle();
//        Candle lastSavedCandle = newCandleService.getPrior5mCandle();
//
//
//        assertNotNull(lastSavedCandle);
//
//        // repo retrieve lastSaveCandle equivalent
//        Candle lastSavedCandleFromRepo = candleRepository.findAllByDateAndTime(lastSavedCandle.getTime().minusMinutes(5), lastSavedCandle.getTime()).get(0);
//
//        if (lastSavedCandleFromRepo == null) {
//            System.out.println(" findByDateTime returned null");
//            lastSavedCandleFromRepo = candleRepository.findByTime(lastSavedCandle.getTime());
//
//            if (lastSavedCandleFromRepo == null) {
//                System.out.println(" findByTime returned null");
//                List<Candle> candlesAllByTime = candleRepository.findAllByTime(lastSavedCandle.getTime());
//                System.out.println(" findAllByTime returned candles: " + candlesAllByTime);
//
//
//                if (lastSavedCandleFromRepo == null) {
//                    System.out.println();
//                    System.out.println("FindAll method:");
//                    List<Candle> candles = candleRepository.findAll();
//                    System.out.println(  "findAll returned candles: " + candles);
//
//                    for (Candle candle : candles) {
//                        if (candle.getTime().equals(lastSavedCandle.getTime())) {
//                            System.out.println(" findAll returned candle");
//                            lastSavedCandleFromRepo = candle;
//                            break;
//                        }
//                    }
//                }
//            }
//
//        }
//    }
}
