package com.mahou.bootjava.restaurantvoting.web.restaurant;

import com.mahou.bootjava.restaurantvoting.model.Restaurant;
import com.mahou.bootjava.restaurantvoting.service.RestaurantService;
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

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping
    public Page<Restaurant> getAll(@RequestParam(name = "p", defaultValue = "1") int pageIndex) {
        return service.findPage(pageIndex - 1, 5);
    }

    @GetMapping("/{id}/menu-of-the-day")
    public Restaurant getWithMenuOfTheDay(@PathVariable int id) {
        return service.getWithMenuOfTheDay(id);
    }

    @GetMapping("/menu-of-the-day")
    public List<Restaurant> getAllWithMenuOfTheDay() {
        return service.getAllWithMenuOfTheDay();
    }

    @GetMapping("/{id}/menu-archive")
    public Restaurant getWithMenuArchive(@PathVariable int id) {
        return service.getWithMenuArchive(id);
    }

    @GetMapping("/menu-archive")
    public List<Restaurant> getAllWithMenuArchive() {
        return service.getAllWithMenuArchive();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", key = "#id")
    public void deleteById(@PathVariable int id) {
        service.deleteById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        Restaurant created = service.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CachePut(value = "restaurants", key = "#id")
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        service.update(restaurant, id);
    }
}
