package org.example.placeservice.service;

import org.example.placeservice.dto.GivePlacesRequestDTO;
import org.example.placeservice.dto.PlaceDTO;
import org.example.placeservice.model.Place;
import org.example.placeservice.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public List<PlaceDTO> getAllFreePlaces() {
        List<Place> places = placeRepository.findByStatus(Place.RackStatus.FREE);
        return places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PlaceDTO> getPlacesByUserId(String userId) {
        List<Place> places = placeRepository.findByTenantId(userId);
        return places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void givePlacesToUser(GivePlacesRequestDTO request) {
        List<String> placeIds = request.getPlaceIds();
        String userId = request.getUserId();

        for (String placeId : placeIds) {
            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new RuntimeException("Place not found: " + placeId));
            
            if (place.getStatus() != Place.RackStatus.FREE) {
                throw new RuntimeException("Place is not free: " + placeId);
            }

            place.setStatus(Place.RackStatus.OCCUPIED);
            place.setTenantId(userId);
            placeRepository.save(place);
        }
    }

    @Transactional
    public void makePlacesFree(List<String> placeIds) {
        for (String placeId : placeIds) {
            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new RuntimeException("Place not found: " + placeId));
            
            place.setStatus(Place.RackStatus.FREE);
            place.setTenantId(null);
            placeRepository.save(place);
        }
    }

    public List<PlaceDTO> getFreePlacesByType(String type) {
        Place.RackType rackType = Place.RackType.valueOf(type);
        List<Place> places = placeRepository.findByStatusAndType(Place.RackStatus.FREE, rackType);
        return places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PlaceDTO convertToDTO(Place place) {
        PlaceDTO.DimensionsDTO dimensions = new PlaceDTO.DimensionsDTO(
                place.getWidth(),
                place.getHeight(),
                place.getDepth()
        );

        return new PlaceDTO(
                place.getRackId(),
                place.getSectionCode(),
                place.getNumber(),
                place.getType().name(),
                place.getStatus().name(),
                place.getPricePerDay(),
                dimensions,
                place.getTenantId()
        );
    }
}

