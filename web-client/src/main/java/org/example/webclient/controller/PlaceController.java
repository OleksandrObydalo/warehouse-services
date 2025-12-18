package org.example.webclient.controller;

import jakarta.servlet.http.HttpSession;
import org.example.webclient.dto.PlaceDTO;
import org.example.webclient.service.WarehouseWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller for managing warehouse places.
 */
@Controller
@RequestMapping("/places")
public class PlaceController {

    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);
    private final WarehouseWebService warehouseService;

    public PlaceController(WarehouseWebService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * Display free (available) places.
     */
    @GetMapping("/free")
    public String showFreePlaces(HttpSession session, Model model) {
        logger.info("Fetching free places");
        
        List<PlaceDTO> freePlaces = warehouseService.getFreePlaces();
        model.addAttribute("places", freePlaces);
        
        logger.info("Displaying {} free places", freePlaces != null ? freePlaces.size() : 0);
        return "places/free";
    }

    /**
     * Display places rented by a specific user.
     * Users can only view their own places.
     */
    @GetMapping("/user/{userId}")
    public String showUserPlaces(@PathVariable String userId, HttpSession session, Model model) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Fetching places for user: {} requested by: {}", userId, loggedInUserId);
        
        // ACCESS CONTROL: Users can only view their own places
        if (!userId.equals(loggedInUserId)) {
            logger.warn("User {} attempted to view places of user {}", loggedInUserId, userId);
            throw new RuntimeException("Access denied: You can only view your own places");
        }
        
        List<PlaceDTO> userPlaces = warehouseService.getPlacesByUserId(userId);
        model.addAttribute("places", userPlaces);
        model.addAttribute("userId", userId);
        
        logger.info("Displaying {} places for user {}", userPlaces != null ? userPlaces.size() : 0, userId);
        return "places/user";
    }

    /**
     * Redirect to logged-in user's places (convenience method).
     */
    @GetMapping("/my-places")
    public String showMyPlaces(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return "redirect:/places/user/" + userId;
    }
}

