package com.mahou.bootjava.restaurantvoting.web.menu;

import com.mahou.bootjava.restaurantvoting.model.Dish;
import com.mahou.bootjava.restaurantvoting.model.Menu;
import com.mahou.bootjava.restaurantvoting.service.MenuService;
import com.mahou.bootjava.restaurantvoting.web.dish.AdminDishController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @GetMapping("/{id}")
    public Menu getWithDishes(@PathVariable int id) {
        return service.getWithDishes(id);
    }

    @GetMapping("/by-restaurant/{id}")
    public List<Menu> getAllWithDishesByRestaurantId(@PathVariable int id) {
        return service.getAllWithDishesByRestaurantId(id);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWithDishes(@PathVariable int id) {
        service.deleteById(id);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocationAndDishes(@RequestBody Menu menu) {
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

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateWithDishes(@RequestBody Menu menu, @PathVariable int id) {
        service.update(menu, id);
    }
}
