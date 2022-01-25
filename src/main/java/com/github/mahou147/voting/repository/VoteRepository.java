package com.github.mahou147.voting.repository;

import com.github.mahou147.voting.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.actualDate=CURRENT_DATE")
    Optional<Vote> findByUserId(int userId);

    @Override
    @Transactional
    Vote save(Vote vote);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.user.id=?1 and v.actualDate=CURRENT_DATE")
    void deleteByUserId(int userId);
}
