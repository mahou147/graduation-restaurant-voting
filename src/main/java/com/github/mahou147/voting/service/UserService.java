package com.github.mahou147.voting.service;

import com.github.mahou147.voting.error.NotFoundException;
import com.github.mahou147.voting.model.User;
import com.github.mahou147.voting.repository.UserRepository;
import com.github.mahou147.voting.to.UserTo;
import com.github.mahou147.voting.util.UserUtil;
import com.github.mahou147.voting.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

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

    public Page<User> findPage(int pageIndex, int pageSize) {
        return repository.findAll(PageRequest.of(pageIndex, pageSize, Sort.Direction.ASC, "lastName", "email"));
    }

    public void delete(int id) {
        repository.deleteExisted(id);
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(repository.save(user));
    }

    public User create(UserTo userTo) {
        Assert.notNull(userTo, "user must not be null");
        User created = prepareAndSave(UserUtil.createNewFromTo(userTo));
        return repository.save(created);
    }

    public User update(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(ValidationUtil.checkNotFoundWithId(repository.save(user), user.id()));
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = findById(userTo.id()).orElseThrow(() -> new NotFoundException("User not found, id: " + userTo.id()));
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }
}
