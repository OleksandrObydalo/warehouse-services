package org.example.placeservice.repository;

import org.example.placeservice.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {
    List<Place> findByStatus(Place.RackStatus status);
    List<Place> findByTenantId(String tenantId);
    List<Place> findByStatusAndType(Place.RackStatus status, Place.RackType type);
}

