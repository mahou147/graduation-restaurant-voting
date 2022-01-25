package com.github.mahou147.voting.web.menuitem;

import com.github.mahou147.voting.model.MenuItem;
import com.github.mahou147.voting.repository.MenuItemRepository;
import com.github.mahou147.voting.service.MenuItemService;
import com.github.mahou147.voting.util.ErrorUtil;
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

import static com.github.mahou147.voting.util.ValidationUtil.assureIdConsistent;
import static com.github.mahou147.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuItemController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminMenuItemController {
    static final String URL = "/api/admin/menu-items";

    private final MenuItemRepository repository;

    private final MenuItemService service;

    @Operation(summary = "get MenuItem")
    @GetMapping("/{id}")
    public MenuItem getById(@PathVariable int id) {
        log.info("get {}", id);
        return repository.findById(id).orElseThrow(ErrorUtil.notFound(MenuItem.class, id));
    }

    @Operation(summary = "get MenuItem by menu")
    @GetMapping("/by-menu/{menuId}")
    public Page<MenuItem> getAllByMenuId(@RequestParam(name = "p", defaultValue = "1") int pageIndex,
                                         @PathVariable Integer menuId) {
        log.info("getAll");
        return repository.getAllByMenuId(PageRequest.of(pageIndex - 1, 5), menuId);
    }

    @Operation(summary = "delete MenuItem")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @Operation(summary = "create MenuItem")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> createWithLocation(@Valid @RequestBody MenuItem menuItem, @RequestParam Integer menuId) {
        checkNew(menuItem);
        MenuItem created = service.create(menuItem, menuId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "update MenuItem")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuItem menuItem, @PathVariable int id) {
        assureIdConsistent(menuItem, id);
        service.update(menuItem, id);
    }
}
