package com.mahou.bootjava.restaurantvoting.web.restaurant;

import com.mahou.bootjava.restaurantvoting.model.Restaurant;
import com.mahou.bootjava.restaurantvoting.service.RestaurantService;
import com.mahou.bootjava.restaurantvoting.to.RestaurantTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminRestaurantController {
    static final String URL = "/api/admin/restaurants";

    private RestaurantService service;

    @Operation(summary = "get Restaurant")
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        return service.getById(id);
    }

    @Operation(summary = "get all Restaurants by pages")
    @GetMapping
    public Page<RestaurantTo> getAll(@RequestParam(name = "p", defaultValue = "1") int pageIndex) {
        return service.findPage(pageIndex - 1, 5);
    }

    @Operation(summary = "get Restaurant with menu of the day")
    @GetMapping("/{id}/menu-of-the-day")
    public Restaurant getWithMenuOfTheDay(@PathVariable int id) {
        return service.getWithMenuOfTheDay(id);
    }

    @Operation(summary = "get all Restaurants with menu of the day")
    @GetMapping("/menu-of-the-day")
    public List<Restaurant> getAllWithMenuOfTheDay() {
        return service.getAllWithMenuOfTheDay();
    }

    @Operation(summary = "get Restaurant with menu archive")
    @GetMapping("/{id}/menu-archive")
    public Restaurant getWithMenuArchive(@PathVariable int id) {
        return service.getWithMenuArchive(id);
    }

    @Operation(summary = "get all Restaurants with menu archive")
    @GetMapping("/menu-archive")
    public List<Restaurant> getAllWithMenuArchive() {
        return service.getAllWithMenuArchive();
    }

    @Operation(summary = "delete Restaurant")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", key = "#id")
    public void deleteById(@PathVariable int id) {
        service.deleteById(id);
    }

    @Operation(summary = "create Restaurant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        Restaurant created = service.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "update Restaurant")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CachePut(value = "restaurants", key = "#id")
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        service.update(restaurant, id);
    }
}
