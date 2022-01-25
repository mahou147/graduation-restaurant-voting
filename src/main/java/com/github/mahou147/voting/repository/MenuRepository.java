package com.github.mahou147.voting.repository;

import com.github.mahou147.voting.model.Menu;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.id=?1")
    Optional<Menu> getWithDishes(int id);

    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m where m.restaurant.id=?1")
    Page<Menu> findAllByRestaurantId(Pageable pageable, int id);

    @Override
    @Transactional
    @CachePut(value = "menus", key = "#menu.id")
    Menu save(Menu menu);
}
