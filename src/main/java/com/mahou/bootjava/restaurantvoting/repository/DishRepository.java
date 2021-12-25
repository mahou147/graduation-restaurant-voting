package com.mahou.bootjava.restaurantvoting.repository;

import com.mahou.bootjava.restaurantvoting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Query("SELECT d FROM Dish d WHERE d.menu.id=?1")
    List<Dish> getAllByMenuId(Integer menuId);

    @Override
    @Modifying
    @Transactional
    Dish save(Dish dish);

    @Override
    @Modifying
    @Transactional
    void deleteById(Integer integer);
}
