package com.mahou.bootjava.restaurantvoting.web.user;

import com.mahou.bootjava.restaurantvoting.model.User;
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
import java.util.Optional;

@RestController
@RequestMapping(value = AdminUserController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminUserController extends AbstractUserController {
    static final String URL = "/api/admin/users";

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping(value = "/by-email")
    public Optional<User> getByEmail(@RequestParam String email) {
        return super.findByEmailIgnoreCase(email);
    }

    @GetMapping
    public Page<User> getAll(@RequestParam(name = "p", defaultValue = "1") int pageIndex) {
        return super.findPage(pageIndex);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        User created = super.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        super.update(user, id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = super.get(id);
        user.setEnabled(enabled);
    }
}
