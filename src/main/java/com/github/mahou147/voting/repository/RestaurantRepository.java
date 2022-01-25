package com.github.mahou147.voting.repository;

import com.github.mahou147.voting.model.Restaurant;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Optional<Restaurant> get(int id);

    @Query("SELECT r FROM Restaurant r")
    Page<Restaurant> getAll(Pageable pageable);

    @Query(value = "SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m JOIN FETCH m.menuItems d",
            countQuery = "SELECT COUNT(r) FROM Restaurant r")
    Page<Restaurant> getAllWithMenuArchive(Pageable pageable);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m JOIN FETCH m.menuItems d WHERE r.id=?1")
    Restaurant getWithMenuArchive(int restaurantId);

    @Cacheable(value = "restaurants")
    @Query(value = "SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.menus m JOIN FETCH m.menuItems d " +
            "WHERE m.actualDate=:date", countQuery = "SELECT COUNT(r) FROM Restaurant r")
    Page<Restaurant> getAllWithMenuByDate(Pageable pageable, LocalDate date);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m JOIN FETCH m.menuItems d " +
            "WHERE r.id=:id AND m.actualDate=:date")
    Restaurant getWithMenuByDate(int id, LocalDate date);

    @Override
    @Transactional
    @CachePut(value = "restaurants", key = "#restaurant.id")
    Restaurant save(Restaurant restaurant);
}
