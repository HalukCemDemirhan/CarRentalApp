package rs.carapp.CarAppDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.carapp.CarAppDemo.model.Reservations;
import rs.carapp.CarAppDemo.model.Cars;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservations, Integer> {
    @Query("SELECT COUNT(r) FROM Reservations r WHERE r.car = :car AND NOT (" +
            "(r.endingDay < :startingDay) OR " +
            "(r.startingDay > :endingDay) OR " +
            "(r.endingDay = :startingDay AND r.endingTime <= :startingHour) OR " +
            "(r.startingDay = :endingDay AND r.startingTime >= :endingHour))")
    Long countOverlappingReservation(@Param("car") Cars car, @Param("startingDay") LocalDate startingDay,
                                     @Param("endingDay") LocalDate endingDay, @Param("startingHour") Long startingHour,
                                     @Param("endingHour") Long endingHour);


/*
"SELECT COUNT(r) FROM Reservations r WHERE r.car = :car AND NOT (" +
            "(r.endingDay < :startingDay) OR " +
            "(r.startingDay > :endingDay) OR " +
            "(r.endingDay = :startingDay AND r.endingTime <= :startingHour) OR " +
            "(r.startingDay = :endingDay AND r.startingTime >= :endingHour))"
 */

    @Query("SELECT COUNT(r) FROM Reservations r WHERE r.car = :car AND NOT (" +
            "(r.endingDay < :startingDay) OR " +
            "(r.startingDay > :endingDay) OR " +
            "(r.endingDay = :startingDay AND r.endingTime <= :startingHour AND (r.username = :username)) OR " +
            "(r.startingDay = :endingDay AND r.startingTime >= :endingHour AND (r.username = :username)))")
    Long countOverlappingReservationWithUsername(@Param("car") Cars car, @Param("startingDay") LocalDate startingDay,
                                     @Param("endingDay") LocalDate endingDay, @Param("startingHour") Long startingHour,
                                     @Param("endingHour") Long endingHour, @Param("username") String username);

    List<Reservations> findReservationByUsername(String username);
    List<Reservations> findReservationById(Integer id);
    @Transactional
    @Query("SELECT r FROM Reservations r JOIN FETCH r.car WHERE r.id = :reservationId")
    Optional<Reservations> findByIdWithCar(@Param("reservationId") Integer reservationId);


}

