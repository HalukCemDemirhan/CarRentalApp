package rs.carapp.CarAppDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.carapp.CarAppDemo.model.Cars;
import rs.carapp.CarAppDemo.model.Reservations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Cars, Long> {


    @Query("SELECT c FROM Cars c WHERE c NOT IN (" +
            "SELECT r.car FROM Reservations r WHERE NOT (" +
            "(r.endingDay < :startingDay) OR " +
            "(r.startingDay > :endingDay)))")
    List<Cars> findAvailableCars(@Param("startingDay") LocalDate startingDay,
                                 @Param("endingDay") LocalDate endingDay);

}

