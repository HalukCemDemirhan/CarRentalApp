package rs.carapp.CarAppDemo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rs.carapp.CarAppDemo.common.ServiceResults;
import rs.carapp.CarAppDemo.model.*;
import rs.carapp.CarAppDemo.repository.CarRepository;
import rs.carapp.CarAppDemo.repository.ReservationRepository;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    CarRepository carRepository;


    @PostMapping
    public ServiceResults<Void> create(@RequestBody Reservations reservation) {
        ServiceResults<Void> result = new ServiceResults<>();

        if (reservation.getCar() == null || reservation.getUsername() == null ||
                reservation.getStartingTime() == null || reservation.getEndingTime() == null) {
            result.setSuccess(false);
            result.setErrorMessage("Please fill in all the parts");
            return result;
        }
        if (reservation.getEndingDay().isBefore(reservation.getStartingDay())) {
            result.setSuccess(false);
            result.setErrorMessage("The order of the dates are wrong");
            return result;
        }
        Long count = reservationRepository.countOverlappingReservation(
                reservation.getCar(), reservation.getStartingDay(),
                reservation.getEndingDay(), reservation.getStartingTime(), reservation.getEndingTime());
        boolean reservationExists = count != null && count > 0;

        Long countOverlapped = reservationRepository.countOverlappingReservationWithUsername(
                reservation.getCar(), reservation.getStartingDay(),
                reservation.getEndingDay(), reservation.getStartingTime(), reservation.getEndingTime(), reservation.getUsername()
        );
        boolean reservationExistsWithUsername = countOverlapped != null && countOverlapped > 0;

        if (reservationExistsWithUsername) {
            result.setSuccess(false);
            result.setErrorMessage("Reservation already exists for the given time, car and username");
            return result;
        }
        if (reservationExists) {
            result.setSuccess(false);
            result.setErrorMessage("Reservation already exists for the given time and car");
            return result; // Return the result immediately if a conflicting reservation exists
        }
        setPriceOfRent(reservation);
        Optional<Cars> car = carRepository.findById(reservation.getCar().getId());
        String model = car.get().getModel();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:6000/api/auth/signin" + "/" + reservation.getUsername();
        BillInfo billInfo = restTemplate.getForObject(url, BillInfo.class);
        assert billInfo != null;
        billInfo.setPriceOfRent(reservation.getPriceOfRent());
        BillRequirements billRequirements = new BillRequirements();

        BillRequirements.CustomerInfo customerInfo = billRequirements.new CustomerInfo();
        customerInfo.setLock(false);
        customerInfo.setName(billInfo.getName());
        customerInfo.setTaxID(billInfo.getTaxId());
        customerInfo.setStreet(billInfo.getAddress());
        customerInfo.setName(billInfo.getName());

        BillRequirements.Items item = billRequirements.new Items();
        item.setName("Car Rental"); // Change to setName if field is renamed
        item.setPriceType(50000);
        item.setPrice(reservation.getPriceOfRent() * 100);
        item.setQuantity(1000);
        item.setTaxPercent(1800);
        item.setTaxType(0);

        billRequirements.setCustomerInfo(customerInfo);
        billRequirements.setItemsList(Arrays.asList(item));
        billRequirements.setTerminalID("STR85"); // Use setTerminalId if field is renamed
        String userConvertedJson;
        try {
            userConvertedJson = new ObjectMapper().writeValueAsString(billRequirements);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("username", "tokenstore8");
        headers.add("password", "123");
        HttpEntity<String> requirements = new HttpEntity<>(userConvertedJson, headers);

        String dUrl = "http://34.78.111.33:9092/switch/v1/eArchive/sendDocument";

        String responseString = restTemplate.postForObject(dUrl, requirements, String.class);
        Gson gson = new GsonBuilder().create();
        final BillResponse userResponse = gson.fromJson(responseString, BillResponse.class);
        String billUrl = userResponse.getDetail().getHtmlUrl();
        reservationRepository.save(reservation);
        result.setData("Reservation with ID " + reservation.getId() + " has been created. This is your bill: " + billUrl);




        //TODO: burada başka bir servise post isteği atacaksın. Cevabını bekleyip linki ekle
        //TODO: username, adres, tc, car adı, fiyatı, onları görüntüle bunun hakkında bir class oluştur.
        //TODO: e arşiv ekledin, faturayı görüntüle

        result.setSuccess(true);
        return result;
    }


    @GetMapping
    public List<Reservations> getAllReservations() {
        List<Reservations> reservations = reservationRepository.findAll();
        return reservations;


    }

    @GetMapping("/{username}")
    public List<Reservations> getAllReservations(@PathVariable String username) {
        List<Reservations> reservations = reservationRepository.findReservationByUsername(username);

        return reservations;
    }
    @DeleteMapping("/{id}")
    public String deleteReservation(@PathVariable Integer id) {
        Reservations reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
        reservationRepository.deleteById(id);
        return "Reservation with ID " + id + " is deleted";
    }
    @PutMapping("/{id}")
    public String updateReservation(@PathVariable Integer id, @RequestBody Reservations carUpdate) {
        Reservations reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));

        reservation.setCar(carUpdate.getCar());
        reservation.setStartingDay(carUpdate.getStartingDay());
        reservation.setEndingDay(carUpdate.getEndingDay());

        reservationRepository.save(reservation);

        return "Car with ID " + id + " is updated";
    }
    private void setPriceOfRent(Reservations reservation) {
        Optional<Cars> carOptional = carRepository.findById(reservation.getCar().getId());
        if (carOptional.isPresent()) {
            Cars car = carOptional.get();
            long pricePerDay = car.getPrice();

            long daysBetween = ChronoUnit.DAYS.between(reservation.getStartingDay(), reservation.getEndingDay());
            long hoursCalculated = (daysBetween * 24) + (reservation.getEndingTime() - reservation.getStartingTime());
            long priceCalculated = (hoursCalculated / 24) * pricePerDay;
            reservation.setPriceOfRent(priceCalculated);
        } else {
            reservation.setPriceOfRent(0L);
        }
    }
}

