package com.mahou.bootjava.restaurantvoting.web;

import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.service.UserService;
import com.mahou.bootjava.restaurantvoting.util.ValidationUtil;
import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Optional;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.*;

@Slf4j
public abstract class AbstractUserController {
    @Autowired
    private UserService service;

    public User get(int id) {
        log.info("get {}", id);
        return service.findById(id).orElseThrow(() -> new NotFoundException("User not found, id: " + id));
    }

    public Optional<User> findByEmailIgnoreCase(String email) {
        log.info("getByEmail {}", email);
        return service.findByEmailIgnoreCase(email);
    }

    public Page<User> findPage(int pageIndex) {
        log.info("getAll");
        return service.findPage(pageIndex -1, 5);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.deleteById(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        return service.create(user);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }
}
