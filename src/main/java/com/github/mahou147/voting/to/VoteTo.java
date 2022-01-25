package com.github.mahou147.voting.to;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDate actualDate;

    private Integer restaurantId;

    public VoteTo(Integer id, LocalDate actualDate, Integer restaurantId) {
        super(id);
        this.actualDate = actualDate;
        this.restaurantId = restaurantId;
    }
}
