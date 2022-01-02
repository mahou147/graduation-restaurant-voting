package com.mahou.bootjava.restaurantvoting.web.dish;

import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.Dish;
import com.mahou.bootjava.restaurantvoting.repository.DishRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminDishController {
    static final String URL = "/api/admin/dishes";

    private final DishRepository repository;

    @Operation(summary = "get Dish")
    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Dish not found, id: " + id));
    }

    @Operation(summary = "get Dishes by menu")
    @GetMapping("/by-menu/{menuId}")
    public List<Dish> getAllByMenuId(@PathVariable Integer menuId) {
        return repository.getAllByMenuId(menuId);
    }


    @Operation(summary = "delete Dish")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.deleteExisted(id);
    }

    @Operation(summary = "create Dish")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish) {
        checkNew(dish);
        Assert.notNull(dish, "Dish must not be null");
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "update Dish")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update dish {}", id);
        assureIdConsistent(dish, id);
        checkNotFoundWithId(repository.save(dish), id);
    }
}
