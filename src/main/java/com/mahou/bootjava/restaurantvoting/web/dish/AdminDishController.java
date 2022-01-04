package com.mahou.bootjava.restaurantvoting.web.dish;

import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.Dish;
import com.mahou.bootjava.restaurantvoting.repository.DishRepository;
import com.mahou.bootjava.restaurantvoting.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = AdminDishController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminDishController {
    static final String URL = "/api/admin/dishes";

    private final DishRepository repository;

    private final DishService service;

    @Operation(summary = "get Dish")
    @GetMapping("/{id}")
    public Dish getById(@PathVariable int id) {
        log.info("get {}", id);
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Dish not found, id: " + id));
    }

    @Operation(summary = "get Dishes by menu")
    @GetMapping("/by-menu/{menuId}")
    public Page<Dish> getAllByMenuId(@RequestParam(name = "p", defaultValue = "1") int pageIndex, @PathVariable Integer menuId) {
        log.info("getAll");
        return repository.getAllByMenuId(PageRequest.of(pageIndex - 1, 5), menuId);
    }

    @Operation(summary = "delete Dish")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @Operation(summary = "create Dish")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @RequestParam Integer menuId) {
        Dish created = service.create(dish, menuId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "update Dish")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @RequestParam Integer menuId) {
        service.update(dish, id, menuId);
    }
}
