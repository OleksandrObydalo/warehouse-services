package org.example.placeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.placeservice.dto.GivePlacesRequestDTO;
import org.example.placeservice.dto.PlaceDTO;
import org.example.placeservice.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@Tag(name = "Place Service", description = "API для управління складськими місцями")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @Operation(summary = "Отримати всі вільні місця", 
               description = "Повертає список всіх вільних місць на складі")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список вільних місць",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PlaceDTO.class)))),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера")
    })
    @GetMapping("/free")
    public ResponseEntity<List<PlaceDTO>> getAllFreePlaces() {
        try {
            List<PlaceDTO> places = placeService.getAllFreePlaces();
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Отримати вільні місця за типом",
               description = "Повертає список вільних місць відповідного типу (STANDARD, REFRIGERATED, SECURE)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список вільних місць",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PlaceDTO.class)))),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера")
    })
    @GetMapping("/free/type/{type}")
    public ResponseEntity<List<PlaceDTO>> getFreePlacesByType(
            @Parameter(description = "Тип місця: STANDARD, REFRIGERATED або SECURE", required = true)
            @PathVariable String type) {
        try {
            List<PlaceDTO> places = placeService.getFreePlacesByType(type);
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Отримати місця користувача",
               description = "Повертає список місць, які зайняті вказаним користувачем")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список місць користувача",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PlaceDTO.class)))),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaceDTO>> getPlacesByUserId(
            @Parameter(description = "ID користувача", required = true)
            @PathVariable String userId) {
        try {
            List<PlaceDTO> places = placeService.getPlacesByUserId(userId);
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Видати місця користувачу",
               description = "Призначає вказані місця користувачу. Місця повинні бути вільними.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Місця успішно призначені"),
        @ApiResponse(responseCode = "400", description = "Невірний запит (місце зайняте або не існує)"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера")
    })
    @PostMapping("/give")
    public ResponseEntity<Void> givePlacesToUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Запит на призначення місць",
                    required = true,
                    content = @Content(schema = @Schema(implementation = GivePlacesRequestDTO.class)))
            @RequestBody GivePlacesRequestDTO request) {
        try {
            placeService.givePlacesToUser(request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Звільнити місця",
               description = "Звільняє вказані місця, роблячи їх доступними для оренди")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Місця успішно звільнені"),
        @ApiResponse(responseCode = "400", description = "Невірний запит (місце не існує)"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера")
    })
    @PostMapping("/free")
    public ResponseEntity<Void> makePlacesFree(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Список ID місць для звільнення",
                    required = true)
            @RequestBody List<String> placeIds) {
        try {
            placeService.makePlacesFree(placeIds);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

