package rs.carapp.CarAppDemo.controller;


import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import rs.carapp.CarAppDemo.common.ServiceResults;
import rs.carapp.CarAppDemo.repository.CarRepository;
import rs.carapp.CarAppDemo.model.Cars;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    CarRepository carRepository;

    @PostMapping
    public ServiceResults<Void> create(@RequestBody Cars car) {
        ServiceResults<Void> result = new ServiceResults<>();
        if (car.getModel() == null || car.getYear() == null ||
                car.getColor() == null || car.getKilometer() == 0 || car.getPrice() == 0 ||
                car.getDamageCondition() == null) {
            result.setSuccess(false);
            result.setErrorMessage("Please fill in all the parts");
        }

        carRepository.save(car);
        return result;
    }
    @GetMapping("/available")
    public List<Cars> getSpecificCars(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "ddMMyyyy") LocalDate startDate,
                                      @RequestParam(value = "endDate") @DateTimeFormat(pattern = "ddMMyyyy") LocalDate endDate) {
        return carRepository.findAvailableCars(startDate, endDate);
    }
    @GetMapping
    public List<Cars> getAllCars() {
        List<Cars> cars = carRepository.findAll();
        return cars;
    }
    @DeleteMapping("/{id}")
    public String deleteCar(@PathVariable Long id) {
        Cars car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
        carRepository.deleteById(id);
        return "Car with ID " + id + " is deleted";
    }
    @PutMapping("/{id}")
    public String updateStudent(@PathVariable Long id, @RequestBody Cars carUpdate) {
        Cars car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + id));

        car.setPrice(carUpdate.getPrice());
        car.setAvailability(carUpdate.isAvailability());
        car.setKilometer(carUpdate.getKilometer());

        carRepository.save(car);

        return "Car with ID " + id + " is updated";
    }

}
