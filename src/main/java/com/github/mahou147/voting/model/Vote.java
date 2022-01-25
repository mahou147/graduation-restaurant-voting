package com.github.mahou147.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "actual_date"},
        name = "user_unique_vote_daily_idx")})
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @NotNull
    @org.hibernate.annotations.Generated(GenerationTime.ALWAYS)
    @Column(name = "actual_date", nullable = false)
    private LocalDate actualDate;

    @NotNull
    @Column(name = "actual_time", nullable = false)
    private LocalTime actualTime;

    @NotNull
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Vote(LocalDate date, LocalTime time, User user) {
        this(null, date, time, user, null);
    }

    public Vote(Integer id, LocalDate date, LocalTime time, User user, Restaurant restaurant) {
        super(id);
        this.actualDate = date;
        this.actualTime = time;
        this.user = user;
        this.restaurant = restaurant;
    }
}
