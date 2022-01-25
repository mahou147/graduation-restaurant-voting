package com.github.mahou147.voting.service;

import com.github.mahou147.voting.model.Restaurant;
import com.github.mahou147.voting.repository.RestaurantRepository;
import com.github.mahou147.voting.util.DateTimeUtil;
import com.github.mahou147.voting.util.ErrorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static com.github.mahou147.voting.util.ValidationUtil.checkNotFoundWithId;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private static final LocalDate TODAY = DateTimeUtil.setCurrentDate();

    private final RestaurantRepository repository;

    public Restaurant get(int id) {
        log.info("get {}", id);
        return repository.get(id).orElseThrow(ErrorUtil.notFound(Restaurant.class, id));
    }

    public Page<Restaurant> findPage(int pageIndex, int pageSize) {
        log.info("getAll");
        return repository.getAll(PageRequest.of(pageIndex, pageSize));
    }

    public Restaurant getWithMenuByDate(int id) {
        log.info("get {} with menu of the day", id);
        return checkNotFoundWithId(repository.getWithMenuByDate(id, TODAY), id);
    }

    public Restaurant getWithMenuArchive(int id) {
        log.info("get {} with menu archive", id);
        return checkNotFoundWithId(repository.getWithMenuArchive(id), id);
    }

    @Caching(
            cacheable = {
                    @Cacheable("restaurants"),
                    @Cacheable("menus"),
                    @Cacheable("menuItems")
            }
    )
    public Page<Restaurant> getAllWithMenuByDate(int pageIndex, int pageSize) {
        log.info("get all with menu of the day");
        return repository.getAllWithMenuByDate(PageRequest.of(pageIndex, pageSize), TODAY);
    }

    public Page<Restaurant> getAllWithMenuArchive(int pageIndex, int pageSize) {
        log.info("get all with menu archive");
        return repository.getAllWithMenuArchive(PageRequest.of(pageIndex, pageSize));
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void deleteById(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant, int id) {
        log.info("update {}", id);
        Assert.notNull(restaurant, "restaurant must not be null");
        repository.save(restaurant);
    }
}
