package com.mahou.bootjava.restaurantvoting.web.user;

import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.service.UserService;
import com.mahou.bootjava.restaurantvoting.to.UserTo;
import com.mahou.bootjava.restaurantvoting.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Optional;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.assureIdConsistent;

@Slf4j
public abstract class AbstractUserController {
    @Autowired
    private UserService service;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

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
        return service.findPage(pageIndex - 1, 5);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        return service.create(user);
    }

    public User create(UserTo userTo) {
        log.info("create {}", userTo);
        ValidationUtil.checkNew(userTo);
        return service.create(userTo);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        service.update(userTo);
    }
}
