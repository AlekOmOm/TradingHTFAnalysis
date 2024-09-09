package dk.kea.tradinghtfanalysis.repository;

import dk.kea.tradinghtfanalysis.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Long> {
    List<Candle> findAllByDateAndTime(LocalDateTime start, LocalDateTime end);

}
