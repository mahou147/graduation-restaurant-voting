package com.mahou.bootjava.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"},
        name = "user_unique_vote_daily_idx")})
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Vote extends BaseEntity {

    @NotNull
    @org.hibernate.annotations.Generated(GenerationTime.ALWAYS)
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    //Use GenerationTime.NEVER when testing vote update after 11:00 AM real time
    @org.hibernate.annotations.Generated(GenerationTime.NEVER)
    @Column(name = "time", nullable = false)
    private LocalTime time;

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

    public Vote(Integer id, LocalDate date, LocalTime time) {
        super(id);
        this.date = date;
        this.time = time;
    }
}
