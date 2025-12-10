package org.example.placeservice.config;

import org.example.placeservice.model.Place;
import org.example.placeservice.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public void run(String... args) {
        // Initialize with sample data
        if (placeRepository.count() == 0) {
            placeRepository.save(new Place("r101", "A", 101, 
                    Place.RackType.STANDARD, Place.RackStatus.OCCUPIED, 
                    new BigDecimal("50.00"), 200, 300, 100, "u001"));
            
            placeRepository.save(new Place("r102", "A", 102, 
                    Place.RackType.STANDARD, Place.RackStatus.FREE, 
                    new BigDecimal("50.00"), 200, 300, 100, null));
            
            placeRepository.save(new Place("r201", null, 201, 
                    Place.RackType.REFRIGERATED, Place.RackStatus.FREE, 
                    new BigDecimal("120.50"), 250, 250, 150, null));
            
            placeRepository.save(new Place("r202", null, 202, 
                    Place.RackType.REFRIGERATED, Place.RackStatus.FREE, 
                    new BigDecimal("120.50"), 250, 250, 150, null));
            
            placeRepository.save(new Place("r301", "B", 301, 
                    Place.RackType.SECURE, Place.RackStatus.FREE, 
                    new BigDecimal("200.00"), 300, 300, 200, null));
        }
    }
}

