package com.mahou.bootjava.restaurantvoting.repository;

import com.mahou.bootjava.restaurantvoting.model.Restaurant;
import com.mahou.bootjava.restaurantvoting.to.RestaurantTo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT new com.mahou.bootjava.restaurantvoting.to.RestaurantTo(r.id, r.title, r.address, r.phone) " +
            "FROM Restaurant r WHERE r.id=?1")
    Optional<RestaurantTo> getById(int id);

    @Query("SELECT new com.mahou.bootjava.restaurantvoting.to.RestaurantTo(r.id, r.title, r.address, r.phone) " +
            "FROM Restaurant r")
    Page<RestaurantTo> getAll(Pageable pageable);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m JOIN FETCH m.dishes d")
    List<Restaurant> getAllWithMenuArchive();

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m JOIN FETCH m.dishes d WHERE r.id=?1")
    Restaurant getWithMenuArchive(int restaurantId);

    @Cacheable(value = "restaurants")
    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.menus m JOIN FETCH m.dishes d WHERE m.date = CURRENT_DATE")
    List<Restaurant> getAllWithMenuOfTheDay();

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m JOIN FETCH m.dishes d WHERE r.id=:id AND m.date = CURRENT_DATE")
    Restaurant getWithMenuOfTheDay(int id);

    @Override
    @Modifying
    @Transactional
    @CachePut(value = "restaurants", key = "#restaurant.id")
    Restaurant save(Restaurant restaurant);
}
