package com.mahou.bootjava.restaurantvoting.repository;

import com.mahou.bootjava.restaurantvoting.model.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

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

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    void deleteById(Integer integer);
}
