package com.github.mahou147.voting.web.restaurant;

import com.github.mahou147.voting.model.Restaurant;
import com.github.mahou147.voting.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = RestaurantController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class RestaurantController {
    static final String URL = "/api/restaurants";

    private RestaurantService service;

    @Operation(summary = "get Restaurant with menu of the day")
    @GetMapping("/{id}/menu-of-the-day")
    public Restaurant getWithMenuByDate(@PathVariable int id) {
        return service.getWithMenuByDate(id);
    }

    @Operation(summary = "get all Restaurants with menu of the day")
    @GetMapping("/menu-of-the-day")
    public Page<Restaurant> getAllWithMenuByDate(@RequestParam(name = "p", defaultValue = "1") int pageIndex) {
        return service.getAllWithMenuByDate(pageIndex - 1, 5);
    }
}
