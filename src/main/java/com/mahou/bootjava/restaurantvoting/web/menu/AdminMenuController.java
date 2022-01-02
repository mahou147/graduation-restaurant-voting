package com.mahou.bootjava.restaurantvoting.web.menu;

import com.mahou.bootjava.restaurantvoting.model.Dish;
import com.mahou.bootjava.restaurantvoting.model.Menu;
import com.mahou.bootjava.restaurantvoting.service.MenuService;
import com.mahou.bootjava.restaurantvoting.web.dish.AdminDishController;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = AdminMenuController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminMenuController {
    static final String URL = "/api/admin/menus";

    private final MenuService service;

    private final AdminDishController adminDishController;

    @Operation(summary = "get Menu with dishes")
    @GetMapping("/{id}")
    public Menu getWithDishes(@PathVariable int id) {
        return service.getWithDishes(id);
    }

    @Operation(summary = "get all Menus with dishes by restaurant")
    @GetMapping("/by-restaurant/{id}")
    public List<Menu> getAllWithDishesByRestaurantId(@PathVariable int id) {
        return service.getAllWithDishesByRestaurantId(id);
    }

    @Operation(summary = "delete Menu with dishes")
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWithDishes(@PathVariable int id) {
        service.deleteById(id);
    }

    @Operation(summary = "create Menu with dishes")
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocationAndDishes(@Valid @RequestBody Menu menu) {
        Menu created = service.create(menu);
        List<Dish> dishes = menu.getDishes();
        dishes.forEach(dish -> dish.setMenu(menu));
        List<ResponseEntity<Dish>> dishesEntity = dishes.stream().map(adminDishController::createWithLocation)
                .collect(Collectors.toList());
        List<Dish> newDishes = dishesEntity.stream().map(HttpEntity::getBody).collect(Collectors.toList());
        created.setDishes(newDishes);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "update Menu and dishes")
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateWithDishes(@Valid @RequestBody Menu menu, @PathVariable int id) {
        service.update(menu, id);
    }
}
