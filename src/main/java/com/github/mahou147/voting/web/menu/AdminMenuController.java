package com.github.mahou147.voting.web.menu;

import com.github.mahou147.voting.model.Menu;
import com.github.mahou147.voting.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.github.mahou147.voting.util.ValidationUtil.assureIdConsistent;
import static com.github.mahou147.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminMenuController {
    static final String URL = "/api/admin/menus";

    private final MenuService service;

    @Operation(summary = "get Menu with dishes")
    @GetMapping("/{id}")
    public Menu getWithDishes(@PathVariable int id) {
        return service.getWithDishes(id);
    }

    @Operation(summary = "get all Menus with dishes by restaurant")
    @GetMapping("/by-restaurant/{id}")
    public Page<Menu> getAllWithDishesByRestaurantId(@RequestParam(name = "p", defaultValue = "1") int pageIndex,
                                                     @PathVariable int id) {
        return service.getAllWithDishesByRestaurantId(pageIndex - 1, 5, id);
    }

    @Operation(summary = "delete Menu")
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.deleteById(id);
    }

    @Operation(summary = "create Menu")
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu, @RequestParam Integer restId) {
        checkNew(menu);
        Menu created = service.create(menu, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "update Menu")
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id, @RequestParam Integer restId) {
        assureIdConsistent(menu, id);
        service.update(menu, id, restId);
    }
}
