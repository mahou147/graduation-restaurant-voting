package com.mahou.bootjava.restaurantvoting.service;

import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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
        return repository.findAll(PageRequest.of(pageIndex, pageSize));
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User update(User user) {
        Assert.notNull(user, "user must not be null");
        return checkNotFoundWithId(repository.save(user), user.id());
    }
}
