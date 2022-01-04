package com.mahou.bootjava.restaurantvoting.service;

import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.Restaurant;
import com.mahou.bootjava.restaurantvoting.repository.RestaurantRepository;
import com.mahou.bootjava.restaurantvoting.to.RestaurantTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantTo getById(int id) {
        log.info("get {}", id);
        return repository.getById(id).orElseThrow(() -> new NotFoundException("Restaurant not found, id: " + id));
    }

    @Cacheable("restaurants")
    public Page<RestaurantTo> findPage(int pageIndex, int pageSize) {
        log.info("getAll");
        return repository.getAll(PageRequest.of(pageIndex, pageSize));
    }

    public Restaurant getWithMenuOfTheDay(int id) {
        log.info("get {} with menu of the day", id);
        return checkNotFoundWithId(repository.getWithMenuOfTheDay(id), id);
    }

    public Restaurant getWithMenuArchive(int id) {
        log.info("get {} with menu archive", id);
        return checkNotFoundWithId(repository.getWithMenuArchive(id), id);
    }

    public Page<Restaurant> getAllWithMenuOfTheDay(int pageIndex, int pageSize) {
        log.info("get all with menu of the day");
        return repository.getAllWithMenuOfTheDay(PageRequest.of(pageIndex, pageSize));
    }

    public Page<Restaurant> getAllWithMenuArchive(int pageIndex, int pageSize) {
        log.info("get all with menu archive");
        return repository.getAllWithMenuArchive(PageRequest.of(pageIndex, pageSize));
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void deleteById(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant, int id) {
        log.info("update {}", id);
        assureIdConsistent(restaurant, id);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant), id);
    }
}
