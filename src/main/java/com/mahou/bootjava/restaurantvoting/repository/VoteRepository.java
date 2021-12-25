package com.mahou.bootjava.restaurantvoting.repository;

import com.mahou.bootjava.restaurantvoting.model.Vote;
import com.mahou.bootjava.restaurantvoting.to.VoteTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT new com.mahou.bootjava.restaurantvoting.to.VoteTo(v.id, v.date, v.restaurant.id) FROM Vote v " +
            "WHERE v.user.id=?1 AND v.date=CURRENT_DATE")
    Optional<VoteTo> findByAuthUser(int userId);

    @Override
    @Modifying
    @Transactional
    Vote save(Vote vote);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.user.id=?1 and v.date=CURRENT_DATE")
    void deleteByAuthUser(int userId);
}
