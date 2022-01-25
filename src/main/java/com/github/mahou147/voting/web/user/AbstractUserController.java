package com.github.mahou147.voting.web.user;

import com.github.mahou147.voting.model.User;
import com.github.mahou147.voting.service.UserService;
import com.github.mahou147.voting.to.UserTo;
import com.github.mahou147.voting.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Optional;

import static com.github.mahou147.voting.util.ValidationUtil.assureIdConsistent;
import static com.github.mahou147.voting.util.ValidationUtil.checkNew;

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
        return service.findById(id).orElseThrow(ErrorUtil.notFound(User.class, id));
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
        checkNew(user);
        return service.create(user);
    }

    public User create(UserTo userTo) {
        log.info("create {}", userTo);
        checkNew(userTo);
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
