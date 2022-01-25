package com.github.mahou147.voting.repository;

import com.github.mahou147.voting.model.MenuItem;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT d FROM MenuItem d WHERE d.menu.id=?1")
    Page<MenuItem> getAllByMenuId(Pageable pageable, Integer menuId);

    @Override
    @Transactional
    @CachePut(value = "menuItems", key = "#menuItem.id")
    MenuItem save(MenuItem menuItem);
}
