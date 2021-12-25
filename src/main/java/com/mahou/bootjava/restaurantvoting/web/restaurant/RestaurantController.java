package com.mahou.bootjava.restaurantvoting.web.restaurant;

import com.mahou.bootjava.restaurantvoting.model.Restaurant;
import com.mahou.bootjava.restaurantvoting.service.RestaurantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class RestaurantController {
    static final String URL = "/api/restaurants";

    private RestaurantService service;

    @GetMapping("/{id}/menu-of-the-day")
    public Restaurant getWithMenuOfTheDay(@PathVariable int id) {
        return service.getWithMenuOfTheDay(id);
    }

    @GetMapping("/menu-of-the-day")
    public List<Restaurant> getAllWithMenuOfTheDay() {
        return service.getAllWithMenuOfTheDay();
    }
}
