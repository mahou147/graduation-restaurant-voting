package com.github.mahou147.voting.web.restaurant;

import com.github.mahou147.voting.mappers.RestaurantMapper;
import com.github.mahou147.voting.model.Restaurant;
import com.github.mahou147.voting.service.RestaurantService;
import com.github.mahou147.voting.to.RestaurantTo;
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

import static com.github.mahou147.voting.util.ValidationUtil.assureIdConsistent;
import static com.github.mahou147.voting.util.ValidationUtil.checkNew;

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
        return RestaurantMapper.INSTANCE.toRestTo(service.get(id));
    }

    @Operation(summary = "get all Restaurants by pages")
    @GetMapping
    public Page<RestaurantTo> getAll(@RequestParam(name = "p", defaultValue = "1") int pageIndex) {
        return RestaurantMapper.INSTANCE.toRestTo(service.findPage(pageIndex - 1, 5));
    }

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

    @Operation(summary = "get Restaurant with menu archive")
    @GetMapping("/{id}/menu-archive")
    public Restaurant getWithMenuArchive(@PathVariable int id) {
        return service.getWithMenuArchive(id);
    }

    @Operation(summary = "get all Restaurants with menu archive")
    @GetMapping("/menu-archive")
    public Page<Restaurant> getAllWithMenuArchive(@RequestParam(name = "p", defaultValue = "1") int pageIndex) {
        return service.getAllWithMenuArchive(pageIndex - 1, 5);
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
        checkNew(restaurant);
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
        assureIdConsistent(restaurant, id);
        service.update(restaurant, id);
    }
}
