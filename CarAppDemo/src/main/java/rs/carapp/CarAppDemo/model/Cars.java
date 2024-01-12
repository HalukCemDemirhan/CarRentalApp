package rs.carapp.CarAppDemo.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rs.carapp.CarAppDemo.enums.CarType;
import rs.carapp.CarAppDemo.enums.DamageCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "car")
public class Cars implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "model")
    private String model;
    @Column(name = "brand")
    private String brand;
    @Column(name = "year")
    private Integer year;
    @Column(name = "color")
    private String color;
    @Column(name = "kilometer")
    private long kilometer;
    @Column(name = "price")
    private long price;
    @Column(name = "availability")
    private boolean availability;
    @Enumerated(EnumType.STRING)
    @Column(name = "damage_condition")
    private DamageCondition damageCondition;


    @JsonManagedReference
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reservations> reservations = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "car_type")
    private CarType carType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getKilometer() {
        return kilometer;
    }

    public void setKilometer(long kilometer) {
        this.kilometer = kilometer;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public DamageCondition getDamageCondition() {
        return damageCondition;
    }

    public void setDamageCondition(DamageCondition damageCondition) {
        this.damageCondition = damageCondition;
    }

    public List<Reservations> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservations> reservations) {
        this.reservations = reservations;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }
}
