package com.mahou.bootjava.restaurantvoting.repository;

import com.mahou.bootjava.restaurantvoting.model.Menu;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends BaseRepository<Menu> {

    @Cacheable(value = "menus")
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.id=?1")
    Optional<Menu> getWithDishes(int id);

    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m where m.restaurant.id=?1")
    List<Menu> findAllByRestaurantId(Integer id);

    @Override
    @Modifying
    @Transactional
    @CachePut(value = "menus", key = "#menu.id")
    Menu save(Menu menu);
}
