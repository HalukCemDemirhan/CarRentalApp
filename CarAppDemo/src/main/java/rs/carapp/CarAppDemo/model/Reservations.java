package rs.carapp.CarAppDemo.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import rs.carapp.CarAppDemo.repository.CarRepository;


@Data
@Setter
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "reservations")

public class Reservations implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //day, hours, user, car, price
    @Column(name = "starting_day")
    private LocalDate startingDay;
    @Column(name = "ending_day")
    private LocalDate endingDay;
    @Column(name = "start_time")
    private Long startingTime;
    @Column(name = "end_time")
    private Long endingTime;
    @Column(name = "username")
    private String username;
    @Column(name = "price_of_rent")
    private Long priceOfRent;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cars_id")
    private Cars car;

    public Reservations() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartingDay() {
        return startingDay;
    }

    public void setStartingDay(LocalDate startingDay) {
        this.startingDay = startingDay;
    }

    public LocalDate getEndingDay() {
        return endingDay;
    }

    public void setEndingDay(LocalDate endingDay) {
        this.endingDay = endingDay;
    }

    public Long getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Long startingTime) {
        this.startingTime = startingTime;
    }

    public Long getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Long endingTime) {
        this.endingTime = endingTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPriceOfRent() {
        return priceOfRent;
    }

    public void setPriceOfRent(Long priceOfRent) {
        this.priceOfRent = priceOfRent;
    }

    public Cars getCar() {
        return car;
    }

    public void setCar(Cars car) {
        this.car = car;
    }

//günlük fiyat 24 saatten küçükse direk bir günlük fiyatı al
    //artarsa güne göre fiyat al
    //başlangıç tarihi, bitiş tarihi, başlangıç saati bitiş saati
    //eğer başlangıç günü bitiş gününden büyük ise izin verme
    //aynı gün içerisinde başlangıç saati bitiş saatinden büyük ise izin verme
    //aynı username'deki kişi eğer aynı zamana kiralama koymaya çalışırsa izin verme
    // aynı saate başka bir kişi koymaya çalışırsa ona da izin verme


    //aynı şekilde car availability diye bir şey yapıp aslında hem saati, hem günü
    //hem de username i alan bişey yapılabilir.
}
