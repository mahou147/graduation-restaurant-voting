package com.mahou.bootjava.restaurantvoting.service;

import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.repository.UserRepository;
import com.mahou.bootjava.restaurantvoting.to.UserTo;
import com.mahou.bootjava.restaurantvoting.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    public Optional<User> findByEmailIgnoreCase(String email) {
        Assert.notNull(email, "email must not be null");
        return repository.findByEmailIgnoreCase(email);
    }

    @Cacheable("users")
    public Page<User> findPage(int pageIndex, int pageSize) {
        return repository.findAll(PageRequest.of(pageIndex, pageSize, Sort.Direction.ASC, "lastName", "email"));
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        repository.deleteExisted(id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(prepareAndSave(user));
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(UserTo userTo) {
        Assert.notNull(userTo, "user must not be null");
        User created = prepareAndSave(UserUtil.createNewFromTo(userTo));
        return repository.save(created);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User update(User user) {
        Assert.notNull(user, "user must not be null");
        return checkNotFoundWithId(repository.save(user), user.id());
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo) {
        User user = findById(userTo.id()).orElseThrow(() -> new NotFoundException("User not found, id: " + userTo.id()));
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }
}
