package org.example.webclient.controller;

import org.example.webclient.dto.PlaceDTO;
import org.example.webclient.service.WarehouseWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String showFreePlaces(Model model) {
        logger.info("Fetching free places");
        
        List<PlaceDTO> freePlaces = warehouseService.getFreePlaces();
        model.addAttribute("places", freePlaces);
        
        logger.info("Displaying {} free places", freePlaces != null ? freePlaces.size() : 0);
        return "places/free";
    }
}

