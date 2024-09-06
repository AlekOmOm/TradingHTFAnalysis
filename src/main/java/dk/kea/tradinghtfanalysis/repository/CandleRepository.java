package dk.kea.tradinghtfanalysis.repository;

import dk.kea.tradinghtfanalysis.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Long> {

    Candle findByTime(LocalTime time);
    List<Candle> findAllByTime(LocalTime time);

}
